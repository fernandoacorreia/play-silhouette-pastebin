/**
 * Users security model object specification.
 *
 * Copyright 2014 Mohiva Organisation (license at mohiva dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import java.util.UUID
import models._
import org.h2.jdbc.JdbcSQLException
import org.specs2.mutable._
import org.specs2.specification._
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import play.api.Logger
import play.api.test._
import play.api.test.Helpers._
import play.api.Play.current
import scala.slick.jdbc.{ StaticQuery => Q }
import Q.interpolation
import utils.TestHelper._
import daos.UserDAO

class UsersSpec extends Specification with BeforeExample {

  val userDAO = new UserDAO

  val defaultUsers = Seq(
    User(email = Some("u1@example.com")),
    User(email = Some("u2@example.com")),
    User(email = Some("u3@example.com")))

  def before {
    running(FakeApplication()) {
      DB.withSession { implicit s: Session =>
        cleanDatabase
        defaultUsers.foreach(userDAO.insert)
      }
    }
  }

  "Users model" should {

    "be inserted" in {
      running(FakeApplication()) {
        val user = User(email = Some("x@example.com"))
        DB.withSession { implicit s =>
          userDAO.insert(user)
          val retrieved = userDAO.retrieve(user.id)
          retrieved.get.email must be equalTo (user.email)
        }
      }
    }

    "not insert two users with the same id" in {
      running(FakeApplication()) {
        val user = User(id = defaultUsers(0).id, email = Some("x@example.com"))
        DB.withSession { implicit s =>
          userDAO.insert(user) must throwA[JdbcSQLException]
        }
      }
    }

    "be retrieved by id" in {
      running(FakeApplication()) {
        val expected = defaultUsers(0)
        DB.withSession { implicit s =>
          val user = userDAO.retrieve(expected.id)
          user must beSome
          user.get.email must be equalTo (expected.email)
        }
      }
    }

    "return None if id does not exist" in {
      running(FakeApplication()) {
        DB.withSession { implicit s =>
          val user = userDAO.retrieve(UUID.randomUUID)
          user must beNone
        }
      }
    }

    "be updated" in {
      running(FakeApplication()) {
        val userToUpdate = defaultUsers(1).copy(email = Some("updated@example.com"))
        DB.withSession { implicit s =>
          userDAO.update(userToUpdate)
          val user = userDAO.retrieve(userToUpdate.id)
          user must beSome
          user.get.email must be equalTo (userToUpdate.email)
        }
      }
    }

    "modify update time on update" in {
      running(FakeApplication()) {
        val userToUpdate = defaultUsers(1).copy(email = Some("updated@example.com"))
        DB.withSession { implicit s =>
          userDAO.update(userToUpdate)
          val user = userDAO.retrieve(userToUpdate.id)
          user must beSome
          user.get.updateTimeUTC must not be equalTo(user.get.creationTimeUTC)
        }
      }
    }

    "be deleted" in {
      running(FakeApplication()) {
        val userToDelete = defaultUsers(2)
        DB.withSession { implicit s =>
          userDAO.delete(userToDelete)
          val user = userDAO.retrieve(userToDelete.id)
          user must beNone
        }
      }
    }

  }

}
