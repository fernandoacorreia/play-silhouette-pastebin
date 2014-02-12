import play.Project._

name := "play-silhouette-pastebin"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc, // Database access.
  "com.github.tototoshi" %% "play-flyway" % "1.0.1", // Database migration.
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.0", // Database access.
  "com.typesafe.play" %% "play-slick" % "0.6.0.1", // Database access.
  "org.webjars" %% "webjars-play" % "2.2.0", // Support for web libraries.
  "org.webjars" % "bootstrap" % "2.3.1", // CSS Framework.
  "com.google.inject" % "guice" % "4.0-beta", // Dependency injection.
  "net.codingwell" %% "scala-guice" % "4.0.0-beta", // Dependency injection.
  "com.mohiva" %% "play-silhouette" % "1.0-SNAPSHOT" // Authentication and authorization.
)

playScalaSettings
