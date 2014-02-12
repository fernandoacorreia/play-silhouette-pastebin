/**
 * Users security model object.
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

import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID
import play.api.db.slick.Config.driver.simple._
import org.joda.time._
import utils.DateTimeHelper._

/**
 * Represents a user.
 *
 * @param id This user's unique internal ID.
 * @param username This user's unique external identifier.
 * @param creationTimeUTC Date and time this user was created, in the UTC timezone.
 * @param updateTimeUTC Date and time this user was last updated or soft deleted, in the UTC timezone.
 * @param isSoftDeleted true if this user has been soft deleted, false otherwise.
 */
case class User(
  id: UUID = UUID.randomUUID,
  username: String,
  creationTimeUTC: DateTime = utcNow,
  updateTimeUTC: DateTime = utcNow,
  isSoftDeleted: Boolean = false)

/** Slick mapping table. */
class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def username = column[String]("USERNAME", O.NotNull)
  def creationTimeUTC = column[DateTime]("CREATION_TIME_UTC", O.NotNull)
  def updateTimeUTC = column[DateTime]("UPDATE_TIME_UTC", O.NotNull)
  def isSoftDeleted = column[Boolean]("IS_SOFT_DELETED", O.NotNull)
  def * = (id, username, creationTimeUTC, updateTimeUTC, isSoftDeleted) <>
    (User.tupled, User.unapply)
}

/** Users security model object. */
object Users {

  /** A query for a table. */
  private val q = TableQuery[Users]

  /**
   * Soft deletes a user.
   *
   * @param user The user to be soft deleted.
   * @param s A database session.
   */
  def delete(user: User)(implicit s: Session) {
    val userToDelete = user.copy(updateTimeUTC = utcNow, isSoftDeleted = true)
    singleUserQuery(user.id).update(userToDelete)
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id The ID of the user to be retrieved.
   * @param s A database session.
   * @returns Maybe a user.
   */
  def retrieve(id: UUID)(implicit s: Session): Option[User] = {
    singleUserQuery(id).firstOption
  }

  /**
   * Inserts a user into the database.
   *
   * @param user The user to be inserted.
   * @param s A database session.
   */
  def insert(user: User)(implicit s: Session) = {
    val userToInsert = user.copy(creationTimeUTC = utcNow, updateTimeUTC = utcNow)
    q.insert(userToInsert)
  }

  /**
   * Updates a user.
   *
   * @param user The user to be updated.
   * @param s A database session.
   */
  def update(user: User)(implicit s: Session) {
    val userToUpdate = user.copy(updateTimeUTC = utcNow)
    singleUserQuery(user.id).update(userToUpdate)
  }

  /** A query for a single, not deleted user. */
  private def singleUserQuery(id: UUID) = q.where(_.id === id).filter(_.isSoftDeleted === false)

}
