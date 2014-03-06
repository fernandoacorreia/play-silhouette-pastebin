package controllers

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.services.{ AuthenticatorService, IdentityService }
import com.mohiva.play.silhouette.core.{ LoginInfo, Silhouette }
import javax.inject.Inject

class Application @Inject() (
  val identityService: IdentityService[User],
  val authenticatorService: AuthenticatorService[CachedCookieAuthenticator])
    extends Silhouette[User, CachedCookieAuthenticator] {

  def about = UserAwareAction { implicit request =>
    implicit val user = request.identity // TODO refactor duplicate code
    Ok(views.html.about())
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
