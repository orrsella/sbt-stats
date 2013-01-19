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

case class AnalyzerMetric(title: String, value: Double, percentage: Option[Double] = None, unit: Option[String] = None) {
  def this(title: String, value: Double, percentage: Double, unit: String) = this(title, value, Some(percentage), Some(unit))
  def this(title: String, value: Double, percentage: Double) = this(title, value, Some(percentage))
  def this(title: String, value: Double, unit: String) = this(title, value, None, Some(unit))

  override def toString = (percentage, unit) match {
    case (Some(p), Some(u)) => "%s %,.0f %s (%.1f%%)".format(title, value, u, p * 100)
    case (Some(p), None)    => "%s %,.0f (%.1f%%)".format(title, value, p * 100)
    case (None, Some(u))    => "%s %,.0f %s".format(title, value, u)
    case _                  => "%s %,.0f".format(title, value)
  }
}