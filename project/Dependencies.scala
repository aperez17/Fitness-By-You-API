import sbt._
import Keys._

object Dependencies {
  val playElastic = "com.evojam" %% "play-elastic4s" % "0.3.1"
  val guice = "com.google.inject" % "guice" % "3.0"
  val elasticDependencies: Seq[ModuleID] = Seq(
    playElastic,
    guice
  )
}