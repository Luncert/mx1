@echo off

:: https://support.google.com/gsa/answer/6316721?hl=en

:: store init workspace
for /F %%i in ('echo %cd%') do ( set init_path=%%i)

:: cd probe-daemon\target
cd /d %~dp0\target

set action=%1%

if "install"=="%action%" (
    set work_path=%cd%
    set command=prunsrv //IS//mx1probe-daemon --Install=prunsrv.exe ^
        --DisplayName=mx1probe-daemon --Description="mx1probe-daemon" --Startup=auto ^
        --StartPath=%work_path% ^
        --StopPath=%work_path% ^
        --Classpath=%work_path%\mx1probe-daemon-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
        --StartClass=org.luncert.mx1.probe.daemon.ProbeDaemonMain ^
        --StartMode=java --StartParams=start ++StartParams="-noBanner" ++StartParams="-s server:port"^
        --StopClass=org.luncert.mx1.probe.daemon.ProbeDaemonMain ^
        --StopMode=java --StopParams=stop ^
        --StdOutput=stdout.log --StdError=stderr.log
    %command%
    cd %init_path%
    goto :eof
)

if "console"=="%action%" (
    prunsrv //TS//mx1probe-daemon
    cd %init_path%
    goto :eof
)

if "start"=="%action%" (
    prunsrv //RS//mx1probe-daemon
    cd %init_path%
    goto :eof
)

if "stop"=="%action%" (
    prunsrv //SS//mx1probe-daemon
    cd %init_path%
    goto :eof
)

if "delete"=="%action%" (
    prunsrv //DS//mx1probe-daemon
    cd %init_path%
    goto :eof
)

if "monitor"=="%action%" (
    prunmgr //MS/mx1probe-daemon
    cd %init_path%
    goto :eof
)

echo usage: mx1probe-daemon.bat [install/console/start/stop/delete/monitor]
