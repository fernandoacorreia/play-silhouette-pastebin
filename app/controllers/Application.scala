/**
 * Copyright 2014 Mohiva Organisation (license at mohiva dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.{ LoginInfo, Silhouette }
import com.mohiva.play.silhouette.core.providers.oauth2._
import com.mohiva.play.silhouette.core.services.{ AuthenticatorService, IdentityService }
import custom.AuthenticationService
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Auth-enabled application controllers.
 */
class Application @Inject() (
  val identityService: IdentityService[User],
  val authenticatorService: AuthenticatorService[CachedCookieAuthenticator],
  val facebookProvider: FacebookProvider, // TODO Here we should go with a ProviderService or with a map of providers.
  val authenticationService: AuthenticationService)
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
          authenticationService.signIn(profile) // TODO convert to an async call
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
