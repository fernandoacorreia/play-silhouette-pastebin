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

-- Application users.
CREATE TABLE USERS
(
  ID UUID PRIMARY KEY NOT NULL,
  FIRSTNAME VARCHAR NOT NULL,
  LASTNAME VARCHAR,
  FULLNAME VARCHAR,
  EMAIL VARCHAR,
  AVATAR VARCHAR,
  CREATION_TIME_UTC TIMESTAMP NOT NULL,
  UPDATE_TIME_UTC TIMESTAMP NOT NULL,
  IS_SOFT_DELETED BOOLEAN NOT NULL DEFAULT FALSE
);

-- Entries that link application users to authentication provider accounts.
-- An application user can be linked to multiple authentication provider accounts.
-- An authentication provider account can be linked to a single application user.
CREATE TABLE USER_LOGIN_INFO
(
  PROVIDER_ID VARCHAR NOT NULL,
  PROVIDER_KEY VARCHAR NOT NULL,
  USER_ID UUID NOT NULL,
  PRIMARY KEY (PROVIDER_ID, PROVIDER_KEY)
);
