name := FitnessByYou.NamePrefix + "root"
version := "0.0.1"
scalaVersion := "2.11.8"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Workaround of https://github.com/sbt/sbt/issues/2054:
resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)
resolvers += Resolver.jcenterRepo
resolvers  += "Online Play Repository" at
  "http://repo.typesafe.com/typesafe/simple/maven-releases/"

libraryDependencies ++= Seq(
	"com.evojam" %% "play-elastic4s" % "0.3.1",
	"org.reactivemongo" %% "play2-reactivemongo" % "0.12.1",
	"com.mohiva" %% "play-silhouette-persistence-reactivemongo" % "4.0.1",
	"org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1",
	"com.mohiva" %% "play-silhouette" % "5.0.0",
	"com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
	"com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
	"com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0",
	"org.webjars" %% "webjars-play" % "2.6.1",
	"org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
	"org.webjars" % "jquery" % "3.2.1",
	"net.codingwell" %% "scala-guice" % "4.1.0",
	"com.iheart" %% "ficus" % "1.4.1",
	"com.typesafe.play" %% "play-mailer" % "6.0.1",
	"com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
	"com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
	"com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3",
	"com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % "test",
	specs2 % Test,
	filters
)

lazy val common = (project in file("modules/common"))
  .settings(Common.settings: _*)

lazy val fitnessApi = (project in file("modules/fitness-api"))
	.settings(Common.settings: _*)
	.settings(libraryDependencies ++= Dependencies.elasticDependencies)
	.enablePlugins(PlayScala)
  .dependsOn(common)

lazy val executableApplications = (project in file("modules/executable-applications"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.elasticDependencies)
  .dependsOn(common, fitnessApi)

lazy val esLink = (project in file("modules/es-link"))
	.settings(Common.settings: _*)
	.settings(libraryDependencies ++= Dependencies.elasticDependencies)
  .dependsOn(common, fitnessApi)

lazy val mongoClient = (project in file("modules/mongo-client"))
	  .settings(libraryDependencies ++= Seq("org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1"))

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).
	aggregate(fitnessApi, esLink)

fork in run := false
