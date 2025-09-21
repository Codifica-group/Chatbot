# Chatbot

Microsserviço de Chatbot.

## Escopo

Este repositório contém o código-fonte do microsserviço de Chatbot, responsável pela automação do atendimento ao cliente. Ele gerencia o estado da conversa, publica eventos para o backend principal (Backend-V2) para realizar operações como cadastro de clientes e pets, e consome os eventos de resposta para dar continuidade ao fluxo da conversa com o usuário.

## Arquitetura

O projeto é estruturado baseado em conceitos de Clean Arch e SOLID, separando as classes em diferentes camadas para facilitar a manutenção e escalabilidade.

| Pasta | Descrição |
| --- | --- |
| `src/main/java/com/codifica/chatbot` | Raiz do código-fonte da aplicação. |
| `.../core/application` | Camada de aplicação, contendo os casos de uso e as portas de entrada/saída. |
| `.../core/domain` | Camada de domínio, que contém as entidades de negócio e a lógica de domínio, como o estado do chat e os eventos. |
| `.../infrastructure` | Camada de infraestrutura, contendo as implementações das portas, como adaptadores para o banco de dados e para o RabbitMQ. |
| `.../interfaces` | Camada de interface, responsável por expor a aplicação, neste caso, através de controladores REST. |
| `src/main/resources` | Contém os arquivos de configuração da aplicação. |

## Eventos

A aplicação utiliza RabbitMQ para comunicação assíncrona com o backend principal.

**Eventos Publicados:**

* **`cliente.para-cadastrar`**: Disparado para solicitar o cadastro de um novo cliente no backend.
* **`pet.para-cadastrar`**: Disparado para solicitar o cadastro de um novo pet para um cliente existente.

**Eventos Consumidos:**

* **`cliente.cadastro.response`**: Consumido para saber o resultado (sucesso ou falha) do cadastro do cliente e atualizar o estado do chat.
* **`pet.cadastro.response`**: Consumido para saber o resultado (sucesso ou falha) do cadastro do pet e atualizar o estado do chat.

## Compilação e Execução

### Pré-requisitos

* Java 21
* Maven 3.9 ou superior
* Docker e Docker Compose
* Variáveis de ambiente configuradas

### Variáveis de Ambiente

As seguintes variáveis de ambiente precisam ser configuradas para a aplicação:

| Variável | Descrição                                                                                     |
| --- |-----------------------------------------------------------------------------------------------|
| `SPRING_PROFILES_ACTIVE` | Ativa o perfil de configuração do Spring. **Padrão:** `dev` ou `prod`.                        |
| `API_URL` | Endereço base da API do backend. **Padrão dev:** `http://localhost:8080/api/`.                |
| `DB_URL` | Endereço de conexão do banco de dados. **Padrão dev:** `jdbc:mysql://localhost:3306/Chatbot`. |
| `DB_USERNAME` | Nome de usuário do banco de dados. **Padrão dev:** `root`.                                    |
| `DB_PASSWORD` | Senha do banco de dados. **Deve ser definida.**                                      |
| `CORS_ALLOWED_ORIGINS` | Libera o acesso à API para o frontend. **Padrão dev:** `http://localhost:5173`.               |

### Passos para Execução

1.  **Após configurar as variáveis de ambiente, inicie o Docker Compose:**

    ```bash
    docker-compose up -d
    ```

2.  **Compile e execute a aplicação através da sua IDE, ou com Maven:**

    ```bash
    ./mvnw spring-boot:run
    ```