scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.evojam" %% "play-elastic4s" % "0.3.1"
)
routesGenerator := InjectedRoutesGenerator


fork in run := false