@echo off
title Instalador Sinal IF

:: ================================================================
:: Verificacao e Solicitacao de Privilegios de Administrador
:: ================================================================
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo [INFO] Solicitando privilegios de administrador...
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
set "AUTOSTART_SOURCE=%~dp0recursos\scripts\autostart.bat"
set "STARTUP_DIR=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup"

echo [INFO] Verificando instalacao anterior...
echo.
if exist "%DEST_DIR%" (
    goto :RemoveOldInstall
) else (
    echo [INFO] Criando diretorio de instalacao...
    mkdir "%DEST_DIR%"
    echo # Diretorio criado com sucesso.
    echo.

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
    
    if /I "%confirm%"=="s" (
        echo [INFO] Removendo diretorio da instalacao anterior...
        RD /S /Q "%DEST_DIR%"
        echo # Diretorio anterior removido.
        echo.

        echo [INFO] Criando diretorio de instalacao...
        mkdir "%DEST_DIR%"
        echo # Novo diretorio criado com sucesso.
        echo.

        goto :Install
    )
    if /I "%confirm%"=="n" (
        echo [INFO] Prosseguindo sem limpeza do diretorio anterior.
        echo.
        goto :Install
    )

    echo # Resposta invalida. Por favor, digite S para Sim ou N para Nao.
    echo.
    goto :confirm

:Install
    echo [INFO] Copiando arquivos do programa...
    xcopy "%~dp0*.*" "%DEST_DIR%" /E /H /C /I /Y > nul
    del "%DEST_DIR%\%~nx0"
    echo # Copia do diretorio raiz realizada com sucesso.
    echo.

    echo [INFO] Criando base do banco de dados no PostgreSQL...
    call "%DEST_DIR%\recursos\scripts\createdb.bat"
    echo.

    echo [INFO] Criando arquivo .jar para inicializacao automatica...
    call "%DEST_DIR%\recursos\scripts\createjar.bat"
    echo.

    echo [INFO] Copiando arquivo para a inicializacao automatica...
    if exist "%AUTOSTART_SOURCE%" (
        xcopy "%AUTOSTART_SOURCE%" "%STARTUP_DIR%" /Y > nul
        echo # Arquivo de inicializacao copiado com sucesso.
        echo.
    ) else (
        echo # ERRO: Arquivo inicializacao nao encontrado.
        echo # Por favor, verifique se o arquivo esta presente na pasta %AUTOSTART_SOURCE%.
        pause
        exit /B
    )
    
    goto :CreateShortcut

:CreateShortcut
    echo [INFO] Criando atalho na area de trabalho...
    
    set "LNK_FILENAME=Sinal IF.lnk"
    set "TARGET_PATH=%DEST_DIR%\recursos\scripts\autostart.bat"
    set "ICON_PATH=%DEST_DIR%\recursos\imagens\sinalif.ico"
    for /f "usebackq tokens=*" %%i in (`powershell -NoProfile -Command "[System.Environment]::GetFolderPath('Desktop')"`) do set "DESKTOP_PATH=%%i"
    set "LNK_FULL_PATH=%DESKTOP_PATH%\%LNK_FILENAME%"
    
    if not exist "%ICON_PATH%" (
        echo # ERRO: Arquivo de icone nao encontrado em "%ICON_PATH%".
        echo Crie o atalho manualmente ou corrija o erro e tente novamente.
        pause
        exit /B
    )

    if exist "%LNK_FULL_PATH%" (
        del "%LNK_FULL_PATH%"
    ) 
    
    powershell -ExecutionPolicy Bypass -Command "$ws = New-Object -ComObject WScript.Shell; $s = $ws.CreateShortcut('%LNK_FULL_PATH%'); $s.TargetPath = '%TARGET_PATH%'; $s.IconLocation = '%ICON_PATH%'; $s.WorkingDirectory = '%DEST_DIR%'; $s.Save()"
    
    if exist "%LNK_FULL_PATH%" (
        echo # Atalho criado com sucesso.
        echo.
    ) else (
        echo # ERRO: Nao foi possivel verificar a criacao do atalho em "%LNK_FULL_PATH%".
        echo.
    )

    goto :Finalize

:Finalize
    echo [INFO] Processo finalizado.
    echo.
    echo ===========================================
    echo     Instalacao Concluida!
    echo ===========================================
    echo.

    goto :openApp

:openApp
    set "openApp="
    set /p "openApp=Voce deseja executar o Sinal IF? (s/n): "
    echo.
    
    if /I "%openApp%"=="s" (
        cls
        title Sinal IF
        call "C:\Program Files\Sinal IF\recursos\scripts\autostart.bat"
        exit /B
    ) 
    
    if /I "%openApp%"=="n" (
        exit /B
    )

    echo # Resposta invalida. Por favor, digite S para Sim ou N para Nao.
    echo.
    goto :openApp
