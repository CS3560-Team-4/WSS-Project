#!/bin/bash
set -e

echo "Cleaning and compiling backend..."

# Run Maven (works if installed on PATH)
if command -v mvn &> /dev/null; then
    mvn clean compile
    echo "Running Java backend..."
    mvn exec:java
else
    echo "Maven not found on this system."
    echo "Attempting to run via WSL (for Windows users)..."
    if command -v wsl &> /dev/null; then
        wsl mvn clean compile
        echo "🚀 Running Java backend (in WSL)..."
        wsl mvn exec:java
    else
        echo "Neither Maven nor WSL found. Please install Maven or use WSL."
        exit 1
    fi
fi

