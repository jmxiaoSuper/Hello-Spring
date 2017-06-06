@echo off
set localdir=%cd%
call mvn install:install-file -Dfile=%localdir%/ojdbc6-11.2.0.1.0.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.1.0 -Dpackaging=jar

pause
