package com.orrsella.sbt.sbtstats

import java.io.File

class FileTypeAnalyzer extends Analyzer {
  def analyze(files: Seq[File]) = {
    val scalaFiles = files.filter(_.getName.endsWith("scala")).length
    val javaFiles = files.filter(_.getName.endsWith("java")).length
    val otherFiles = files.length - scalaFiles - javaFiles
    new FileTypeAnalyzerResult(files.length, scalaFiles, javaFiles, otherFiles)
  }
}

class FileTypeAnalyzerResult(totalFiles: Int, scalaFiles: Int, javaFiles: Int, otherFiles: Int) extends AnalyzerResult {
  val title = "File Types"
  val metrics =
    Seq(
      new Metric("Total:", totalFiles, "files"),
      new Metric("Scala:", scalaFiles, scalaFiles.toDouble / totalFiles, "files"),
      new Metric("Java: ", javaFiles, javaFiles.toDouble / totalFiles, "files"))
      // new Metric("Other", otherFiles, otherFiles.toDouble / totalFiles, "files"))
}