@ECHO OFF

ECHO Compiling the project...
cd ../
javac *.java -d "./build"
ECHO Done!
cd ./build
ECHO.

ECHO Making Jar
ECHO.
jar cvfm Rosemary.jar manifest.txt *.class
ECHO Done!

PAUSE