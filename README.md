# services-authenticated-http-communication

Exemplo de comunicação http entre serviços autenticada com OAuth2 e JWT. Implementada com SpringBoot.

Existem dois serviços nesse projeto: legacy-api e legacy-consumer.

Ambos utilizam o banco H2 em memória, e têm implementações próprias de OAuth2 Authorization Server.

O serviço legacy-api roda na porta 8081, e legacy-consumer na porta 8080.

Ambos tem o endpoint /estados com um CRUD completo:

GET /estados - Lista todos os estados cadastrados
GET /estados/{id} - Busca um estado específico por id
POST /estados - Cadastra um estado
PUT /estados - Atualiza um estado
DELETE /estados - Exclui um estado

Em legacy-api o id é auto-incremental. Em legacy-consumer é manual. A sincronização de legacy-consumer verifica o id para sincronizar (ou não) um estado vindo de legacy-api.

Todos os endpoints precisam de autenticação. É possível autenticar um usuário (fluxos authorization_code e password) em ambos os serviços com as credenciais abaixo:

usuário: admin
senha: admin

No serviço legacy-api também é possível autenticar um client (fluxo client_credentials) com as credentiais abaixo:

client_id: legacy-consumer
cliente_secret: 123

O serviço legacy-consumer possui dois profiles: "local" e "sync". Quando não informado um profile para a aplicação, o profile "default" do Spring tem o mesmo comportamento do profile "local". Para ativar um dos perfis crie uma variável de ambiente SPRING_PROFILES_ACTIVE com valor do profile desejado ("local" ou "sync").

Quando a aplicação roda com o profile "local", ela busca estados apenas na sua própria base. Quando roda com o profile "sync", consulta o serviço legacy-api e sincroniza os estados.

O profile "sync" de legacy-consumer também permite a ativação de um scheduler que realiza sincronizações a cada 10 segundos com o serviço legacy-api. Nesse caso, a sincronização deixa de ser feita ao realizar uma requisição GET em legacy-consumer, sendo feita apenas pelo scheduler. Para ativar o scheduler, criar uma variável de ambiente LEGACY_CONSUMER_SCHEDULED_SYNC com o valor "true".

A aplicação legacy-api é iniciada com dois estados brasileiros cadastrados.

![diagrama-de-sequencia](https://raw.githubusercontent.com/paulosalonso/services-authenticated-http-communication/master/legacy-communication-sequence.jpeg)
