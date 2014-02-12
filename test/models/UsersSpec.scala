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
import models._;
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

class UsersSpec extends Specification with BeforeExample {

  val defaultUsers = Seq(
    User(username = "u1"),
    User(username = "u2"),
    User(username = "u3"))

  def before {
    running(FakeApplication()) {
      DB.withSession { implicit s: Session =>
        cleanDatabase
        defaultUsers.foreach(Users.insert)
      }
    }
  }

  "Users model" should {

    "be inserted" in {
      running(FakeApplication()) {
        val user = User(username = "x")
        DB.withSession { implicit s =>
          Users.insert(user)
          val retrieved = Users.retrieve(user.id)
          retrieved.get.username must be equalTo(user.username)
        }
      }
    }

    "not insert two users with the same id" in {
      running(FakeApplication()) {
        val user = User(id = defaultUsers(0).id, username = "x")
        DB.withSession { implicit s =>
          Users.insert(user) must throwA[JdbcSQLException]
        }
      }
    }

    "be retrieved by id" in {
      running(FakeApplication()) {
        val expected = defaultUsers(0)
        DB.withSession { implicit s =>
          val user = Users.retrieve(expected.id)
          user must beSome
          user.get.username must be equalTo(expected.username)
        }
      }
    }

    "return None if id does not exist" in {
      running(FakeApplication()) {
        DB.withSession { implicit s =>
          val user = Users.retrieve(UUID.randomUUID)
          user must beNone
        }
      }
    }

    "be updated" in {
      running(FakeApplication()) {
        val userToUpdate = defaultUsers(1).copy(username = "updated")
        DB.withSession { implicit s =>
          Users.update(userToUpdate)
          val user = Users.retrieve(userToUpdate.id)
          user must beSome
          user.get.username must be equalTo(userToUpdate.username)
        }
      }
    }

    "modify update time on update" in {
      running(FakeApplication()) {
        val userToUpdate = defaultUsers(1).copy(username = "updated")
        DB.withSession { implicit s =>
          Users.update(userToUpdate)
          val user = Users.retrieve(userToUpdate.id)
          user must beSome
          user.get.updateTimeUTC must not be equalTo(user.get.creationTimeUTC)
        }
      }
    }

    "be deleted" in {
      running(FakeApplication()) {
        val userToDelete = defaultUsers(2)
        DB.withSession { implicit s =>
          Users.delete(userToDelete)
          val user = Users.retrieve(userToDelete.id)
          user must beNone
        }
      }
    }

  }

}
