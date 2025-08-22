@echo off
title Sinal IF

echo ==========================================================
echo                   Sinal IF - via Docker
echo ==========================================================
echo.

echo Abrindo a pagina web...
start http://localhost:8080
echo.

echo Iniciando o servidor Java Spring via Docker...
cd "C:\Program Files\Sinal IF\sinalif"
docker-compose up --build

pause