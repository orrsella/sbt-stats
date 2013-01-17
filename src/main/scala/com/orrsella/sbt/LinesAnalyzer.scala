package com.orrsella.sbt.sbtstats

import java.io.File
import scala.io.Source

class LinesAnalyzer extends Analyzer {
  def analyze(files: Seq[File]) = {
    val lines: Seq[Line] = for {
      file <- files
      line <- Source.fromFile(file).getLines
    } yield new Line(line)

    val totalLines = lines.length
    val codeLines = lines.filter(_.isCode).length
    val commentLines = lines.filter(_.isComment).length
    val bracketLines = lines.filter(_.isBracket).length
    val blankLines = lines.filter(_.isBlank).length
    val avgLength = lines.filter(_.isCode).map(_.length).reduceLeft(_ + _) / totalLines

    new LinesAnalyzerResult(totalLines, codeLines, commentLines, bracketLines, blankLines, avgLength)
  }

  class Line(l: String) {
    private val line = l.trim
    val length = l.length
    def isComment = line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")
    def isBracket = line == "{" || line == "}"
    def isBlank = line.length == 0
    def isCode = !isComment && !isBlank
  }
}

class LinesAnalyzerResult(
    totalLines: Int,
    codeLines: Int,
    commentLines: Int,
    bracketLines: Int,
    blankLines: Int,
    avgLength: Int)
  extends AnalyzerResult {

  val title = "Lines"
  val metrics =
    Seq(
      new Metric("Total:     ", totalLines, "lines"),
      new Metric("Code:      ", codeLines, codeLines.toDouble / totalLines, "lines"),
      new Metric("Comment:   ", commentLines, commentLines.toDouble / totalLines, "lines"),
      new Metric("Blank:     ", blankLines, blankLines.toDouble / totalLines, "lines"),
      new Metric("Bracket:   ", bracketLines, bracketLines.toDouble / totalLines, "lines"),
      new Metric("Avg length:", avgLength, "characters (code lines only)"))
}