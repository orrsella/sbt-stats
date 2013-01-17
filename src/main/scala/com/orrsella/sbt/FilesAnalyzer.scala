package com.orrsella.sbt.sbtstats

import java.io.File
import scala.io.Source

class FilesAnalyzer extends Analyzer {
  def analyze(files: Seq[File]) = {
    val scalaFiles = files.filter(_.getName.endsWith("scala")).length
    val javaFiles = files.filter(_.getName.endsWith("java")).length
    val totalSize = files.map(_.length).reduceLeft(_ + _)
    val avgSize = totalSize / files.length

    // val lines: Seq[Int] = for {
    //   file <- files
    // } yield Source.fromFile(file).getLines.length

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
      new Metric("Total:     ", totalFiles, "files"),
      new Metric("Scala:     ", scalaFiles, scalaFiles.toDouble / totalFiles, "files"),
      new Metric("Java:      ", javaFiles, javaFiles.toDouble / totalFiles, "files"),
      new Metric("Total size:", totalSize.toDouble / 1024, "KB"),
      new Metric("Avg size:  ", avgSize.toDouble / 1024, "KB"),
      new Metric("Avg length:", avgLines, "lines"))
}