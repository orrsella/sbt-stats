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

import org.scalatest.FlatSpec

// val length = line.length
// def isComment = line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")
// def isBracket = line == "{" || line == "}"
// def isBlank = line.length == 0
// def isCode = !isComment && !isBlank

class LineSpec extends FlatSpec {
  "Line" should "correctly prase isCode" in {
    // code
    assert(new Line("val x = 5").isCode === true)
    assert(new Line("   def method(x: Int): Unit = println(x)").isCode === true)
    assert(new Line("   something").isCode === true)

    // bracket
    assert(new Line("   {   ").isCode === true)
    assert(new Line("}").isCode === true)
    assert(new Line("  } ").isCode === true)

    // blank
    assert(new Line("").isCode === false)
    assert(new Line("    ").isCode === false)

    // comments
    assert(new Line("//").isCode === false)
    assert(new Line("  /*").isCode === false)
    assert(new Line("  *").isCode === false)
  }

  it should "correctly parse isComment" in {
    // comments
    assert(new Line("//").isComment === true)
    assert(new Line("  //").isComment === true)
    assert(new Line("/* private def").isComment === true)
    assert(new Line("*").isComment === true)
    assert(new Line("*  val x").isComment === true)

    // code
    assert(new Line("val x = 5").isComment === false)
    assert(new Line("   def method(x: Int): Unit = println(x)").isComment === false)
    assert(new Line("   foo").isComment === false)

    // bracket
    assert(new Line("   {   ").isComment === false)
    assert(new Line("}").isComment === false)
    assert(new Line("  } ").isComment === false)

    // blank
    assert(new Line("").isComment === false)
  }

  it should "correctly parse isBracket" in {
    // bracket
    assert(new Line("   {   ").isBracket === true)
    assert(new Line("}").isBracket === true)
    assert(new Line("  } ").isBracket === true)
    assert(new Line("  {} ").isBracket === false)
    assert(new Line("  {  for() ").isBracket === false)

    // code
    assert(new Line("val x = 5").isBracket === false)
    assert(new Line("   def method(x: Int): Unit = println(x)").isBracket === false)
    assert(new Line("   foo").isBracket === false)

    // comment
    assert(new Line("//").isBracket === false)
    assert(new Line("  /*").isBracket === false)
    assert(new Line("  *").isBracket === false)

    // blank
    assert(new Line("").isBracket === false)
  }

  it should "correctly parse isBlank" in {
    // blank
    assert(new Line("").isBlank === true)
    assert(new Line(" ").isBlank === true)
    assert(new Line("  ").isBlank === true)
    assert(new Line("     ").isBlank === true)

    assert(new Line("d").isBlank === false)
    assert(new Line("def").isBlank === false)
    assert(new Line("   d   ").isBlank === false)
    assert(new Line("{").isBlank === false)
    assert(new Line("  //").isBlank === false)
    assert(new Line("/*  ").isBlank === false)
    assert(new Line("}").isBlank === false)
  }

  it should "correctly calculate length" in {
    assert(new Line("").length === 0)
    assert(new Line("d").length === 1)
    assert(new Line("dd").length === 2)
    assert(new Line("///").length === 3)
    assert(new Line("   def method(x: Int): Unit = println(x)").length === 37)
  }
}