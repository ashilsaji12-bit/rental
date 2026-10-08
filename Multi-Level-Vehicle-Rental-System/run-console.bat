@echo off
echo ==========================================
echo   RENTX - Multi-Level Vehicle Rental
echo   Starting Terminal Application...
echo ==========================================
echo.

cd /d "%~dp0backend"

:: Set Maven path if not in system PATH
set MAVEN_HOME=C:\tools\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%

:: Compile if needed
if not exist "target\classes\com\vehiclerental\console\ConsoleApplication.class" (
    echo Building project...
    mvn compile -q
)

:: Run the console application using the compiled classes directory.
:: Note: Spring Boot fat-jars use a custom classloader.
::       For the standalone console app, we use target\classes directly.
java -cp "target\classes" com.vehiclerental.console.ConsoleApplication

pause
