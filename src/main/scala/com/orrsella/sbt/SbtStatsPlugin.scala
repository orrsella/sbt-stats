package com.orrsella.sbt.sbtstats

import java.io.File
import sbt._
import sbt.Keys._

object SbtStatsPlugin extends Plugin {

  private val configs = Seq(Compile, Test)

  lazy val stats = TaskKey[Unit]("stats", "Get code statistics for the project")
  lazy val statsSettings = configs map makeTask

  lazy val statsAnalyzers = SettingKey[Seq[Analyzer]]("stats-analyzers")
  lazy val statsAnalyzersSettings = statsAnalyzers := Seq(new FilesAnalyzer(), new LinesAnalyzer())

  override lazy val projectSettings = super.projectSettings ++ statsSettings ++ statsAnalyzersSettings

  private def makeTask(config: Configuration): Setting[Task[Unit]] = {
    stats in config <<= (statsAnalyzers, sources in config, state, compile in config) map {
      (analyzers, sources, state, compile) => statsImpl(analyzers, sources, state.log)
    }
  }

  private def statsImpl(analyzers: Seq[Analyzer], sources: Seq[File], log: Logger) {
    log.info("Code Statistics:")
    log.info("")

    for (a <- analyzers) {
      log.info(a.analyze(sources).toString)
      log.info("")
    }
  }
}