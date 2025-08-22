cd "C:\Program Files\Sinal IF\sinalif"
call ./mvnw clean package -D skipTests
echo # Arquivo .jar criado com sucesso.
cd %~dp0