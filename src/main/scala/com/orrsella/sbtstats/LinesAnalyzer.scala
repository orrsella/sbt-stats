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

class LinesAnalyzer extends Analyzer {
  def analyze(sources: Seq[File], packageBin: File) = {
    val lines: Seq[Line] = for {
      file <- sources
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
}

class Line(l: String) {
  private val line = l.trim
  val length = line.length
  val isComment = line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")
  val isBracket = line == "{" || line == "}"
  val isBlank = line.length == 0
  val isCode = !isComment && !isBlank
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
      new AnalyzerMetric("Total:     ", totalLines, "lines"),
      new AnalyzerMetric("Code:      ", codeLines, codeLines.toDouble / totalLines, "lines"),
      new AnalyzerMetric("Comment:   ", commentLines, commentLines.toDouble / totalLines, "lines"),
      new AnalyzerMetric("Blank:     ", blankLines, blankLines.toDouble / totalLines, "lines"),
      new AnalyzerMetric("Bracket:   ", bracketLines, bracketLines.toDouble / totalLines, "lines"),
      new AnalyzerMetric("Avg length:", avgLength, "characters (code lines only, not inc spaces)"))
}