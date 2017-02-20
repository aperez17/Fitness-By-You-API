name := "Fitness-By-You-API"
version := "0.1.0"

lazy val commonSettings = Seq(
  organization := "com.fitnessbyyou",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val esSetup = (project in file("modules/es-setup")).enablePlugins(PlayScala)

lazy val root = (project in file("modules/main-api")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "com.evojam" %% "play-elastic4s" % "0.3.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Workaround of https://github.com/sbt/sbt/issues/2054:
resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

resolvers  += "Online Play Repository" at
  "http://repo.typesafe.com/typesafe/simple/maven-releases/"

routesGenerator := InjectedRoutesGenerator


fork in run := false