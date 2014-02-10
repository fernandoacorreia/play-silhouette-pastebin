package util.silhouette

import com.mohiva.play.silhouette.core.services.AuthInfoService
import com.mohiva.play.silhouette.core.LoginInfo
import scala.concurrent.Future
import com.mohiva.play.silhouette.core.providers.OAuth2Info

/**
 * Base implementation to show how Guice works.
 */
class AuthInfoServiceImpl extends AuthInfoService {

  /**
   * Saves auth info.
   *
   * This method gets called when a user logs in(social auth) or registers. This is the change
   * to persist the auth info for a provider in the backing store. If the application supports
   * the concept of "merged identities", i.e., the same user being able to authenticate through
   * different providers, then make sure that the auth info for every linked login info gets
   * stored separately.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info or None if the identity couldn't be saved.
   */
  def save[T](loginInfo: LoginInfo, authInfo: T): Future[Option[T]] = Future.successful(Some(authInfo))

  /**
   * Retrieves the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def retrieve[T](loginInfo: LoginInfo): Future[Option[T]] = Future.successful(Some(
    OAuth2Info("token").asInstanceOf[T]
  ))
}
