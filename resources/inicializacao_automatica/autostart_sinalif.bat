echo Abrindo pgAdmin...
start "pgAdmin4" "C:\Program Files\PostgreSQL\17\pgAdmin 4\runtime\pgAdmin4.exe"

echo Aguardando 30 segundos para abrir servidor Java Spring...
timeout /t 15 /nobreak > nul
echo Iniciando em 15 segundos
timeout /t 12 /nobreak > nul
echo Iniciando em 3 segundos
timeout /t 1 /nobreak > nul
echo Iniciando em 2 segundos
timeout /t 1 /nobreak > nul
echo Iniciando em 1 segundo
timeout /t 1 /nobreak > nul


echo Abrindo a página web...
start http://localhost:8080

echo Iniciando o servidor Java Spring...
java -jar "C:\Program Files\Sinal IF\CTIC-sinalif\resources\inicializacao_automatica\sinalif-1.0.jar"

exit