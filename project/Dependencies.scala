import sbt._
import Keys._

object Dependencies {
  val playElastic = "com.evojam" %% "play-elastic4s" % "0.3.1"
  val elasticDependencies: Seq[ModuleID] = Seq(
    playElastic
  )
}