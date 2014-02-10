/**
 * Creates database tables for storing security data.
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

-- TODO users, login info, profiles, etc.

CREATE TABLE USERS
(
  ID UUID PRIMARY KEY NOT NULL,
  USERNAME VARCHAR NOT NULL,
  CREATION_TIME_UTC TIMESTAMP NOT NULL,
  UPDATE_TIME_UTC TIMESTAMP NOT NULL,
  IS_SOFT_DELETED BOOLEAN NOT NULL DEFAULT FALSE
);
