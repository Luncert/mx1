@echo off

:: store init workspace
for /F %%i in ('echo %cd%') do ( set init_path=%%i)

:: cd root path
cd /d %~dp0

:: build mx1probe jar
mvn package

:: build docker image in root dir
docker build -t luncert/mx1probe:1.0.0 ./

cd %init_path%