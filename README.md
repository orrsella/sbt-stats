# sbt-stats

An [sbt](http://www.scala-sbt.org/) (Simple Build Tool) plugin that easily provides source code statistics and analytics in the sbt console. It's purpose is to provide a Bird's-eye View of your project in terms of raw numbers and percentages. Can provide statistics for an entire multi-project build, or on a per-project basis. See [this post](http://orrsella.com/2013/01/20/introducing-sbt-stats-an-sbt-plugin-for-source-code-statistics/) for some more background.

## Add Plugin

To add sbt-stats functionality to your project add the following to your `project/plugins.sbt` file:

```scala
addSbtPlugin("com.orrsella" % "sbt-sublime" % "1.0.7")
```

If you want to use it for more than one project, you can add it to your global plugins file, usually found at: `~/.sbt/plugins/plugins.sbt` and then have it available for all sbt projects. See [Using Plugins](http://www.scala-sbt.org/release/docs/Getting-Started/Using-Plugins.html) for additional information on sbt plugins.

### Troubleshooting

If you added the plugin globally but still don't have the `stats` command available, try:

```
$ sbt
> reload plugins
> clean
> reload return
```

Essentially, this enters the `project` project, cleans it, and returns back to your main project (remember that [sbt is recursive](http://www.scala-sbt.org/release/docs/Getting-Started/Full-Def.html#sbt-is-recursive) – each `project/` folder is an sbt project in itself!).

### Requirements

* sbt 1.0+, Scala 2.12.x
* sbt 0.13.5+ (requires `AutoPlugin`), Scala 2.10.x


## Usage

To use sbt-stats, simply enter the `stats` command in the sbt console. It will provide basic statistics about the source files in your single-project build, or all projects aggregated if you have a multi-project setup. Here's an example for the [akka](https://github.com/akka/akka) project:

```
akka > stats
[info] Code Statistics for project:
[info]
[info] Files
[info] - Total:      267 files
[info] - Scala:      214 files (80.1%)
[info] - Java:       53 files (19.9%)
[info] - Total size: 2,101,408 Bytes
[info] - Avg size:   164,258 Bytes
[info] - Avg length: 4,545 lines
[info]
[info] Lines
[info] - Total:      57,443 lines
[info] - Code:       34,388 lines (59.9%)
[info] - Comment:    15,864 lines (27.6%)
[info] - Blank:      7,191 lines (12.5%)
[info] - Bracket:    5,760 lines (10.0%)
[info]
[info] Characters
[info] - Total:      1,821,830 chars
[info] - Code:       1,272,314 chars (69.8%)
[info] - Comment:    549,516 chars (30.2%)
```

The default output provides File, Line and Character statistics (the analysis can be [easily extended](https://github.com/orrsella/sbt-stats#extending) with custom logic). These stats are aggregated across all 30 projects in the akka build.

To get statistics for a single project, select it using `project PROJECT_NAME` and run `stats-project`. This will only run the task on the selected project, and won't aggregate any subprojects. For example:

```
akka > project akka-actor
[info] Set current project to akka-actor (in build file:/Users/orr/Development/Scala/Projects/akka/)
akka-actor > stats-project
[info]
[info] Code Statistics for project 'akka-actor':
[info]
[info] Files
[info] - Total:      91 files
[info] - Scala:      78 files (85.7%)
[info] - Java:       13 files (14.3%)
[info] - Total size: 765,238 Bytes
[info] - Avg size:   8,409 Bytes
[info] - Avg length: 229 lines
[info]
[info] Lines
[info] - Total:      20,894 lines
[info] - Code:       10,161 lines (48.6%)
[info] - Comment:    8,078 lines (38.7%)
[info] - Blank:      2,655 lines (12.7%)
[info] - Bracket:    1,583 lines (7.6%)
[info]
[info] Characters
[info] - Total:      680,139 chars
[info] - Code:       405,894 chars (59.7%)
[info] - Comment:    274,245 chars (40.3%)
```

### Output

Here is how line types are defined for the `Lines` output:

* Code – any line that's not a comment or a blank line
* Comment – any line starting with `//`, `/*` or `*`
  * In-line comments (```val x = 5 // my comment```) are *not* counted as comment lines.
  * Multi-line comment blocks (`/* ... \n ... \n ... */`) of `n` lines are counted as *only* 1 comment line and n-1 code lines (if you want more advanced comment parsing you can [easily extend](https://github.com/orrsella/sbt-stats#extending) an `Analyzer` yourself).
* Blank – any empty line or spaces-only
* Bracket – a line consisting of *only* an opening *or* closing curly brace
* Since bracket lines are obviously code: code % + comment % + blank % = 100%

## Configuration

The plugin uses `Analyzer` classes to produce it's statistics. The default analyzers include `FilesAnalyzer`, `LinesAnalyzer` and `CharsAnalyzer` which are automatically used (their output displayed above). You can manually configure which analyzers will be used by setting the `statsAnalyzers` sbt [setting](http://www.scala-sbt.org/release/docs/Getting-Started/Basic-Def.html). For example, add the following to your `build.sbt` file to only use the FilesAnalyzer:

```scala
import com.orrsella.sbtstats._

statsAnalyzers := Seq(new FilesAnalyzer())
```

The default analyzers intentionally have simple and basic implementations, calculating all their stats by simple string reads and comparisons. No reflection or advanced analysis methods are used. If you want more advanced output, see the next section.

## Extending

If you're interested in more statistics (like counting the number of classes, traits, objects, methods, etc.) or implementing the existing analyzers differently, you can easily do it. For example, to create a new `MyAnalyzer` place the following code in the `project` directory (for example in `project/MyAnalyzer.scala`):

```scala
import com.orrsella.sbtstats._
import java.io.File

class MyAnalyzer extends Analyzer {
  // sources: source files as retrieved from sbt's `sources` setting
  // packageBin: the output jar file as retrieved from sbt's `packageBin` task
  def analyze(sources: Seq[File], packageBin: File) = {
    val metric1 = sources.filter(_.getName.endsWith("foo")).length // completely useless metric 1
    val metric2 = packageBin.length^2                              // completely useless metric 2
    ...
    new MyAnalyzerResult(metric1, metric2, ...)
  }
}

// result object for MyAnalyzer
class MyAnalyzerResult(metric1: Int, metric2: Double, ...) extends AnalyzerResult {
  val title = "My Analysis"
  val metrics =
    Seq(
      new AnalyzerMetric("Metric 1:", metric1, "files"),
      new AnalyzerMetric("Metric 2:", metric2, 3500, "Bytes"))
}
```

As you can see, all that you need to do is override the `analyze(sources: Seq[File], packageBin: File)` method of the [Analyzer](https://github.com/orrsella/sbt-stats/blob/master/src/main/scala/com/orrsella/sbtstats/Analyzer.scala) class and use the `sources` sequence (of source files) *and/or* `packageBin` file (the output jar) to calculate the metrics you're interested in. The `AnalyzerResult` object is also pretty straight-forward – the `title` is the string that'll be displayed at the top of the analyzer block in the sbt console, and the `AnalyzerMetric` objects are the body of the block. Each [metric](https://github.com/orrsella/sbt-stats/blob/master/src/main/scala/com/orrsella/sbtstats/AnalyzerMetric.scala) has a mandatory `title` and `value`. Optional are `total` (used to calculate percentage) and `units` for that metric.

Now to tell sbt-stats to use your new analyzer, add it as explained in [configuration](https://github.com/orrsella/sbt-stats#configuration). To add the new analyzer *in addition* to the default analyzers, add the following to `build.sbt`:

```scala
import com.orrsella.sbtstats._

statsAnalyzers += new MyAnalyzer()
```

To have the new analyzer the sole analyzer, without using the default analyzers, add:

```scala
statsAnalyzers := Seq(new MyAnalyzer())
```

Finally, here's what the output of the new `MyAnalyzer` will look like in the sbt console:

```
[info] ...
[info]
[info] My Analysis
[info] - Metric 1: 5 files
[info] - Metric 2: 100 Bytes (60.0%)
```

For a full Analyzer class you can see [FilesAnalyzer](https://github.com/orrsella/sbt-stats/blob/master/src/main/scala/com/orrsella/sbtstats/FilesAnalyzer.scala) for example.

## Feedback

Any comments/suggestions? Let me know what you think – I'd love to hear from you. Send pull requests, issues or contact me: [@orrsella](http://twitter.com/orrsella) and [orrsella.com](http://orrsella.com)

## License

This software is licensed under the Apache 2 license, quoted below.

Copyright (c) 2013 Orr Sella

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
