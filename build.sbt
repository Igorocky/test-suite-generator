val projVersion = "0.1-SNAPSHOT"
val scalaV = "2.12.2"
lazy val scalaTestVersion = "3.0.1"

lazy val testSuiteGenerator = (project in file(".")).settings(
  name := "test-suite-generator",
  version := projVersion,
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
    ,"org.scalatest" %% "scalatest" % scalaTestVersion % Test
    ,"org.scalacheck" %% "scalacheck" % "1.12.6" % Test
  ),
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "ch.qos.logback" % "logback-core" % "1.2.3"
  )
)