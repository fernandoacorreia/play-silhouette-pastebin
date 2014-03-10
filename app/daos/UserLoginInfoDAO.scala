package daos

import com.github.tototoshi.slick.H2JodaSupport._
import com.mohiva.play.silhouette.core.LoginInfo
import java.util.UUID
import models.UserLoginInfo
import org.joda.time._
import play.api.db.slick.Config.driver.simple._
import utils.DateTimeHelper._

/** Slick table mapping. */
class UserLoginInfoTable(tag: Tag) extends Table[UserLoginInfo](tag, "USER_LOGIN_INFO") {
  def providerID = column[String]("PROVIDER_ID", O.NotNull)
  def providerKey = column[String]("PROVIDER_KEY", O.NotNull)
  def userID = column[UUID]("USER_ID", O.NotNull)
  def * = (providerID, providerKey, userID) <>
    (UserLoginInfo.tupled, UserLoginInfo.unapply)
}

/** Data access object for [[UserLoginInfo]]. */
class UserLoginInfoDAO {

  /**
   * Deletes a user login info.
   *
   * This unlinks the user from the authentication provider.
   *
   * @param loginInfo The login info to be deleted.
   * @param s A database session.
   */
  def delete(loginInfo: LoginInfo)(implicit s: Session) = {
    singleUserLoginInfoQuery(loginInfo).delete
  }

  /**
   * Checks whether a user login info exists.
   *
   * This determines whether a user is linked to an authentication provider account.
   *
   * @param loginInfo The login info to be checked.
   * @param s A database session.
   * @return true if the user login info exists, false if it doesn't.
   */
  def exists(loginInfo: LoginInfo)(implicit s: Session): Boolean =
    !retrieve(loginInfo).isEmpty

  /**
   * Inserts a user login info into the database.
   *
   * @param userLoginInfo The user login info to be inserted.
   * @param s A database session.
   */
  def insert(userLoginInfo: UserLoginInfo)(implicit s: Session) = {
    q.insert(userLoginInfo)
  }

  /**
   * Retrieves a user login info by ID.
   *
   * @param loginInfo The login info to be retrieved.
   * @param s A database session.
   * @returns Maybe a user login info.
   */
  def retrieve(loginInfo: LoginInfo)(implicit s: Session): Option[UserLoginInfo] = {
    singleUserLoginInfoQuery(loginInfo).firstOption
  }

  /** A query for a table. */
  private val q = TableQuery[UserLoginInfoTable]

  /** A query for a single, not deleted user. */
  private def singleUserLoginInfoQuery(loginInfo: LoginInfo) = q
    .where(_.providerID === loginInfo.providerID)
    .where(_.providerKey === loginInfo.providerKey)
}
