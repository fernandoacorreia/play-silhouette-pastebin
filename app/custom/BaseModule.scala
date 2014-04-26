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

package custom

import play.api.Play
import play.api.Play.current
import com.google.inject.{ Provides, AbstractModule }
import com.mohiva.play.silhouette.contrib.services.{ CachedCookieAuthenticatorService, CachedCookieAuthenticatorSettings, CachedCookieAuthenticator }
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.contrib.utils.{ SecureRandomIDGenerator, PlayCacheLayer }
import com.mohiva.play.silhouette.core.services.{ AuthInfoService, AuthenticatorService, IdentityService }
import com.mohiva.play.silhouette.core.providers.OAuth2Settings
import com.mohiva.play.silhouette.core.providers.oauth2.FacebookProvider
import com.mohiva.play.silhouette.core.utils._
import daos._
import net.codingwell.scalaguice.ScalaModule
import play.api.Logger

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
   * Provides the authentication service.
   */
  @Provides
  def provideAuthenticationService(cacheLayer: CacheLayer, idGenerator: IDGenerator): AuthenticationService = {
    new AuthenticationService(provideAuthenticatorService(cacheLayer, idGenerator), new UserLoginInfoDAO, new UserDAO)
    // TODO refactor, make better use of Guice
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
   * @param cacheLayer The cache layer implementation.
   * @param httpLayer The HTTP layer implementation.
   * @return The Facebook provider.
   */
  @Provides
  def provideFacebookProvider(cacheLayer: CacheLayer, httpLayer: HTTPLayer): FacebookProvider = {
    new FacebookProvider(cacheLayer, httpLayer, OAuth2Settings(
      authorizationURL = Play.configuration.getString("silhouette.facebook.authorizationURL").get,
      accessTokenURL = Play.configuration.getString("silhouette.facebook.accessTokenURL").get,
      redirectURL = Play.configuration.getString("silhouette.facebook.redirectURL").get,
      clientID = Play.configuration.getString("silhouette.facebook.clientID").get,
      clientSecret = Play.configuration.getString("silhouette.facebook.clientSecret").get))
  }

}
