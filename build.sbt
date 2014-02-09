import play.Project._

name := "play-silhouette-pastebin"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  "com.github.tototoshi" %% "play-flyway" % "1.0.1",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1"
)

playScalaSettings
