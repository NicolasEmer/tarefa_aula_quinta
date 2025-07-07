#Acessar por ssh

ssh univates@177.44.248.76


#Enviar jar

scp .\tarefa-0.0.1-SNAPSHOT.jar univates@177.44.248.76:/home/univates/homolog/


#Listar os containers

sudo docker ps -a


#Restart do container

sudo docker restart univates-app-homolog-1


#Mostrar tabela alterada

DESCRIBE nome_da_tabela;
