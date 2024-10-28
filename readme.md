# Projeto Code Challenger - SEA Tecnologia
Descrição

Este repositório contém um desafio de código para a vaga de Desenvolvedor Backend na SEA Tecnologia.


# Dependências do Projeto
O projeto utiliza as seguintes dependências Maven:

- Spring Boot Starter Data JPA: Integração com bancos de dados relacionais através do JPA.
- bcrypt: Para criptografia de senhas, garantindo a segurança dos dados dos usuários.
- PostgreSQL: Driver necessário para a conexão com o banco de dados PostgreSQL.
- Spring Boot Starter Web: Permite a criação de aplicações web utilizando Spring MVC.
- Lombok: Reduz o boilerplate de código em Java, facilitando a manutenção.
- Spring Boot Starter Thymeleaf: Utilizado como mecanismo de templates para a renderização de páginas HTML.
- Spring Boot Starter Security: Implementação de segurança na aplicação, protegendo as rotas e recursos.
- Spring Boot DevTools: Facilita o desenvolvimento com recarga automática.
- Spring Boot Starter Tomcat: Embutido como servidor web para a aplicação.

Pré-requisitos
- Docker: Necessário para a criação de containers.
- Docker Compose: Para orquestração de containers

## Configuração do Application.property
```xml
spring.datasource.url=jdbc:postgresql://localhost:5432/posts-db
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
logging.level.org.springframework.security=DEBUG
```

## Execução do projeto

Para executar o projeto primeiramente.

## 1 - Clone o projeto
```bash
git clone https://github.com/vitorsantosb/sea_tecnologia_code_challenger
```

## 2 - Crie os containers do postgress e do pgAdmin
```bash
docker-compose up -d
```

## 3 - Acesso e rotas

A URL da base da aplicação para acessar é `localhost:8080` e as rotas são:

### Usuários

A aplicação cria um usuário admin base cujo login e senha são:

```json
{
    "email": "admin@exemplo.com",
    "password": "admin"
}
```
#### 1.Login

Rota pública.
```
POST /auth/login
Host: localhost:8080
Content-Type: application/json
```
body
```json
{
    "email": "admin@exemplo.com",
    "password": "admin"
}
```

Resposta esperada
```json
{
    "request": {
        "method": "POST",
        "message": "User authenticated",
        "url": "/auth/login"
    },
    "data": {
        "id": "e1b46fb4-0081-4524-8a5d-5471f60162cc",
        "userEmail": "admin@exemplo.com",
        "token": "3cb24e6c-bc1e-41f5-957f-fffbe4ea8e15",
        "role": "ROLE_ADMIN"
    },
    "message": "Success",
    "statusCode": 200
}
```


#### 2. Listagem de Usuários
```
Endpoint: GET /users/list
Host: localhost:8080
Permissão necessária: ROLE_ADMIN
Headers: Authorization: Bearer {token}
```
Resposta esperada

```json
{
    "request": {
        "method": "GET",
        "message": "List of users",
        "url": "/users"
    },
    "data": [
        {
            "role": "ROLE_ADMIN",
            "address": {},
            "phone": [
                {
                    "phoneType": "CELLPHONE",
                    "phoneNumber": "+55(11) 99999-9999"
                }
            ],
            "cpf": "",
            "id": "e1b46fb4-0081-4524-8a5d-5471f60162cc",
            "email": [
                "admin@exemplo.com"
            ],
            "username": "admin"
        },
        {
            "role": "ROLE_USER",
            "address": {
                "number": "123",
                "zipCode": "72316-007",
                "city": "Brasília",
                "street": "Rua Exemplo",
                "neighborhood": "Centro",
                "state": "DF"
            },
            "phone": [],
            "cpf": "122.223.456-000",
            "id": "fde99d8a-f981-4fa0-90d3-dac951ecdc6d",
            "email": [
                "johndoe@example.com",
                "johndoe23@example.com"
            ],
            "username": "johndoe"
        }
    ],
    "message": "Successfully",
    "statusCode": 200
}
```

#### 3. Criar Usuários
```
Endpoint: POST /users/create
Autenticação: Bearer Token
Permissão necessária: ROLE_ADMIN
Headers: Authorization: Bearer {token}
```
Body de requisição
```JSON
{
    "username": "johndoe",
    "cpf": "23556721111",
    "emails": [
        "johndoe@example.com",
        "johndoe23@example.com"
    ],
    "role": "ROLE_USER",
    "phones": [
        {
            "phoneType": "cellphone",
            "phoneNumber": "+5511999999999"
        }
    ],
    "password": "securepassword",
    "address": {
        "street": "Rua Exemplo",
        "number": "123",
        "neighborhood": "Centro",
        "city": "Brasília",
        "state": "DF",
        "zipCode": "72316-007"
    }
}
```

#### 4. Atualizar Usuário
```
Endpoint: PUT /users/me/update
Autenticação: Bearer Token
Headers: Authorization: Bearer {token}
Params: id - user id
```
Body
```json
{
    "username": "Shadomal",
    "cpf": "39274160100",
    "emails": [
        "shadomal1990@gmail.com"
    ],
    "role": "ROLE_ADMIN",
    "phones": [
        {
            "phoneType": "cellphone",
            "phoneNumber": "+5511999999999"
        }
    ],
    "address": {
        "street": "QN 408 CONJ E LOTE 1",
        "number": "801",
        "neighborhood": "Samambaia",
        "city": "Brasília",
        "state": "DF",
        "zipCode": "72316-007"
    }
}
```

#### 5. Receber dados de um usuário

 ```
Endpoint: GET /users/me
Descrição: Obtém os dados do usuário autenticado.
Autenticação: Bearer Token
Headers: Authorization: Bearer {token}
```

Exemplo de Requisição:
```http
GET localhost:8080/users/me?id=e1b46fb4-0081-4524-8a5d-5471f60162cc
```
Resposta esperada
```json
{
    "request": {
        "method": "GET",
        "message": "User found",
        "url": "/user/me"
    },
    "data": {
        "role": "ROLE_ADMIN",
        "address": {
            "number": "801",
            "zipCode": "72316-007",
            "city": "Brasília",
            "street": "QN 408 CONJ E LOTE 1",
            "neighborhood": "Samambaia",
            "state": "DF"
        },
        "phone": [
            {
                "phoneType": "CELLPHONE",
                "phoneNumber": "+55(11) 99999-9999"
            }
        ],
        "cpf": "392.741.601-00",
        "id": "e1b46fb4-0081-4524-8a5d-5471f60162cc",
        "email": [
            "shadomal1990@gmail.com"
        ],
        "username": "Shadomal"
    },
    "message": "User found",
    "statusCode": 200
}
```

