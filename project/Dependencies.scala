import sbt._

object Dependencies {
  val playElastic = "com.evojam" %% "play-elastic4s" % "0.3.1"
  val guice = "com.google.inject" % "guice" % "3.0"
  val elasticsearch = "org.elasticsearch" % "elasticsearch" % "2.4.4"
  val elasticDependencies: Seq[ModuleID] = Seq(
    playElastic,
    guice,
    elasticsearch
  )
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
}
