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
import scala.io.Source

class FilesAnalyzer extends Analyzer {
  def analyze(files: Seq[File]) = {
    val scalaFiles = files.filter(_.getName.endsWith("scala")).length
    val javaFiles = files.filter(_.getName.endsWith("java")).length
    val totalSize = files.map(_.length).reduceLeft(_ + _)
    val avgSize = totalSize / files.length
    val avgLines = files.map(Source.fromFile(_).getLines.length).reduceLeft(_ + _) / files.length

    new FilesAnalyzerResult(files.length, scalaFiles, javaFiles, totalSize, avgSize, avgLines)
  }
}

class FilesAnalyzerResult(
    totalFiles: Int,
    scalaFiles: Int,
    javaFiles: Int,
    totalSize: Long,
    avgSize: Long,
    avgLines: Int)
  extends AnalyzerResult {

  val title = "Files"
  val metrics =
    Seq(
      new AnalyzerMetric("Total:     ", totalFiles, "files"),
      new AnalyzerMetric("Scala:     ", scalaFiles, scalaFiles.toDouble / totalFiles, "files"),
      new AnalyzerMetric("Java:      ", javaFiles, javaFiles.toDouble / totalFiles, "files"),
      new AnalyzerMetric("Total size:", totalSize.toDouble / 1024, "KB"),
      new AnalyzerMetric("Avg size:  ", avgSize.toDouble / 1024, "KB"),
      new AnalyzerMetric("Avg length:", avgLines, "lines"))
}