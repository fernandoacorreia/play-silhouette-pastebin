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

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.User
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.IdentityService
import daos._
import play.api.db.slick._ // TODO avoid depending directly on Slick
import play.api.Logger
import play.api.Play.current
import scala.concurrent.Future

/**
 * Base implementation to show how Guice works.
 */
class IdentityServiceImpl @Inject() (userLoginInfoDAO: UserLoginInfoDAO, userDAO: UserDAO) extends IdentityService[User] {

  /**
   * Retrieves an identity that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve an identity.
   * @return The retrieved identity or None if no identity could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    Logger.debug(s"[IdentityServiceImpl.retrieve] loginInfo=$loginInfo")
    DB.withSession { implicit s: Session =>
      Future.successful(
        userLoginInfoDAO.retrieve(loginInfo).flatMap { userLoginInfo =>
          Logger.debug(s"[IdentityServiceImpl.retrieve] userLoginInfo=$userLoginInfo")
          userDAO.retrieve(userLoginInfo.userID).map { user =>
            Logger.debug(s"[IdentityServiceImpl.retrieve] user=$user")
            User(
              loginInfo = loginInfo,
              firstName = user.firstName.getOrElse(""),
              lastName = user.lastName.getOrElse(""),
              fullName = user.fullName.getOrElse(""),
              email = user.email,
              avatarURL = user.avatarURL)
          }
        })
    }
  }
}
