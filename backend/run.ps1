Write-Host "Cleaning and compiling backend..."
.\mvnw.cmd clean compile

Write-Host "Running Java backend..."
.\mvnw.cmd exec:java

