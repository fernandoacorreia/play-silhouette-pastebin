package custom

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{ Authenticator, LoginInfo, Identity }
import com.mohiva.play.silhouette.core.providers.SocialProfile
import com.mohiva.play.silhouette.core.services.AuthenticatorService
import daos._
import models._
import play.api.db.slick._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger
import play.api.Play.current
import scala.concurrent.Future
import com.mohiva.play.silhouette.core.providers.OAuth2Info

/**
 * Provides services related to authentication.
 */
class AuthenticationService(
    authenticatorService: AuthenticatorService[CachedCookieAuthenticator],
    userLoginInfoDAO: UserLoginInfoDAO,
    usersDAO: UserDAO) {

  def signIn(profile: SocialProfile[OAuth2Info]): Future[CachedCookieAuthenticator] = {
    Logger.debug("[AuthenticationService.signIn] profile=" + profile)
    val loginInfo = profile.loginInfo

    // Create user if it doesn't exist.
    // TODO convert to async
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

    // Note: Since this application supports authenticating a single user using multiple authentication provider accounts,
    // it keeps its own user profile. For this reason we won't try to update the user profile in subsequent sign ins.

    // Create authenticator.
    val identity = ApplicationIdentity(loginInfo)
    authenticatorService.create(identity).map { a =>
      a match {
        case Some(authenticator) => {
          Logger.debug("[AuthenticationService.signIn] created authenticator=" + authenticator)
          authenticator
        }
        case None => throw new RuntimeException("Could not create an authenticator.")
      }
    }

  }

}

/** An identity representing an application user. */
case class ApplicationIdentity(
  loginInfo: LoginInfo,
  user: Option[User] = None) extends Identity
