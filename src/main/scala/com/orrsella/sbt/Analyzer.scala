package com.orrsella.sbt.sbtstats

import java.io.File
import scala.io.Source
import scala.math.round

abstract class Analyzer {
  def analyze(files: Seq[File]): AnalyzerResult
}

abstract class AnalyzerResult {
  def title: String
  def metrics: Seq[Metric]

  override def toString = title + "\n- " + metrics.mkString("\n- ") + "\n"
  private implicit def anyToString(x: Any) = x.toString

  // Scala 2.10 implicit class (instead of implicit def)
  // implicit class AnyExtender(x: Any) {
  //   def toStr = x.toString
  // }
}

case class Metric(title: String, value: Double, percentage: Option[Double] = None, unit: Option[String] = None) {
  def this(title: String, value: Double, percentage: Double, unit: String) = this(title, value, Some(percentage), Some(unit))
  def this(title: String, value: Double, percentage: Double) = this(title, value, Some(percentage))
  def this(title: String, value: Double, unit: String) = this(title, value, None, Some(unit))

  override def toString = (percentage, unit) match {
    // TODO: add thousands separator
    // case (Some(p), Some(u)) => "%s %.0f %s (%d%%)".format(title, value, u, round(p * 100))
    // case (Some(p), None)    => "%s %.0f (%d%%)".format(title, value, round(p * 100))
    case (Some(p), Some(u)) => "%s %.0f %s (%.1f%%)".format(title, value, u, p * 100)
    case (Some(p), None)    => "%s %.0f (%.1f%%)".format(title, value, p * 100)
    case (None, Some(u))    => "%s %.0f %s".format(title, value, u)
    case _                  => "%s %.0f".format(title, value)

    // Scala 2.10 string interpolation:
    // case (Some(p), Some(u)) => f"$title: $value%.0f $u (${p * 100}%.1f%)"
    // case (Some(p), None)    => f"$title: $value%.0f (${p * 100}%.1f%)"
    // case (None, Some(u))    => f"$title: $value%.0f $u"
    // case _                  => f"$title: $value%.0f"
  }
}