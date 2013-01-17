package com.orrsella.sbt.sbtstats

import java.io.File

object Main {
  def main(args: Array[String]) {
    val file = new File("/Users/orr/Development/Scala/Projects/tumblr4s/src/main/scala/tumblr4s/TumblrApi.scala")
    val files = Seq(file)
    println(new FilesAnalyzer().analyze(files))
    println(new LinesAnalyzer().analyze(files))
    // println(new ScalaFileHandler().handle(file))

    // val files: Map[FileResultType, Seq[FileResult]] = Map()


  }

  // def iterFiles(files: Map[FileResultType, Seq[FileResult]]) {
  //   if (!files.isEmpty) {
  //     val (fileResultType, fileResults) = files.head

  //     fileResultType match {
  //       case RegularFileResult
  //     }
  //   }
  // }

  // def printFileResults(resultsSeq: Seq[Seq[FileResult]]) {
  //   if (!resultsSeq.isEmpty) {
  //     val results = resultsSeq.head
  //     results.head match {
  //       case FileQuantityResult => printFileQuantityResult(fileResults)
  //       case FileResult => printFileResult(fileResults)
  //     }
  //   }
  // }

  // def printFileQuantityResult(results: Seq[FileQuantityResult]) {
  //   val sum = results.reduceLeft(_ + _)
  //   val avg = sum / results.length
  //   println("")
  // }

  // def printFileResult(results: Seq[File])
}