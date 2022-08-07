#!/bin/bash
set -e
JAVA_VERSION="17"
BUILD_FOLDER="server-build"
OUTPUT_JAR_NAME="harmony-server.jar"
echo "Compiling server !"
if [ -d $BUILD_FOLDER ]
then
    rm -rf $BUILD_FOLDER
fi
mkdir $BUILD_FOLDER
javac --release $JAVA_VERSION -d $BUILD_FOLDER $(find server-source -type f -name "*.java")
cd $BUILD_FOLDER
jar cfe $OUTPUT_JAR_NAME Main $(find . -type f -name "*.class")
