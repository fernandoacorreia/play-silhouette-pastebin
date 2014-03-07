package custom

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.OAuth2Info
import com.mohiva.play.silhouette.core.services.{ AuthInfoService, AuthInfo }
import play.api.Logger
import scala.concurrent.Future
import scala.reflect.ClassTag

/**
 * Base implementation to show how Guice works.
 */
class AuthInfoServiceImpl extends AuthInfoService {

  /**
   * Saves auth info.
   *
   * This method gets called when a user logs in (social auth) or registers. This is the chance
   * to persist the auth info for a provider in the backing store. If the application supports
   * the concept of "merged identities", i.e., the same user being able to authenticate through
   * different providers, then make sure that the auth info for every linked login info gets
   * stored separately.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = {
    Logger.debug("TODO: save loginInfo " + loginInfo)
    Future.successful(authInfo)
  }

  /**
   * Retrieves the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @param tag The class tag of the auth info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def retrieve[T <: AuthInfo](loginInfo: LoginInfo)(implicit tag: ClassTag[T]): Future[Option[T]] = {
    Logger.debug("TODO: retrieve loginInfo")
    Future.successful(Some(OAuth2Info("token").asInstanceOf[T]))
  }
}
