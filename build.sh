#!/bin/bash

echo "Starting build process..."

# Clean previous builds
echo "Cleaning previous builds..."
rm -rf target/

# Build with Maven
echo "Building with Maven..."
./mvnw clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "JAR file location: target/"
    ls -la target/*.jar
else
    echo "Build failed!"
    exit 1
fi 