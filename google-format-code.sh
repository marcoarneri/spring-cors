#!/usr/bin/env sh

GOOGLE_JAVA_FORMAT_VERSION=1.18.1
mkdir -p .cache
cd .cache || exit
if [ ! -f google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar ]
then
    curl -LJO "https://github.com/google/google-java-format/releases/download/v${GOOGLE_JAVA_FORMAT_VERSION}/google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar"
    chmod 755 google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar
fi
cd ..

java -jar .cache/google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar --replace $(find . -type f -name "*.java")

## se JAVA 17 escludo le classi con il String text block perchè non ancora supportate da google java format
java -jar .cache/google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar --replace $(find . -type f -name "*.java")
