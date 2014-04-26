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

package models

import java.util.UUID
import org.joda.time._
import utils.DateTimeHelper._
import com.mohiva.play.silhouette.core.providers.SocialProfile
import com.mohiva.play.silhouette.core.providers.OAuth2Info

/**
 * Users security model object.
 *
 * @param id This user's unique internal ID.
 * @param username This user's unique external identifier.
 * @param creationTimeUTC Date and time this user was created, in the UTC timezone.
 * @param updateTimeUTC Date and time this user was last updated or soft deleted, in the UTC timezone.
 * @param isSoftDeleted true if this user has been soft deleted, false otherwise.
 */
case class User(
  id: UUID = UUID.randomUUID,
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  fullName: Option[String] = None,
  email: Option[String] = None,
  avatarURL: Option[String] = None,
  creationTimeUTC: DateTime = utcNow,
  updateTimeUTC: DateTime = utcNow,
  isSoftDeleted: Boolean = false)

/** Creates [[User]] instances. */
object UserFactory {

  /**
   * Creates a User from a [[SocialProfile]].
   *
   * @param profile The source social profile.
   * @return A new [[User]].
   */
  def fromSocialProfile(profile: SocialProfile[OAuth2Info]) = User(
    firstName = profile.firstName,
    lastName = profile.lastName,
    fullName = profile.fullName,
    email = profile.email,
    avatarURL = profile.avatarURL)

}
