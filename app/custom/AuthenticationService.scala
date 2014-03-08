package custom

import com.mohiva.play.silhouette.core.providers.SocialProfile
import daos._
import models._
import play.api.db.slick._
import play.api.Logger
import play.api.Play.current

/**
 * Provides services related to authentication.
 */
class AuthenticationService(usersDAO: UsersDAO, userLoginInfoDAO: UserLoginInfoDAO) {

  def signIn(profile: SocialProfile) = {
    Logger.debug("[AuthenticationService.signIn] profile=" + profile)

    val loginInfo = profile.loginInfo

    // Create user if it doesn't exist.
    val exists = DB.withSession { implicit s: Session =>
      userLoginInfoDAO.exists(loginInfo)
    }
    if (!exists) {
      val user = UserFactory.fromSocialProfile(profile)
      DB.withTransaction { implicit s: Session =>
        Logger.debug("[AuthenticationService.signIn] inserting user=" + user)
        usersDAO.insert(user) // Create a new user in the database.
        val userLoginInfo = UserLoginInfoFactory.fromLoginInfo(loginInfo, user)
        userLoginInfoDAO.insert(userLoginInfo) // Link the new user to the authentication provider account.
      }
    }

    // See:
    // DelegableAuthInfoService
    // AuthInfoService

    // Note: Since this application supports authenticating a single user with multiple authentication provider accounts,
    // it keeps its own user profile. For this reason we won't try to update the user profile in subsequent sign ins.

    // TODO authenticator
    // To set the authenticator cookie you must first create an authenticator and then modify the response
    // with the help of the AuthenticatorService.send method.
    // See:
    // CachedCookieAuthenticatorService
    // AuthenticatorService
    // TokenService

  }

}
