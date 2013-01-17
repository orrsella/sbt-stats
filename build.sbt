sbtPlugin := true

name := "sbt-stats"

organization := "com.orrsella.sbt"

version := "1.0-SNAPSHOT"

// scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  // "net.databinder" %% "dispatch-http" % "0.8.9",
  // "net.databinder" %% "dispatch-oauth" % "0.8.9",
  // "net.databinder" %% "dispatch-mime" % "0.8.9",
  // "org.json4s" %% "json4s-native" % "3.0.0",
  // "org.scala-sbt" %% "scripted-plugin" % "1.0"
)

// libraryDependencies <+= sbtVersion("org.scala-sbt" % "scripted-plugin" % _)
