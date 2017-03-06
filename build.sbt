name := FitnessByYou.NamePrefix + "root"
version := "0.0.1"
scalaVersion := "2.11.8"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Workaround of https://github.com/sbt/sbt/issues/2054:
resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

resolvers  += "Online Play Repository" at
  "http://repo.typesafe.com/typesafe/simple/maven-releases/"

lazy val fitnessApi = (project in file("modules/fitness-api"))
	.settings(Common.settings: _*)
	.settings(libraryDependencies ++= Dependencies.elasticDependencies)
	.enablePlugins(PlayScala)

lazy val esLink = (project in file("modules/es-link"))
	.settings(Common.settings: _*)
	.settings(libraryDependencies ++= Dependencies.elasticDependencies)


libraryDependencies ++= Seq(
  "com.evojam" %% "play-elastic4s" % "0.3.1"
)
routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).
	aggregate(fitnessApi, esLink)

fork in run := false
