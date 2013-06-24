Tagger Description
====================

Requirements
---------------------
+	[Java 1.7](http://www.java.com/en/download/)
+	[Apache Ant](http://ant.apache.org/bindownload.cgi)

How to build & run
---------------------
1. Download the whole tagger directory and its content, it should contain:
~~~~
    /tagger
    |
    |---Exporter.java
    |---Launcher.java
    |---PyExe.java
    |---Tagger.java
    |---TaggerGUI.java
    |---UniTagger.java
    |---TrigramTagger.java
    |---build.xml
    |---README.md
    |---.gitignore
    |---/data
        |
        |---icon.png
        |---gson-2.2.4.jar
~~~~

2. Switch into the tagger folder and run:
``` bash
> ant
```
If it builds succesfully, it generates a directory called <tt>src</tt>.

3. To run the program, switch into the <tt>src</tt> directory and run:
``` bash
> java Launcher
```
