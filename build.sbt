sbtPlugin := true

name := "sbt-stats"

organization := "com.orrsella.sbt"

version := "1.0-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

crossScalaVersions := Seq("2.9.2", "2.10.0")