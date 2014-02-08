Silhouette Pastebin
===================

**A sample application for Silhouette.**

Silhouette Pastebin is a basic example of how to use [Silhouette](http://silhouette.mohiva.com/) for authentication and authorization in a Scala [Play Framework](http://www.playframework.com/) application.

It implements a simple web application for saving and sharing texts.


### Introduction

This application is written in [Scala](http://www.scala-lang.org/) and is intended as an introductory learning aid for Silhouette.

It exemplifies one way in which Silhouette can be integrated into a Play Framework application.

In particular, this sample demonstrates:

* How to include Silhouette in a Play Framework application.
* How to implement authentication integrated with external services such as Google, Facebook, Twitter and others.
* How to implement proprietary authentication based on credentials (username and password).
* How to implement authorization for web pages.
* How to implement authorization for a RESTful JSON API.
* How to...

Due to Silhouette's loosely coupled and flexible design, this is not by any measure an exhaustive list of how Silhouette can be leveraged.

Particularly, it's out of the scope of this sample:

* Custom cache layers.
* Custom credentials service.
* Custom ...
* ...


### Instructions

TODO: how to run locally, how to run tests, how to deploy (e.g. Heroku), how to work in Activator, etc.


### Use cases

#### List latest texts

A user can see a list of most recently created or edited texts which they are allowed to view.

#### View text

A user can see the contents of a text which they are allowed to view.

#### Create text

A user can create a new text, specifying its title, contents and privacy level.

#### Edit text

A user can edit a text they are allowed to.

#### Fork text

A user can copy a text created by another user. This copy will be considered as a text created by the user which forked it.

#### Delete text

A user can delete a text they are allowed to.

#### Change text privacy level

A user can modify the privacy level of a text they are allowed to.

Privacy levels are:

* Public: can be viewed by anonymous users.
* Protected: can be viewed by authenticated users.
* Private: can be viewed only by its creator.

See the authorization features section below for more details on permissions.


### Authentication features

#### Sign in with external services

A user can sign in through an external service which has been integrated into the application.

If it's the first time this user is authenticating into the application, a new user account will be created.

If there's already a user account which was created by authenticating with another service, the new login info will be attached to this existing user account.

#### Sign up

A user can sign up to the application to create a password which is specific to the application.

A sign up request will have to be confirmed through a token sent by email.

#### Confirm sign up

A user can click on the link with the sign up confirmation token which was sent by email.

If the token is valid, the user will be able to enter their username and password and their user account will be created.

If there is already a user account for that user (created by signing in with an external service), the new login info will be attached to this existing user account.

#### Sign in with application credentials

A user which has signed up and created an account with a password can sign in by providing their credentials.

#### Reset password

A user which has forgotten their application credentials can ask for a password reset.

A token will be sent by email. After clicking on the link containing the token, the user will be able to change their password.

#### Change password

An authenticated user, or a user which has clicked on a link with a password reset token, will be able to change the password of their application credential.

This only applies to user accounts which have signed up and created an application credential.

#### Sign out

A user can sign out from the application, regardless of whether they signed in with an external service or with application credentials.

Their session will be deactivated and they will be considered an anonymous user until they sign in again.


### Authorization features

#### Anonymous access

Users which are not authenticated will be allowed to view public texts, but not protected and private ones.

They will not be allowed to create new texts, edit or delete any texts.

They will also be allowed to view other users' profiles.

#### Authenticated user access

Authenticated users will be allowed to view public and protected texts, and their own private texts, but not private texts created by other users.

They will also be allowed to create new texts, fork any text they can view, and edit and delete the texts they created.

They will not be allowed to edit and delete texts created by other users.

They will also be allowed to view other users' profiles.

#### Private access

Authenticated users will be allowed to view, edit and delete private texts that they created, but not private texts created by other users.

They will also be allowed to change the privacy level of the texts they created, but not of texts created by other users.

A user will also be allowed to update their own profile, but not other users' profiles.

#### Administrative access

Application administrators will be allowed to view, delete and change the privacy level of any text created by any user.

They will not be allowed to edit texts created by other users.


### Architecture

TODO the application is organized in this way...

TODO these tools and libraries are used...


### Copyright and License

Copyright 2014 Mohiva Organisation (license at mohiva dot com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
