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

/**
 * Texts domain model object.
 *
 * @param id This text's unique internal ID.
 * @param title This text's title.
 * @param contents The main contents of this text.
 * @param privacyLevel The privacy level of this text.
 * @param sourceID If this text was forked from another, the ID of the original text.
 * @param creatorID The ID of the user that created this text.
 * @param creationTimeUTC Date and time this text was created, in the UTC timezone.
 * @param updateTimeUTC Date and time this text was last updated or soft deleted, in the UTC timezone.
 * @param isSoftDeleted true if this text has been soft deleted, false otherwise.
 */
case class Text(
  id: UUID = UUID.randomUUID,
  title: String,
  contents: String,
  privacyLevel: Int, // TODO ENUM
  sourceID: Option[UUID] = None,
  creatorID: UUID,
  creationTimeUTC: DateTime = utcNow,
  updateTimeUTC: DateTime = utcNow,
  isSoftDeleted: Boolean = false)
