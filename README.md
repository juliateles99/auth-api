# Projeto Auth API

## Visão Geral

Bem-vindo ao projeto **Auth API**! Este projeto serve como uma solução de backend robusta para autenticação e autorização de usuários usando JWT (JSON Web Tokens) integrado com Spring Security. Ele fornece funcionalidades fundamentais para gerenciar o registro e login de usuários, garantindo acesso seguro a recursos protegidos.

O objetivo principal é oferecer uma maneira segura e direta de lidar com a autenticação em aplicações web modernas. Todas as interações com o sistema são projetadas para serem através de uma API REST. A descrição do projeto é: "API com autenticação JWT e Spring Security".

---

## Funcionalidades Principais

O sistema inclui as seguintes características chave:

* **Gerenciamento e Autenticação de Usuários:**
    * Permite que novos usuários se **registrem** com um nome de usuário e senha.
    * Permite que usuários existentes façam **login** usando suas credenciais.
    * As senhas dos usuários são armazenadas de forma segura usando **criptografia BCrypt**.
    * Impede o registro com um nome de usuário já existente.
* **Autorização Baseada em JWT:**
    * Gera um **token JWT** após um registro ou login bem-sucedido, que é retornado ao cliente.
    * Protege endpoints da API, exigindo um token JWT Bearer válido no cabeçalho "Authorization" para acesso.
    * Valida tokens JWT recebidos quanto à autenticidade e expiração.
* **Gerenciamento de Papéis (Básico):**
    * Atribui um papel padrão (ex: `ROLE_USER`) a usuários recém-registrados.
    * O sistema é estruturado para suportar controle de acesso baseado em papéis para diferentes endpoints da API.

---

## Tecnologias & Ferramentas

Este projeto utiliza uma stack Java moderna:

* **Linguagem:** Java 17
* **Framework Principal:** Spring Boot 3.2.3
    * **Spring Web:** Para construir APIs RESTful.
    * **Spring Security:** Para autenticação e controle de acesso.
    * **Spring Data JPA:** Para acesso simplificado a dados e Mapeamento Objeto-Relacional (ORM).
* **Banco de Dados:** PostgreSQL (versão 42.6.0 especificada no pom.xml)
* **Manipulação de JWT:** Biblioteca `io.jsonwebtoken` (jjwt-api, jjwt-impl, jjwt-jackson) versão 0.11.5
* **Build & Gerenciamento de Dependências:** Apache Maven
* **Utilitários:**
    * **Lombok:** Para reduzir código boilerplate (ex: getters, setters, construtores).
* **Testes:**
    * JUnit 5 (via `spring-boot-starter-test`).

---

##  Primeiros Passos: Configuração & Execução

Siga estes passos para colocar o projeto em funcionamento na sua máquina local:

1.  **Pré-requisitos:**
    * **Java JDK 17** ou mais recente instalado.
    * **Apache Maven** instalado.
    * Uma instância do servidor **PostgreSQL** em execução.

2.  **Clone o Repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    cd auth-api
    ```

3.  **Configure o Banco de Dados & Segredos JWT:**
    * Certifique-se de que seu servidor PostgreSQL está ativo.
    * Crie um banco de dados (ex: `authdb`).
    * Abra o arquivo `src/main/resources/application.properties` e atualize as seguintes propriedades com seus detalhes do PostgreSQL e JWT:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/authdb # Ou o nome do seu BD
        spring.datasource.username=seu_usuario_postgres
        spring.datasource.password=sua_senha_postgres

        jwt.secret=segredoSuperSecreto123 # Sua chave super secreta para JWTs
        jwt.expiration=86400000 # Validade do token em milissegundos (ex: 24 horas)
        ```
    * A aplicação usa `spring.jpa.hibernate.ddl-auto=update`, então o Hibernate tentará atualizar o schema com base nas suas entidades. Para migrações mais controladas em um ambiente de produção, considere ferramentas como Flyway ou Liquibase.

4.  **Construa o Projeto:**
    Use o Maven para compilar o código fonte e baixar as dependências:
    ```bash
    mvn clean install
    ```

5.  **Execute a Aplicação:**
    Você pode iniciar a aplicação usando o plugin Maven do Spring Boot:
    ```bash
    mvn spring-boot:run
    ```
    Alternativamente, você pode executar a classe principal `AuthapiApplication.java` diretamente da sua IDE.

    A aplicação normalmente iniciará em `http://localhost:8080`.

---

## Endpoints da API

Aqui estão os principais endpoints fornecidos pela Auth API:

* **Autenticação:**
    * `POST /auth/register`
        * **Ação:** Registra um novo usuário.
        * **Corpo da Requisição:** `RegisterRequest` (JSON com `username` e `password`).
        * **Resposta:** `AuthResponse` (JSON com `token`) em caso de registro bem-sucedido.
        * **Erro:** Retorna um erro se o nome de usuário já existir ("Usuário já existe").
    * `POST /auth/login`
        * **Ação:** Autentica um usuário existente.
        * **Corpo da Requisição:** `LoginRequest` (JSON com `username` e `password`).
        * **Resposta:** `AuthResponse` (JSON com `token`) em caso de login bem-sucedido.
        * **Erro:** Retorna um erro para credenciais inválidas.

* **Endpoints Protegidos:**
    * Quaisquer outros endpoints configurados em `SecurityConfig.java` (ex: `anyRequest().authenticated()`) exigirão um token JWT Bearer válido no cabeçalho `Authorization`.
        ```
        Authorization: Bearer <seu_token_jwt>
        ```

---

## Detalhes de Segurança

* **Filtro de Autenticação JWT (`JwtAuthenticationFilter.java`):** Este filtro customizado intercepta requisições recebidas, extrai o JWT do cabeçalho `Authorization`, o valida e configura o contexto de segurança do Spring se o token for válido.
* **Configuração de Segurança (`SecurityConfig.java`):**
    * Define rotas públicas (como `/auth/**`) e rotas protegidas.
    * Configura o `PasswordEncoder` (usando `BCryptPasswordEncoder`) para hashing seguro de senhas.
    * Configura o `AuthenticationManager`.
    * Desabilita CSRF e configura o gerenciamento de sessão para ser stateless, já que JWTs são usados.
* **Serviço JWT (`JwtService.java`):** Lida com todas as operações relacionadas a JWT: geração, parse de claims (como nome de usuário e data de expiração) e validação contra a chave secreta configurada.
* **Serviço de Detalhes do Usuário (`UserDetailsServiceImpl.java`):** Implementa o `UserDetailsService` do Spring Security para carregar dados específicos do usuário (nome de usuário, senha, autoridades/papéis) do banco de dados durante o processo de autenticação.

---

## Testes

O projeto inclui uma configuração básica de teste:

* **`AuthapiApplicationTests.java`**: Um teste simples para garantir que o contexto da aplicação Spring Boot carrega corretamente.
* A dependência `spring-boot-starter-test` fornece JUnit 5, Mockito e outros utilitários de teste, permitindo o desenvolvimento adicional de testes unitários e de integração.

**Oportunidades Adicionais de Teste:**
* Testes unitários para `AuthService`, `JwtService`.
* Testes de integração para os endpoints do `AuthController`.
* Testes focados em segurança para as regras de autenticação e autorização.

---

Espero que esta documentação forneça um entendimento claro do projeto Auth API!
