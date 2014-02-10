import play.Project._

name := "play-silhouette-pastebin"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc,
  "com.github.tototoshi" %% "play-flyway" % "1.0.1",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.0",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "com.google.inject" % "guice" % "4.0-beta",
  "net.codingwell" %% "scala-guice" % "4.0.0-beta",
  "com.mohiva" %% "play-silhouette" % "1.0-SNAPSHOT"
)

playScalaSettings
