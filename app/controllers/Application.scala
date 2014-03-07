package controllers

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.{ LoginInfo, Silhouette }
import com.mohiva.play.silhouette.core.providers.oauth2._
import com.mohiva.play.silhouette.core.services.{ AuthenticatorService, IdentityService }
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits._
import play.Logger

class Application @Inject() (
  val identityService: IdentityService[User],
  val authenticatorService: AuthenticatorService[CachedCookieAuthenticator],
  val facebookProvider: FacebookProvider) // TODO Here we should go with a ProviderService or with a map of providers.
    extends Silhouette[User, CachedCookieAuthenticator] {

  def about = UserAwareAction { implicit request =>
    implicit val user = request.identity // TODO refactor duplicate code
    Ok(views.html.about())
  }

  def account = UserAwareAction { implicit request =>
    implicit val user = request.identity // TODO refactor duplicate code
    Ok(views.html.account())
  }

  def authenticate(provider: String) = UserAwareAction.async { implicit request =>
    val p = provider match {
      case "facebook" => facebookProvider // TODO refactor to avoid having to change this code to add providers
      case _ => throw new Exception("Unknown provider: " + provider)
    }
    p.authenticate().map { authResult =>
      authResult match {
        case Left(result) => result
        case Right(profile) => {
          Logger.debug("Authenticated user: " + profile)
          // TODO save user, create session, etc.
          // After a user is authenticated you can check if the user already exists or if this is a new user.
          // You can check this with the help of the LoginInfo. If the user is a new user then create it in the database.
          // You must also save the LoginInfo for the user.
          // If the user already exists then you can update the profile with data returned from the provider.
          // To set the authenticator cookie you must first create an authenticator and then modify the response
          // with the help of the AuthenticatorService.send method.
          Redirect(routes.Application.account)
        }
      }
    }
  }

  def index = UserAwareAction { implicit request =>
    implicit val user = request.identity
    Ok(views.html.index())
  }

  def signIn = UserAwareAction { implicit request =>
    implicit val user = request.identity
    // TODO redirect to target page if already signed in
    Ok(views.html.signIn())
  }
}
