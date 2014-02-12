/**
 * Texts domain model object specification.
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

class TextsSpec extends Specification with BeforeExample {

  val defaultUsers = Seq(
    User(username = "u1"),
    User(username = "u2"))

  val defaultTexts = Seq(
    Text(title = "t1", contents = "text 1", privacyLevel = 1, creatorID = defaultUsers(0).id),
    Text(title = "t2", contents = "text 2", privacyLevel = 2, creatorID = defaultUsers(0).id),
    Text(title = "t3", contents = "text 3", privacyLevel = 3, creatorID = defaultUsers(1).id))

  def before {
    running(FakeApplication()) {
      DB.withSession { implicit s: Session =>
        cleanDatabase
        defaultUsers.foreach(Users.insert)
        defaultTexts.foreach(Texts.insert)
      }
    }
  }

  "Texts model" should {

    "be inserted" in {
      running(FakeApplication()) {
        val text = Text(title = "x", contents = "y", privacyLevel = 1, creatorID = defaultUsers(0).id)
        DB.withSession { implicit s =>
          val user = Users.retrieve(defaultUsers(0).id)
          Texts.insert(text)
          val retrieved = Texts.retrieve(text.id)
          retrieved.get.title must be equalTo(text.title)
        }
      }
    }

    "not insert two texts with the same id" in {
      running(FakeApplication()) {
        val text = Text(id = defaultTexts(0).id, title = "x", contents = "y", privacyLevel = 1, creatorID = defaultUsers(0).id)
        DB.withSession { implicit s =>
          Texts.insert(text) must throwA[JdbcSQLException]
        }
      }
    }

    "be retrieved by id" in {
      running(FakeApplication()) {
        val expected = defaultTexts(0)
        DB.withSession { implicit s =>
          val text = Texts.retrieve(expected.id)
          text must beSome
          text.get.title must be equalTo (expected.title)
        }
      }
    }

    "return None if id does not exist" in {
      running(FakeApplication()) {
        DB.withSession { implicit s =>
          val text = Texts.retrieve(UUID.randomUUID)
          text must beNone
        }
      }
    }

    "be updated" in {
      running(FakeApplication()) {
        val textToUpdate = defaultTexts(1).copy(title = "updated")
        DB.withSession { implicit s =>
          Texts.update(textToUpdate)
          val text = Texts.retrieve(textToUpdate.id)
          text must beSome
          text.get.title must be equalTo (textToUpdate.title)
        }
      }
    }

    "modify update time on update" in {
      running(FakeApplication()) {
        val textToUpdate = defaultTexts(1).copy(title = "updated")
        DB.withSession { implicit s =>
          Texts.update(textToUpdate)
          val text = Texts.retrieve(textToUpdate.id)
          text must beSome
          text.get.updateTimeUTC must not be equalTo(text.get.creationTimeUTC)
        }
      }
    }

    "be deleted" in {
      running(FakeApplication()) {
        val textToDelete = defaultTexts(2)
        DB.withSession { implicit s =>
          Texts.delete(textToDelete)
          val text = Texts.retrieve(textToDelete.id)
          text must beNone
        }
      }
    }

  }

}
