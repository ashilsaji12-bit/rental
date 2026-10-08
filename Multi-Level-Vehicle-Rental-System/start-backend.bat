@echo off
echo ==========================================
echo   RENTX - Multi-Level Vehicle Rental
echo   Starting Spring Boot Backend...
echo ==========================================
echo.

:: Set Maven path if not in system PATH
set MAVEN_HOME=C:\tools\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

cd /d "%~dp0backend"

echo Starting on http://localhost:8080
echo Press Ctrl+C to stop.
echo.

mvn spring-boot:run

pause
