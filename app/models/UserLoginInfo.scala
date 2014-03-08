package models

import com.mohiva.play.silhouette.core.LoginInfo
import java.util.UUID

/**
 * User login info security model object.
 *
 * Links an authentication provider account to an application user.
 *
 * An application user can be linked to multiple authentication provider accounts.
 * An authentication provider account can be linked to a single application user.
 *
 * @param providerID The authentication provider's identification.
 * @param providerKey The user's key in the authentication provider.
 * @param userID The user's key in the application.
 */
case class UserLoginInfo(
  providerID: String,
  providerKey: String,
  userID: UUID)

/** Creates [[UserLoginInfo]] instances. */
object UserLoginInfoFactory {

  /**
   * Creates a [[UserLoginInfo]] from a [[LoginInfo]] and a [[User]].
   *
   * @param loginInfo The source [[[[LoginInfo]]]].
   * @param user The source [[[[User]]]].
   * @return A new [[UserLoginInfo]].
   */
  def fromLoginInfo(loginInfo: LoginInfo, user: User) = UserLoginInfo(
    providerID = loginInfo.providerID,
    providerKey = loginInfo.providerKey,
    userID = user.id)
}
