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

  def index = UserAwareAction { implicit request =>
    Ok(views.html.index(request.identity))
  }
}
