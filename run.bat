@echo off
chcp 65001 > nul
cd /d "%~dp0"
javac -encoding UTF-8 -d out -sourcepath src src\com\lab\translator\Main.java || exit /b 1
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.lab.translator.Main %*
