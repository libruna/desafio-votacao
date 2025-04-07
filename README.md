<h1 align="center">API de gerenciamento de votos</h1>

## Visão geral
Este projeto é uma API REST para gerenciamento de pautas e registro de votos em assembleias.

### Objetivos
- Cadastrar pautas;
- Abrir sessões de votação em pautas;
- Registrar votos de membros associados;
- Contabilizar votos.

## Decisões técnicas

### Tecnologias
- Java `21`
- Spring Boot `3.4.4`
- Apache Maven `3.9.9`
- MongoDB

### Banco de dados
Além do Java e do Spring Boot, que foram definidos como tecnologias base para o desenvolvimento deste desafio, a escolha do MongoDB como banco de dados foi motivada por sua capacidade de escalabilidade horizontal e pelo alto desempenho em operações de escrita.

Considerando que há a possibilidade de haver o registro de grandes volumes de votos concorrentes, é adequado utilizar uma solução que seja capaz de lidar com múltiplas requisições simultâneas de forma escalável.

## Tarefas bonus

### Integração com sistemas externos

Foi criada uma camada *Client* para simular a integração com sistemas externos, conforme requisitado no desafio. Nela, foi simulada a consulta a uma API externa que recebe o CPF do membro associado e retorna aleatoriamente se o CPF é válido ou não.

### Versionamento da API
Poderia ser utilizado o versionamento por URL, onde o número da versão é anexado à base da aplicação, como no exemplo: *POST /voting-manager/v1/votes*

Desta forma, é fácil distinguir diferentes versões da API, o que facilita a manutenção e a evolução da aplicação sem impactar as versões anteriores.

## Documentação
A especificação OpenAPI da aplicação está disponível em: http://localhost:8080/voting-manager/swagger-ui/index.html

## Coleção do Postman
A coleção da API está disponível para download [aqui](./collection/desafio-votacao.postman_collection.json).

## Configuração do ambiente

### Pré-requisitos
Certifique-se de que os seguintes softwares estão instalados:

- Git
- Java `21`
- Apache Maven `3.9.9`
- Docker `28.0.1`

### Como executar o projeto
Após clonar o repositório, utilizando o terminal, navege até o diretório raiz da aplicação e siga os passos a seguir:

1. Suba o MongoDB com Docker:
   - Rode o comando `docker-compose up -d` para iniciar uma instância local do MongoDB com os dados pré-cadastrados.
2. Instale as dependências com `mvn install`
3. Inicie a aplicação com `mvn spring-boot:run`
4. Execute os testes unitários com `mvn test`

## Oportunidades de melhorias futuras

- Containerizar a aplicação e incluir ela no docker-compose.
- Otimizar a quantidade de consultas ao banco nas validações do fluxo de votação.
- Incluir dados pré-cadastrados na inicialização do banco de dados local.
- Melhorar o desempenho do fluxo de votação com o uso de caching, por exemplo.
- Incluir endpoints para consulta dos dados de sessões.
