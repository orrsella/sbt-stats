package com.orrsella.sbt.sbtstats

import java.io.File

class FileSizeAnalyzer extends Analyzer {
  def analyze(files: Seq[File]) = {
    val totalSize = files.map(_.length).reduceLeft(_ + _)
    val avgSize = totalSize / files.length
    new FileSizeAnalyzerResult(totalSize, avgSize)
  }
}

class FileSizeAnalyzerResult(totalSize: Long, averageSize: Long) extends AnalyzerResult {
  val title = "File Sizes"
  val metrics =
    Seq(
      new Metric("Total:  ", totalSize.toDouble / 1024, "KB"),
      new Metric("Average:", averageSize.toDouble / 1024, "KB"))
}