/**
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

import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID
import play.api.db.slick.Config.driver.simple._
import org.joda.time._
import utils.DateTimeHelper._

/**
 * Texts domain model object.
 *
 * @param id This text's unique internal ID.
 * @param title This text's title.
 * @param contents The main contents of this text.
 * @param privacyLevel The privacy level of this text.
 * @param sourceID If this text was forked from another, the ID of the original text.
 * @param creatorID The ID of the user that created this text.
 * @param creationTimeUTC Date and time this text was created, in the UTC timezone.
 * @param updateTimeUTC Date and time this text was last updated or soft deleted, in the UTC timezone.
 * @param isSoftDeleted true if this text has been soft deleted, false otherwise.
 */
case class Text(
  id: UUID = UUID.randomUUID,
  title: String,
  contents: String,
  privacyLevel: Int, // TODO ENUM
  sourceID: Option[UUID] = None,
  creatorID: UUID,
  creationTimeUTC: DateTime = utcNow,
  updateTimeUTC: DateTime = utcNow,
  isSoftDeleted: Boolean = false)

/** Slick mapping table. */
class Texts(tag: Tag) extends Table[Text](tag, "TEXTS") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def title = column[String]("TITLE", O.NotNull)
  def contents = column[String]("CONTENTS", O.NotNull)
  def privacyLevel = column[Int]("PRIVACY_LEVEL", O.NotNull)
  def sourceID = column[Option[UUID]]("SOURCE_ID")
  def creatorID = column[UUID]("CREATOR_ID", O.NotNull)
  def creationTimeUTC = column[DateTime]("CREATION_TIME_UTC", O.NotNull)
  def updateTimeUTC = column[DateTime]("UPDATE_TIME_UTC", O.NotNull)
  def isSoftDeleted = column[Boolean]("IS_SOFT_DELETED", O.NotNull)
  def * = (id, title, contents, privacyLevel, sourceID, creatorID, creationTimeUTC, updateTimeUTC, isSoftDeleted) <>
    (Text.tupled, Text.unapply)
}

/** Texts domain model object. */
object Texts {

  /** A query for a table. */
  private val q = TableQuery[Texts]

  /** A query for a single, not deleted text. */
  private def singleTextQuery(id: UUID) = q.where(_.id === id).filter(_.isSoftDeleted === false)

  /**
   * Soft deletes a text.
   *
   * @param text The text to be soft deleted.
   * @param s A database session.
   */
  def delete(text: Text)(implicit s: Session) {
    val textToDelete = text.copy(updateTimeUTC = utcNow, isSoftDeleted = true)
    singleTextQuery(text.id).update(textToDelete)
  }

  /**
   * Retrieves a text by ID.
   *
   * @param id The ID of the text to be retrieved.
   * @param s A database session.
   * @returns Maybe a text.
   */
  def retrieve(id: UUID)(implicit s: Session): Option[Text] = {
    singleTextQuery(id).firstOption
  }

  /**
   * Inserts a text into the database.
   *
   * @param text The text to be inserted.
   * @param s A database session.
   */
  def insert(text: Text)(implicit s: Session) = {
    val textToInsert = text.copy(creationTimeUTC = utcNow, updateTimeUTC = utcNow)
    q.insert(textToInsert)
  }

  /**
   * Updates a text.
   *
   * @param text The text to be updated.
   * @param s A database session.
   */
  def update(text: Text)(implicit s: Session) {
    val textToUpdate = text.copy(updateTimeUTC = utcNow)
    singleTextQuery(text.id).update(textToUpdate)
  }

}
