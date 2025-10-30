$ErrorActionPreference = "Stop"

Write-Host  "Cleaning and compiling backend..." -ForegroundColor Cyan

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn clean compile
    Write-Host "Running backend..." -ForegroundColor Green
    mvn exec:java
}
elseif (Get-Command wsl -ErrorAction SilentlyContinue) {
    Write-Host "⚙ Using Maven to run backend via WSL..." -ForegroundColor Yellow
    wsl mvn clean compile
    wsl mvn exec:java
}
else {
    Write-Host "❌ Neither Maven nor WSL detected. Please install Maven or WSL." -ForegroundColor Red
    exit 1
}

