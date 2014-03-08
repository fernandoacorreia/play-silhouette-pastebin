package daos

import com.github.tototoshi.slick.H2JodaSupport._
import java.util.UUID
import models.Text
import play.api.db.slick.Config.driver.simple._
import org.joda.time._
import utils.DateTimeHelper._

/** Slick table mapping. */
class TextsTable(tag: Tag) extends Table[Text](tag, "TEXTS") {
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

/** Texts data access object. */
object TextsDAO {

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
   * Updates a text.
   *
   * @param text The text to be updated.
   * @param s A database session.
   */
  def update(text: Text)(implicit s: Session) {
    val textToUpdate = text.copy(updateTimeUTC = utcNow)
    singleTextQuery(text.id).update(textToUpdate)
  }

  /** A query for a table. */
  private val q = TableQuery[TextsTable]

  /** A query for a single, not deleted text. */
  private def singleTextQuery(id: UUID) = q.where(_.id === id).filter(_.isSoftDeleted === false)

}
