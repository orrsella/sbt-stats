sbtPlugin := true

name := "sbt-stats"

organization := "com.orrsella"

version := "1.0.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

// publishing related
// crossScalaVersions := Seq("2.9.0", "2.9.1", "2.9.2", "2.9.3", "2.10.0", "2.10.1")

// scalaVersion := "2.10.1"

sbtVersion in Global := "0.13.0-RC3"

scalaVersion in Global := "2.10.2"

// scalacOptions ++= Seq("-feature")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/orrsella/sbt-stats</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:orrsella/sbt-stats.git</url>
    <connection>scm:git:git@github.com:orrsella/sbt-stats.git</connection>
  </scm>
  <developers>
    <developer>
      <id>orrsella</id>
      <name>Orr Sella</name>
      <url>http://orrsella.com</url>
    </developer>
  </developers>
)
