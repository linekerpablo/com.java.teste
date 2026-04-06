# Person API

API REST de gerenciamento de pessoas desenvolvida com Spring Boot 4 e Java 21.

---

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker + Docker Compose (para o SonarQube)

---

## Executar a aplicação

### Localmente

```bash
mvn spring-boot:run
```

### Via Docker

**Build da imagem:**

```bash
docker build -t person-api:local .
```

**Subir o container:**

```bash
docker run -d \
  --name person-api \
  -p 8080:8080 \
  person-api:local
```

**Parar e remover:**

```bash
docker stop person-api && docker rm person-api
```

Swagger UI disponível em: http://localhost:8080/swagger-ui.html

---

## Testes unitários

```bash
mvn test
```

---

## Cobertura de código (JaCoCo)

Gera o relatório e valida que a camada `application/usecase` tem no mínimo **80% de cobertura**.
O build falha automaticamente caso a cobertura esteja abaixo do limite.

```bash
mvn verify
```

Relatório HTML: `target/site/jacoco/index.html`

---

## Análise SonarQube (local)

### 1. Subir o servidor

```bash
docker-compose up -d
```

Aguarde ~1 minuto e acesse http://localhost:9000  
Login padrão: `admin` / `admin` (o SonarQube pedirá para trocar a senha no primeiro acesso)

### 2. Gerar um token de acesso

1. Acesse http://localhost:9000
2. Vá em **My Account → Security → Generate Token**
3. Copie o token gerado

### 3. Rodar a análise

```bash
mvn verify sonar:sonar -Dsonar.token=<seu-token>
```

O projeto aparecerá em http://localhost:9000/projects com nome **Person API**.

### 4. Parar o servidor (quando não usar)

```bash
docker-compose down
```

---

## Git hook — bloqueio automático no push

O projeto usa um `pre-push` hook que roda `mvn verify` antes de cada push.
Se a cobertura dos use cases estiver abaixo de **80%**, o push é bloqueado.

**Ativar na sua máquina (necessário apenas uma vez):**

```bash
git config core.hooksPath .githooks
```

> Para fazer push ignorando a verificação: `git push --no-verify`
