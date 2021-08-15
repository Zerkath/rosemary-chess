echo -e "Compiling the project\n"
cd ../
javac *.java -d "./build"
echo -e "Done!\n"
cd ./build

echo -e "Making Jar\n"
jar cvfm Rosemary.jar manifest.txt *.class
echo -e "\nDone!"
