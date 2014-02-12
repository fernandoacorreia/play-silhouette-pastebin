package com.mohiva.play.silhouette.custom

import play.api.Play
import play.api.Play.current
import com.google.inject.{Provides, AbstractModule}
import net.codingwell.scalaguice.ScalaModule
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.utils._
import com.mohiva.play.silhouette.core.services.{AuthInfoService, AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.core.providers.OAuth2Settings
import com.mohiva.play.silhouette.core.providers.oauth2.FacebookProvider
import com.mohiva.play.silhouette.contrib.utils.{SecureRandomIDGenerator, PlayCacheLayer}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticatorService
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticatorSettings
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator

/**
 * The base module is used to wire common dependencies.
 */
class BaseModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  def configure() {
    bind[IdentityService[User]].to[IdentityServiceImpl]
    bind[AuthInfoService].to[AuthInfoServiceImpl]
    bind[CacheLayer].to[PlayCacheLayer]
    bind[HTTPLayer].to[PlayHTTPLayer]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
  }

  /**
   * Provides the authenticator service.
   *
   * @param cacheLayer The cache layer implementation.
   * @param idGenerator The ID generator used to create the authenticator ID.
   * @return The authenticator service.
   */
  @Provides
  def provideAuthenticatorService(cacheLayer: CacheLayer, idGenerator: IDGenerator): AuthenticatorService[CachedCookieAuthenticator] = {
    val settings = CachedCookieAuthenticatorSettings()
    new CachedCookieAuthenticatorService(settings, cacheLayer, idGenerator, Clock())
  }

  /**
   * Provides the Facebook provider.
   *
   * @param authInfoService The auth info service.
   * @param cacheLayer The cache layer implementation.
   * @param httpLayer The HTTP layer implementation.
   * @return The Facebook provider.
   */
  @Provides
  def provideFacebookProvider(authInfoService: AuthInfoService, cacheLayer: CacheLayer, httpLayer: HTTPLayer): FacebookProvider = {
    new FacebookProvider(authInfoService, cacheLayer, httpLayer, OAuth2Settings(
      authorizationURL = Play.configuration.getString("silhouette.facebook.authorizationURL").get,
      accessTokenURL = Play.configuration.getString("silhouette.facebook.accessTokenURL").get,
      redirectURL = Play.configuration.getString("silhouette.facebook.redirectURL").get,
      clientID = Play.configuration.getString("silhouette.facebook.clientID").get,
      clientSecret = Play.configuration.getString("silhouette.facebook.clientSecret").get
    ))
  }
}
