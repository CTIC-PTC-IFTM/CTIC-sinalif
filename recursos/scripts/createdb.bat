@echo off
set PG_HOST=localhost
set PG_PORT=5432
set PG_USER=postgres
set PGPASSWORD=123456
set PG_BIN="C:\Program Files\PostgreSQL\17\bin"
set NOME_BANCO=bd_sinalif

echo ===============================================
echo Banco de dados: %NOME_BANCO%
echo Host: %PG_HOST%:%PG_PORT%
echo Usuario: %PG_USER%
echo Senha: 123456
echo ===============================================
echo.

echo Verificando a existencia do banco de dados "%NOME_BANCO%"...
%PG_BIN%\psql.exe -l -h %PG_HOST% -p %PG_PORT% -U %PG_USER% -d postgres | findstr /I /C:" %NOME_BANCO% " > nul

if %ERRORLEVEL% == 0 (
    echo INFO: O banco de dados "%NOME_BANCO%" ja existe. Nenhuma acao necessaria.
    echo INFO: O banco de dados nunca e apagado por nenhum script, isto deve ser feito manualmente.
) else (
    echo INFO: O banco de dados "%NOME_BANCO%" nao foi encontrado. Tentando criar...
    %PG_BIN%\createdb.exe -h %PG_HOST% -p %PG_PORT% -U %PG_USER% %NOME_BANCO%
    echo # Banco de dados criado com sucesso.
)