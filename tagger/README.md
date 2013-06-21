Tagger Description
====================

Requires
---------------------
+	Java 1.7  (http://www.java.com/en/download/)
+	Apache Ant (http://ant.apache.org/bindownload.cgi)

How to build & run
---------------------
1. Download the whole tagger directory and its content, it should contain:
	- + /tagger
	- |
	- |-------Launcher.java
	- |-------Tagger.java
	- |-------TaggerGUI.java
	- |-------UniTagger.java
	- |-------build.xml
	- |-------README.md
	- |-------/data
		-	|
		- 	|--icon.png
		- 	|--gson-2.2.4.jar
	
2. Switch into the tagger folder and run:
	- > ant
	- If it builds succesfully, it generates a directory called src.
	
3. To run the program, switch into the src directory and run:
   - > java Launcher