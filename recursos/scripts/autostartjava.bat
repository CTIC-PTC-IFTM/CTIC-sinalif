@echo off
title Sinal IF

echo ==========================================================
echo                   Sinal IF - via Java
echo ==========================================================
echo.

echo Iniciando servico do PostgreSQL...
start /B "postgresql-x64-17"
echo.

echo Abrindo a pagina web...
start http://localhost:8080
echo.

echo Iniciando o servidor Java Spring...
java -jar "C:\Program Files\Sinal IF\sinalif\target\sinalif-1.0.jar"

pause