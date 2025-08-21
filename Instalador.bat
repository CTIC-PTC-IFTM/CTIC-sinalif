@echo off
title Instalador Sinal IF

:: ================================================================
:: Verificacao e Solicitacao de Privilegios de Administrador
:: ================================================================
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo [1/8] Solicitando privilegios de administrador...
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

echo ===========================================
echo     Instalador - Sinal IF
echo ===========================================
echo.

set "DEST_DIR=C:\Program Files\Sinal IF"
set "AUTOSTART_SOURCE=%~dp0resources\inicializacao_automatica\autostart_sinalif.bat"
set "STARTUP_DIR=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup"

echo [2/8] Verificando instalacao anterior...
echo.
if exist "%DEST_DIR%" (
    goto :RemoveOldInstall
) else (
    REM Se a pasta nao existe, vai direto para a instalacao
    goto :Install
)

:RemoveOldInstall
    echo # Uma instalacao anterior foi encontrada em "%DEST_DIR%".
    echo - Ao aceitar realizar uma instalacao limpa, o diretorio anterior sera excluido.
    echo - Negar a exclusao permitira a existencia do diretorio anterior.
    echo.
    
:confirm
    set "confirm="
    set /p "confirm=Voce deseja prosseguir com a instalacao limpa? (s/n): "
    echo.
    
    if /I "%confirm%"=="s" goto :ActionDelete
    if /I "%confirm%"=="n" goto :ActionSkipDelete

    echo # Resposta invalida. Por favor, digite S para Sim ou N para Nao.
    echo.
    goto :confirm

:ActionDelete
    echo [3/8] Removendo diretorio da instalacao anterior...
    RD /S /Q "%DEST_DIR%"
    echo # Diretorio anterior removido.
    echo.
    goto :Install

:ActionSkipDelete
    echo [3/8] Prosseguindo sem limpeza do diretorio anterior.
    echo.
    goto :Install

:Install
    echo [4/8] Criando diretorio de destino...
    mkdir "%DEST_DIR%"
    echo # Diretorio criado com sucesso.
    echo.
    
    echo [5/8] Copiando arquivos do programa...
    xcopy "%~dp0*.*" "%DEST_DIR%" /E /H /C /I /Y > nul
    echo # Copia do diretorio raiz realizada com sucesso.
    echo.

    echo [6/8] Copiando arquivo para a inicializacao automatica...
    if exist "%AUTOSTART_SOURCE%" (
        xcopy "%AUTOSTART_SOURCE%" "%STARTUP_DIR%" /Y > nul
        echo # Arquivo de inicializacao copiado com sucesso.
        echo.
    ) else (
        echo # ERRO: Arquivo 'autostart_sinalif.bat' nao encontrado.
        echo # Por favor, verifique se o arquivo esta presente na pasta %AUTOSTART_SOURCE%.
        pause
        exit /B
    )

    goto :CreateShortcut

:CreateShortcut
    echo [4/5] Criando atalho na area de trabalho...

    set "LNK_FILE=%USERPROFILE%\Desktop\Sinal IF.lnk"
    set "TARGET_PATH=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup\autostart_sinalif.bat"
    set "ICON_PATH=%DEST_DIR%\resources\img\sinalif.ico"
    set "WORK_DIR=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup"

    if exist "%LNK_FILE%" del /F /Q "%LNK_FILE%"

    REM --- Cria um script VBS temporario para criar o atalho ---
    echo Set oShell = CreateObject("WScript.Shell") > "%temp%\createshortcut.vbs"
    echo Set oLink = oShell.CreateShortcut("%LNK_FILE%") >> "%temp%\createshortcut.vbs"
    echo oLink.TargetPath = "%TARGET_PATH%" >> "%temp%\createshortcut.vbs"
    echo oLink.IconLocation = "%ICON_PATH%" >> "%temp%\createshortcut.vbs"
    echo oLink.WorkingDirectory = "%WORK_DIR%" >> "%temp%\createshortcut.vbs"
    echo oLink.Save >> "%temp%\createshortcut.vbs"

    REM --- Executa o VBS e depois o apaga ---
    cscript //nologo "%temp%\createshortcut.vbs" > nul
    del "%temp%\createshortcut.vbs"

    if exist "%LNK_FILE%" (
        echo # Atalho "Sinal IF.lnk" criado na area de trabalho.
    ) else (
        echo # ERRO: Nao foi possivel criar o atalho na area de trabalho.
    )
    goto :Finalize

:Finalize
    echo.
    echo [8/8] Processo finalizado.
    echo.
    echo ===========================================
    echo     Instalacao Concluida!
    echo ===========================================
    echo.
    pause
    exit /B