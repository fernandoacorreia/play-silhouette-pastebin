/**
 * Test helper object.
 *
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

package utils

import play.api.db.slick.Config.driver.simple._
import scala.slick.jdbc.{ StaticQuery => Q }
import Q.interpolation

/** Provides helper function for tests. */
object TestHelper {

  /**
   * Deletes all records from the database, in the proper order to avoid referential integrity violations.
   *
   * @param s A database session.
   */
  def cleanDatabase(implicit s: Session) {
    sqlu"DELETE FROM TEXTS".first
    sqlu"DELETE FROM USERS".first
  }

}