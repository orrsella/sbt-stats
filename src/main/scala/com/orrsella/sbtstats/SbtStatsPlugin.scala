/**
 * Copyright (c) 2013 Orr Sella
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orrsella.sbtstats

import java.io.File
import sbt._
import sbt.Keys._

object SbtStatsPlugin extends Plugin {

  private val configs = Seq(Compile, Test)

  lazy val stats = TaskKey[Unit]("stats", "Get code statistics for the project")
  lazy val statsSettings = configs map makeTask

  lazy val statsAnalyzers = SettingKey[Seq[Analyzer]]("stats-analyzers")
  lazy val statsAnalyzersSettings = statsAnalyzers := Seq(new FilesAnalyzer(), new LinesAnalyzer(), new CharsAnalyzer())

  override lazy val projectSettings = super.projectSettings ++ statsSettings ++ statsAnalyzersSettings

  private def makeTask(config: Configuration): Setting[Task[Unit]] = {
    stats in config <<= (statsAnalyzers, sources in config, packageBin in config, state, compile in config) map {
      (analyzers, sources, packageBin, state, compile) => statsImpl(analyzers, sources, packageBin, state.log)
    }
  }

  private def statsImpl(analyzers: Seq[Analyzer], sources: Seq[File], packageBin: File, log: Logger) {
    log.info("")
    log.info("Code Statistics:")
    log.info("")

    for (a <- analyzers) {
      log.info(a.analyze(sources, packageBin).toString)
      log.info("")
    }
  }
}