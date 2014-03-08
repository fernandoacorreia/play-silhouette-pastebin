import play.Project._

name := "play-silhouette-pastebin"

version := "1.0-SNAPSHOT"

// Search in Sonatype Snapshots repository.
resolvers += "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

// Search in local ivy repository.
resolvers += Resolver.file("Local repo", file(System.getProperty("user.home") + "/.ivy2/local"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  jdbc, // Database access.
  "com.github.tototoshi" %% "play-flyway" % "1.0.1", // Database migration.
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.0", // Data mapping.
  "com.typesafe.play" %% "play-slick" % "0.6.0.1", // Database access.
  "org.webjars" %% "webjars-play" % "2.2.1-2", // Support for web libraries.
  "org.webjars" % "bootstrap" % "3.1.1", // CSS framework.
  "org.webjars" % "jquery" % "1.11.0", // JavaScript framework.
  "com.google.inject" % "guice" % "4.0-beta", // Dependency injection.
  "net.codingwell" %% "scala-guice" % "4.0.0-beta", // Dependency injection.
  "com.mohiva" %% "play-silhouette" % "1.0-SNAPSHOT" // Authentication and authorization.
)

playScalaSettings

// Configure Auto Refresh plugin.
com.jamesward.play.BrowserNotifierPlugin.livereload
