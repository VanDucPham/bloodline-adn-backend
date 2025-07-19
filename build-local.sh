#!/bin/bash

echo "Building JAR file locally..."

# Check if Maven is installed
if command -v mvn &> /dev/null; then
    echo "Using Maven..."
    mvn clean package -DskipTests
elif [ -f "./mvnw" ]; then
    echo "Using Maven wrapper..."
    chmod +x mvnw
    ./mvnw clean package -DskipTests
else
    echo "Maven not found. Please install Maven or use Maven wrapper."
    exit 1
fi

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "JAR file location: target/"
    ls -la target/*.jar
else
    echo "Build failed!"
    exit 1
fi 