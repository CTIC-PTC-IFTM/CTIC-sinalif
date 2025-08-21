# Inicialização Automática

## Estrutura dos arquivos

1. Acesse a pasta de inicialização referente ao seu usuário do Windows utilizando o comando de atalho "Windows" + "R" e digite "**shell:startup**".
2. Cole o arquivo "autostart_sinalif.bat" na pasta em que foi aberta ("C:\Users\USUARIO\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup") para que estes sejam executados na inicialização da máquina.
3. Cole o arquivo "sinalif-1.0.jar" na pasta "C:\Program Files\Sinal IF".
4. Confira se as informações de caminhos e links contido no arquivo .bat estão de acordo com o seu uso.

Pronto, feito isto agora o Sinal IF e seus recursos serão executados automaticamente na inicialização da máquina.

## Atualização do arquivo .jar

A cada vez em que o sistema passa por alguma atualização é necessário que seja criado novamente o arquivo .jar. Para isso basta seguir o seguinte passo a passo:

1. Abra o terminal do Windows na pasta raiz da aplicação (onde está o pom.xml) e execute o seguinte comando "**.\mvnw clean package**"
3. Se a execução for um sucesso, o arquivo .jar será gerado na pasta "target". Com isto, confira se o nome do arquivo é "sinalif-1.0.jar", se não, renomeie-o.
4. Cole o arquivo "sinalif-1.0.jar" na pasta "C:\Program Files\Sinal IF" substituindo o arquivo antigo.
