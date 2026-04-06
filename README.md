# Person API

> API REST de gerenciamento de pessoas construída com **Spring Boot 4**, **Java 21** e boas práticas de engenharia de software — arquitetura limpa, cobertura de testes, análise estática de qualidade de código e pipeline de CI/CD automatizado.

🔗 **Ambiente de desenvolvimento (live):** [https://comjavateste-development.up.railway.app/swagger-ui/index.html](https://comjavateste-development.up.railway.app/swagger-ui/index.html)

---

## Índice

- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Padrões e decisões técnicas](#padrões-e-decisões-técnicas)
- [Endpoints](#endpoints)
- [Como executar localmente](#como-executar-localmente)
- [Como executar via Docker](#como-executar-via-docker)
- [Testes e cobertura](#testes-e-cobertura)
- [Análise de qualidade com SonarQube](#análise-de-qualidade-com-sonarqube)
- [Git hook — bloqueio automático no push](#git-hook--bloqueio-automático-no-push)
- [CI/CD com GitFlow e Railway](#cicd-com-gitflow-e-railway)

---

## Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.5 | Framework web e IoC |
| Maven | 3.9+ | Build e gerenciamento de dependências |
| Lombok | — | Redução de boilerplate nos modelos e DTOs |
| springdoc-openapi | 3.0.2 | Documentação automática via Swagger UI |
| JaCoCo | 0.8.14 | Medição e enforcement de cobertura de testes |
| SonarQube | — | Análise estática de qualidade e segurança |
| Docker | — | Containerização da aplicação |
| Railway | — | Plataforma de deploy em nuvem (CI/CD) |
| GitHub Actions | — | Orquestração dos pipelines de entrega |

---

## Arquitetura

O projeto segue os princípios da **Arquitetura em Camadas** inspirada em Clean Architecture, com separação clara de responsabilidades:

```
src/main/java/com/java/teste/
│
├── domain/                    # Núcleo da aplicação — independente de frameworks
│   ├── model/                 # Entidades e enums de domínio
│   ├── repository/            # Interfaces de repositório (contratos)
│   └── exception/             # Exceções de negócio
│
├── application/               # Casos de uso — orquestram as regras de negócio
│   └── usecase/               # Um arquivo por operação (SRP)
│
├── infrastructure/            # Implementações técnicas
│   ├── persistence/           # Repositório em memória
│   └── config/                # Configurações Spring (OpenAPI, converters)
│
└── presentation/              # Camada HTTP
    ├── controller/            # Endpoints REST
    ├── dto/                   # Request/Response objects
    └── handler/               # Tratamento global de exceções
```

### Por que essa estrutura?

- **`domain` sem dependências externas** — as regras de negócio não conhecem Spring, JPA nem nenhuma lib de infraestrutura. Isso facilita testes unitários puros e a troca de frameworks no futuro.
- **Um use case por arquivo** — cada classe tem uma única razão para mudar (Single Responsibility Principle). Fica fácil localizar, testar e evoluir qualquer operação isoladamente.
- **Repositório como interface no `domain`** — a camada de negócio depende de uma abstração, não de uma implementação. A implementação em memória pode ser substituída por JPA, MongoDB etc. sem alterar um único use case.
- **DTOs separados de entidades** — o modelo de domínio não vaza para a API. Requests e responses têm contratos próprios, o que dá liberdade para evoluir cada um independentemente.

---

## Padrões e decisões técnicas

| Padrão / Prática | Decisão |
|---|---|
| **Clean Architecture** | Separação em `domain`, `application`, `infrastructure` e `presentation` para isolamento de responsabilidades |
| **Use Case Pattern** | Cada operação de negócio é uma classe autônoma com método `execute()` |
| **Repository Pattern** | Contrato definido no domínio, implementação resolvida pelo Spring na infraestrutura |
| **Global Exception Handler** | `@RestControllerAdvice` centraliza o tratamento de erros e padroniza os contratos de erro da API |
| **DTO Pattern** | `PersonRequest`, `PersonPatchRequest` e `PersonResponse` isolam contratos de entrada e saída do modelo interno |
| **Lombok** | Elimina getters/setters/construtores repetitivos; `@Data`, `@RequiredArgsConstructor` e `@NoArgsConstructor` foram usados estrategicamente |
| **Enum com `fromValue()`** | Enums de query param (`AgeOutputFormat`, `SalaryOutputFormat`) aceitam valores case-insensitive via `Converter` registrado no Spring |
| **Forwarded Headers** | `server.forward-headers-strategy: framework` garante que o Swagger gere URLs HTTPS corretamente quando a aplicação está atrás de proxy reverso (Railway) |
| **JaCoCo enforcement** | Build falha automaticamente se cobertura de instruções dos use cases cair abaixo de 80% |
| **SonarQube Quality Gate** | Análise de código integrada ao pipeline; blocos de código duplicado, code smells e vulnerabilidades são rastreados |
| **Pre-push hook** | `mvn verify` é executado automaticamente antes de qualquer push, impedindo que código sem cobertura suficiente chegue ao repositório remoto |
| **Multi-stage Dockerfile** | Imagem de build separada da imagem de runtime; resultado final usa JRE Alpine com usuário não-root para menor superfície de ataque |

---

## Endpoints

A documentação interativa completa está disponível via Swagger UI:

- **Local:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Development:** [https://comjavateste-development.up.railway.app/swagger-ui/index.html](https://comjavateste-development.up.railway.app/swagger-ui/index.html)

### Resumo

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/person` | Lista todas as pessoas (ordenado por nome) |
| `GET` | `/person/{id}` | Busca pessoa por ID |
| `GET` | `/person/{id}/age?output={days\|months\|years}` | Calcula a idade na unidade desejada |
| `GET` | `/person/{id}/salary?output={full\|min}` | Calcula o salário atual (valor em R$ ou em salários mínimos) |
| `POST` | `/person` | Cadastra nova pessoa |
| `PUT` | `/person/{id}` | Atualiza todos os dados de uma pessoa |
| `PATCH` | `/person/{id}` | Atualiza parcialmente os dados de uma pessoa |
| `DELETE` | `/person/{id}` | Remove uma pessoa |

---

## Como executar localmente

### Pré-requisitos

- Java 21
- Maven 3.9+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/linekerpablo/com.java.teste
cd com.java.teste

# 2. Ative o git hook de qualidade (necessário apenas uma vez)
git config core.hooksPath .githooks

# 3. Execute a aplicação
./mvnw spring-boot:run
```

A aplicação estará disponível em [http://localhost:8080](http://localhost:8080).  
Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Como executar via Docker

### Pré-requisitos

- Docker instalado e rodando

### Passos

```bash
# 1. Build da imagem
docker build -t person-api:local .

# 2. Subir o container
docker run -d \
  --name person-api \
  -p 8080:8080 \
  person-api:local

# 3. Acompanhar os logs (opcional)
docker logs -f person-api

# 4. Parar e remover o container
docker stop person-api && docker rm person-api
```

Swagger UI disponível em: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Testes e cobertura

### Executar os testes

```bash
./mvnw test
```

### Verificar cobertura (mínimo 80% nos use cases)

```bash
./mvnw verify
```

O build falha automaticamente se a cobertura de instruções da camada `application/usecase` estiver abaixo de **80%**.

Após rodar, o relatório HTML estará disponível em:

```
target/site/jacoco/index.html
```

---

## Análise de qualidade com SonarQube

### 1. Subir o servidor SonarQube via Docker Compose

```bash
docker-compose up -d
```

Aguarde subir o SonarQube e acesse [http://localhost:9000](http://localhost:9000).  
Login padrão: `admin` / `admin` (será solicitada troca de senha no primeiro acesso).

### 2. Gerar um token de acesso

1. Acesse [http://localhost:9000](http://localhost:9000)
2. Vá em **My Account → Security → Generate Token**
3. Copie o token gerado

### 3. Rodar a análise

```bash
./mvnw verify sonar:sonar -Dsonar.token=<seu-token>
```

O projeto aparecerá em [http://localhost:9000/projects](http://localhost:9000/projects) com o nome **Person API**.

### 4. Parar o servidor (quando não estiver usando)

```bash
docker-compose down
```

---

## Git hook — bloqueio automático no push

O projeto usa um `pre-push` hook que executa `mvn verify` antes de cada push.  
Se a cobertura dos use cases estiver abaixo de **80%**, o push é **bloqueado automaticamente**.

**Ativar na sua máquina (necessário apenas uma vez):**

```bash
git config core.hooksPath .githooks
```

> Para fazer push ignorando a verificação: `git push --no-verify`

---

## CI/CD com GitFlow e Railway

O projeto adota **GitFlow** como estratégia de branches e usa o **Railway** como plataforma de deploy, orquestrado via **GitHub Actions**.

### Estratégia de branches

| Branch | Finalidade |
|---|---|
| `main` | Código de produção — pipeline configurado, ambiente não publicado (projeto de estudo) |
| `dev` | Integração contínua — deploy automático no ambiente de desenvolvimento |
| `feature/*` | Desenvolvimento de novas funcionalidades — deploy automático no ambiente de desenvolvimento |

### Como funciona

- A cada push em `dev` ou `feature/**`, o workflow `.github/workflows/deploy-dev.yml` é acionado e faz deploy no ambiente de **development** do Railway.
- A cada push em `main`, o workflow `.github/workflows/deploy-main.yml` é acionado e faz deploy no ambiente de **production**.
- Os deploys são feitos via **Railway CLI** com `railway up`, usando secrets do GitHub para autenticação. O autodeploy nativo do Railway está desabilitado; todo deploy passa pelo pipeline do GitHub Actions.

### Ambientes

| Ambiente | URL |
|---|---|
| Development | [https://comjavateste-development.up.railway.app/swagger-ui/index.html](https://comjavateste-development.up.railway.app/swagger-ui/index.html) |
| Production | — (pipeline configurado; ambiente não provisionado neste projeto de estudo) |
