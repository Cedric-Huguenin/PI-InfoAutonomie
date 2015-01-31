name := """PI-InfoAutonomie"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
