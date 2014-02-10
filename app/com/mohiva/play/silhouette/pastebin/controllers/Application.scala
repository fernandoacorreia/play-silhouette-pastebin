package com.mohiva.play.silhouette.pastebin.controllers

import com.mohiva.play.silhouette.core.Silhouette
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.pastebin.views
import javax.inject.Inject
import com.mohiva.play.silhouette.core.services.{AuthenticatorService, IdentityService}

class Application @Inject() (
  val identityService: IdentityService[User],
  val authenticatorService: AuthenticatorService[CachedCookieAuthenticator])
    extends Silhouette[User, CachedCookieAuthenticator] {

  def index = SecuredAction {
    Ok(views.html.index("Hello Play Framework"))
  }
}
