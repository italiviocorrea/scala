# AKKA HTTP api REST

O objetivo deste projeto é explorar os recursos do AKKA HTTP para criar API's REST em linguagem
Scala. Para este CRUD escolhi a automação da tabela de UF (Unidade Federativa) do Brasil. Neste
projeto foi utilizado o AKKA HTTP, scala, e para persistência o Quill.

## Executando a aplicação 

Antes de executar a aplicação, execute o docker-compose para ativar a dependências do projeto

```
docker-compose up -d
```
O docker compose vai ativar um stack com os seguintes componentes : Postgresql, Pgadmin. Para verificar se o stack subiu completo use o comando :

```
docker-compose ps
```

Quando não precisar mais do stack, utilize o seguinte comando para parar :
```
docker-compose down
```
As seguintes URIs para acessar : swagger, health check e pgAdmin.
- Documentação OpenAPI
```
http://localhost:8080/swagger-ui
```

- Health Checks
```
http://localhost:8080/dfe/v1/ufs/healtcheck/

```
- PgAdmin : ferramenta para gerenciar o postgresql
```
http://localhost:8181
```
