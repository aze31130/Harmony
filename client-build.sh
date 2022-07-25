#!/bin/bash
set -e
JAVA_VERSION="8"
BUILD_FOLDER="client-build"
OUTPUT_JAR_NAME="client-server.jar"
echo "Compiling client !"
if [ -d $BUILD_FOLDER ]
then
    rm -rf $BUILD_FOLDER
fi
mkdir $BUILD_FOLDER
javac --release $JAVA_VERSION -d $BUILD_FOLDER $(find client-source -type f -name "*.java")
cd $BUILD_FOLDER
jar cfe $OUTPUT_JAR_NAME Main $(find . -type f -name "*.class")