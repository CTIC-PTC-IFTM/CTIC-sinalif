@echo off
title Sinal IF

:: ================================================================
:: Verificacao e Solicitacao de Privilegios de Administrador
:: ================================================================
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo Solicitando privilegios de administrador...
    goto UACPrompt
) else ( goto gotAdmin )

:UACPrompt
    echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    echo UAC.ShellExecute "%~s0", "", "", "runas", 1 >> "%temp%\getadmin.vbs"
    "%temp%\getadmin.vbs"
    exit /B

:gotAdmin
    if exist "%temp%\getadmin.vbs" ( del "%temp%\getadmin.vbs" )
    pushd "%CD%"
    CD /D "%~dp0"
    cls

echo ==========================================================
echo                        Sinal IF
echo ==========================================================
echo.

echo Iniciando servico do PostgreSQL...
net start "postgresql-x64-17"

echo Abrindo a pagina web...
start http://localhost:8080
echo.

echo Iniciando o servidor Java Spring...
java -jar "C:\Program Files\Sinal IF\sinalif\target\sinalif-1.0.jar"

exit