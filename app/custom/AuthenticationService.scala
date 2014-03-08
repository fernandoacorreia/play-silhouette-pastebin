package custom

import com.mohiva.play.silhouette.core.providers.SocialProfile
import play.api.Logger

/**
 * Provides services related to authentication.
 */
class AuthenticationService {

  def signIn(profile: SocialProfile) = {
    // TODO save user, create session, etc.
    // After a user is authenticated you can check if the user already exists or if this is a new user.
    // You can check this with the help of the LoginInfo. If the user is a new user then create it in the database.
    // You must also save the LoginInfo for the user.
    // If the user already exists then you can update the profile with data returned from the provider.
    // To set the authenticator cookie you must first create an authenticator and then modify the response
    // with the help of the AuthenticatorService.send method.
    //
    // See:
    // CachedCookieAuthenticatorService
    // DelegableAuthInfoService
    // AuthInfoService
    // AuthenticatorService
    // TokenService
    //
    // From https://github.com/mohiva/play-silhouette/pull/38#issue-25868986:
    // If the user authenticates the first time, the social provider implementation stores the authentication info
    // for the linked LoginInfo into the backing store and returns the SocialProfile.
    // Now the controller should create a new Identity. Maybe only from the data provided by the social provider
    // or with additional data(IP-Address, UserAgent, Lang, ...).
    // If the user authenticates again at a later time, then the provider updates the authentication info
    // for the linked LoginInfo and returns also the SocialProfile.
    // Now the identity can be updated with the returned profile data and the LoginInfo can be stored
    // in the authenticator cookie.
    Logger.debug("AuthenticationService.signIn profile=" + profile)

  }

}
