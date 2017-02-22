import sbt._
import Keys._

object Common {
  val appVersion = "0.1.0"
  val settings: Seq[Def.Setting[_]] = Seq(
    version := appVersion,
    scalaVersion := "2.11.7",
    resolvers += Opts.resolver.mavenLocalFile,
    resolvers ++= Seq(DefaultMavenRepository,
        "Typesafe Releases" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"
    ),
    resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)
  )
}