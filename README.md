# 🧩 Mini Test — Two Spring Boot Apps with Postgres and Docker

**Goal:**  
Build and run two simple Spring Boot microservices (`auth-api` and `data-api`) that communicate with each other through Docker and share a PostgreSQL database.

---

## 🏗️ Architecture Overview

```
Client (Postman / curl)
        │
        ▼
 ┌──────────────────────────┐
 │        auth-api          │
 │  - /api/auth/register    │
 │  - /api/auth/login       │
 │  - /api/process          │
 │  - /api/health           │
 │  → connects to data-api  │
 │  → stores logs in DB     │
 └──────────────────────────┘
             │
             ▼
 ┌──────────────────────────┐
 │         data-api         │
 │  - /api/transform        │
 │  - /api/health           │
 │  ✅ validates X-Internal-Token
 │  🔄 transforms input text
 └──────────────────────────┘
             │
             ▼
       🐘 PostgreSQL
```

Both services run inside a shared Docker network and communicate via internal DNS:
```
auth-api → http://data-api:8081/api/transform
```

---

## ⚙️ Technologies

- **Spring Boot 3.5.6**
- **Java 17**
- **Spring Security + JWT**
- **Spring Data JPA (Postgres)**
- **Flyway migrations**
- **Docker & Docker Compose**
- **RestTemplate (service-to-service calls)**

---

## 📦 Project Structure

```
/auth-api
  ├── src/main/java/authapi/servicea/...
  ├── src/main/resources/db/migration/V1__init.sql
  └── Dockerfile

/data-api
  ├── src/main/java/dataapi/serviceb/...
  ├── src/main/resources/application.yml
  └── Dockerfile

/.env
/docker-compose.yml
/README.md
```

---

## 🌍 Environment Variables (`.env`)

```bash
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=mini_test_db
POSTGRES_PORT=5432

JWT_SECRET=mysecretjwtkey
INTERNAL_TOKEN=supersecrettoken

SPRING_PROFILES_ACTIVE=default
```

---

## 🚀 Run Instructions

### 1️⃣ Build both services
```bash
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### 2️⃣ Start everything via Docker
```bash
docker compose up -d --build
```

This will launch:
- `auth-api` → http://localhost:8080  
- `data-api` → http://localhost:8081  
- `postgres` → internal only

---

## 🧠 API Reference

### 🩵 Auth API (`localhost:8080`)

#### 1. Register
```bash
POST /api/auth/register
Content-Type: application/json

{
  "email": "test2@test.com",
  "password": "password123",
  "repeatPassword": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```
Response: `200 Ok`

---

#### 2. Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "test2@test.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

#### 3. Process Text
```bash
POST /api/process
Authorization: Bearer <token>
Content-Type: application/json

{
  "text": "hello world"
}
```

➡ Calls **data-api**  
➡ Stores result in `processing_log`

Response example:
```json
{
  "result": "DLROW OLLEH"
}
```

---

#### 4. Health Check
```bash
GET /api/health
```
Response:
```
200 OK
Authentication API is up and healthy✅
```

---

### 🧩 Data API (`localhost:8081`)

#### Transform Text
```bash
POST /api/transform
X-Internal-Token: supersecrettoken
Content-Type: application/json

{
  "text": "test"
}
```

Response:
```json
{
  "result": "TSET"
}
```

If the token is invalid:
```
403 Forbidden
```

#### Health Check
```bash
GET /api/health
```
Response:
```
200 OK
Data API is up and healthy✅
```

---

## 🗄️ Database Schema

| Table | Columns |
|-------|----------|
| **users** | id (UUID, PK), email (unique), password_hash |
| **processing_log** | id (UUID, PK), user_id (FK), input_text, output_text, created_at |

Created automatically by Flyway migration file:
```
auth-api/src/main/resources/db/migration/V1__init.sql
```

---

## 🩺 Docker Health Checks

Both services include a `/api/health` endpoint.  
In `docker-compose.yml` you can add:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/api/health"]
  interval: 30s
  timeout: 5s
  retries: 3
```

and analogously for `data-api` with port `8081`.

This allows Docker to automatically monitor service health.

---

## 🔍 Useful Commands

Check logs:
```bash
docker compose logs -f auth-api
docker compose logs -f data-api
```

Restart containers:
```bash
docker compose down
docker compose up -d
```

Connect to DB (inside container):
```bash
docker exec -it postgres psql -U postgres -d mini_test_db
```

---

## ✅ Acceptance Criteria

| Requirement | Status |
|--------------|---------|
| Register/Login via JWT | ✅ Done |
| Protected `/api/process` | ✅ Done |
| Inter-service call (`auth-api → data-api`) | ✅ Done |
| PostgreSQL persistence | ✅ Done |
| Docker Compose orchestration | ✅ Done |
| `/health` endpoints for both | ✅ Done |
| Flyway DB migrations | ✅ Done |
| Secure internal token validation | ✅ Done |

---

## 🧩 Notes

- All sensitive tokens/secrets are stored in `.env`
- Passwords hashed using **BCrypt**
- `data-api` accepts only requests from `auth-api` via `X-Internal-Token`
- Both apps use `/api/health` for monitoring (and can be used in Docker health checks)
- Compatible with PostgreSQL 15+
- Simple, production-like microservice setup

---

## 🧑‍💻 Example End-to-End Test Flow

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register   -H "Content-Type: application/json"   -d '{"email":"a@a.com","password":"pass"}'

# Login (get JWT)
curl -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d '{"email":"a@a.com","password":"pass"}'

# Process text using token
curl -X POST http://localhost:8080/api/process   -H "Authorization: Bearer <token>"   -H "Content-Type: application/json"   -d '{"text":"hello"}'

# Check logs in DB (processing_log table)
```

---

## 🧱 Author

Developed with ❤️ by Oleksandr Chernetskyi  
📧 Email: oleksandr0chernetskyi@gmail.com

📅 October 2025

---
