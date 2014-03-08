package daos

import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID
import models.User
import org.joda.time._
import play.api.db.slick.Config.driver.simple._
import utils.DateTimeHelper._

/** Slick table mapping. */
class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def firstName = column[Option[String]]("FIRSTNAME")
  def lastName = column[Option[String]]("LASTNAME")
  def fullName = column[Option[String]]("FULLNAME")
  def email = column[Option[String]]("EMAIL")
  def avatarURL = column[Option[String]]("AVATAR_URL")
  def creationTimeUTC = column[DateTime]("CREATION_TIME_UTC", O.NotNull)
  def updateTimeUTC = column[DateTime]("UPDATE_TIME_UTC", O.NotNull)
  def isSoftDeleted = column[Boolean]("IS_SOFT_DELETED", O.NotNull)
  def * = (id, firstName, lastName, fullName, email, avatarURL, creationTimeUTC, updateTimeUTC, isSoftDeleted) <>
    (User.tupled, User.unapply)
}

/** Users data access object. */
class UsersDAO {

  /**
   * Soft deletes a user.
   *
   * @param user The user to be soft deleted.
   * @param s A database session.
   */
  def delete(user: User)(implicit s: Session) = {
    val userToDelete = user.copy(updateTimeUTC = utcNow, isSoftDeleted = true)
    singleUserQuery(user.id).update(userToDelete)
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
   * Updates a user.
   *
   * @param user The user to be updated.
   * @param s A database session.
   */
  def update(user: User)(implicit s: Session) = {
    val userToUpdate = user.copy(updateTimeUTC = utcNow)
    singleUserQuery(user.id).update(userToUpdate)
  }

  /** A query for a table. */
  private val q = TableQuery[UsersTable]

  /** A query for a single, not deleted user. */
  private def singleUserQuery(id: UUID) = q.where(_.id === id).filter(_.isSoftDeleted === false)
}
