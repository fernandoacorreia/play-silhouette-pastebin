package custom

import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.IdentityService
import scala.concurrent.Future

/**
 * Base implementation to show how Guice works.
 */
class IdentityServiceImpl extends IdentityService[User] {

  /**
   * Retrieves an identity that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve an identity.
   * @return The retrieved identity or None if no identity could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    Future.successful(Some(User(
      loginInfo = LoginInfo("facebook", "12345"),
      firstName = "Christian",
      lastName = "Kaps",
      fullName = "Christian Kaps",
      email = None,
      avatarURL = None)))
  }
}
