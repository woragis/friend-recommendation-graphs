@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

set "JAVA_MIN=16"
set "JAVA_HOME="

call :find_jdk
if not defined JAVA_HOME (
    echo Erro: JDK %JAVA_MIN%+ nao encontrado.
    echo O projeto nao compila com Java 8. Instale JDK 21:
    echo   winget install -e --id EclipseAdoptium.Temurin.21.JDK
    exit /b 1
)

set "JAVAC=%JAVA_HOME%\bin\javac.exe"
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

echo Usando JDK: %JAVA_HOME%
"%JAVAC%" -version
echo.
echo Compilando...
if exist out rmdir /s /q out
mkdir out

dir /s /b src\*.java > sources.txt
"%JAVAC%" -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
    del sources.txt 2>nul
    exit /b 1
)
del sources.txt

echo.
echo Executando Main...
echo.
"%JAVA_EXE%" -cp out app.Main
exit /b %ERRORLEVEL%

:find_jdk
call :try_path "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
if defined JAVA_HOME exit /b 0

for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-21*") do call :try_path "%%~D"
if defined JAVA_HOME exit /b 0

for /d %%D in ("C:\Program Files\Microsoft\jdk-21*") do call :try_path "%%~D"
if defined JAVA_HOME exit /b 0

for /d %%D in ("C:\Program Files\Java\jdk-21*") do call :try_path "%%~D"
if defined JAVA_HOME exit /b 0

for /d %%D in ("C:\Program Files\Java\jdk-17*") do call :try_path "%%~D"
if defined JAVA_HOME exit /b 0

for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do call :try_path "%%~D"
if defined JAVA_HOME exit /b 0

if defined LOCALAPPDATA (
    for /d %%D in ("%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-21*") do call :try_path "%%~D"
    if defined JAVA_HOME exit /b 0
    for /d %%D in ("%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-17*") do call :try_path "%%~D"
)
exit /b 1

:try_path
set "CAND=%~1"
if not exist "%CAND%\bin\javac.exe" exit /b 0
call :check_version "%CAND%\bin\javac.exe"
if !CHECK_OK! equ 1 set "JAVA_HOME=%CAND%"
exit /b 0

:check_version
set "CHECK_OK=0"
set "JAVAC_TEST=%~1"
for /f "tokens=2 delims= " %%V in ('"%JAVAC_TEST%" -version 2^>^&1') do set "VER=%%V"
if not defined VER exit /b 0

echo !VER! | findstr /r "^1\.[0-9]" >nul
if !ERRORLEVEL! equ 0 (
    for /f "tokens=2 delims=." %%M in ("!VER!") do if %%M geq %JAVA_MIN% set "CHECK_OK=1"
    exit /b 0
)

for /f "tokens=1 delims=." %%M in ("!VER!") do if %%M geq %JAVA_MIN% set "CHECK_OK=1"
exit /b 0
