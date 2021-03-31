@echo off
setlocal EnableDelayedExpansion
setlocal EnableExtensions


set MVNW=mvnw.cmd

SET CMD=%1
if "%CMD%"=="" (
  set CMD=simulator
)
goto %CMD%

:simulator
!MVNW! verify -Psimulator -DskipTests -Dcodename1.platform^=javase

goto :EOF
:desktop
!MVNW! verify -Prun-desktop -DskipTests -Dcodename1.platform^=javase

goto :EOF
:settings
!MVNW! cn:settings

goto :EOF
:update
!MVNW! cn:update

goto :EOF
:help
echo run.bat [COMMAND]
echo Commands:
echo   simulator
echo     Runs app using Codename One Simulator
echo   desktop
echo     Runs app as a desktop app.
echo   settings
echo     Opens Codename One settings
echo   update
echo     Update Codename One libraries
