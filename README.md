- total file types (java, scala, etc.)
- total lines, code lines, comment lines, blank lines
- also show percentages
- file sizes (in KB, average file size, file lengths, avg)
- line width, averages


- Source Files
Total files: 49
Java files: 0 (0%)
Scala files: 49 (100%)
Other files: 0 (0%)

- Lines
Total lines: 1047
Code lines: 1024 (94%)
Comment lines: 23 (4%)
Bracket lines: 200 (2%)
Blank lines: 20 (2%)

- File size
Total file size: 840KB
Average file size: 28KB


todo:
+ change handler to Analyzer
- add average file length (lines, chars)
+ rename analyzers to more appropriate names: FileTypeAnalyzer, LineTypeAnalyzer, FileSizeAnalyzer, etc.
+ add use of state.log or streams.log
- scope task to other tasks/keys so it's available to all configurations (even user defined ones)
- readme - known issues (comment limits)
+ make Analyzers extendable thru settings
- write tests