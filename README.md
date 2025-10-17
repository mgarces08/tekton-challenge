# Tekton Challenge

Backend for the Tekton Challenge. Includes:
- **Spring Boot 3** (Java 21)
- **Spring Data JPA** + **PostgreSQL**
- **Flyway** for database migrations
- **Cache** with **Ehcache 3** (via JCache)
- **Swagger/OpenAPI** using **springdoc**
- **Asynchronous call history**
- Profiles: `dev` (local/compose) and `prod`

---

## 🚀 Main Endpoints

- **Dynamic percentage calculation**  
  `POST /api/calc?num1=10&num2=20`  
  Response:
  ```json
  { "sum": 30, "percentage": 15, "total": 34.50 }
  ```

- **History of calls**  
  `GET /api/history`

- **Process failed asynchronous tasks**  
  `POST /api/task/process`

- **Actuator**  
  `GET /actuator/health` – service health status  
  `GET /actuator/info` – build info and metadata

---

## 📜 Swagger / OpenAPI (only in `dev`)

With `springdoc-openapi-starter-webmvc-ui`, **Swagger UI** is available at:
- `http://localhost:8080/swagger-ui.html`
- Spec JSON: `http://localhost:8080/v3/api-docs`

---

## 🧰 Requirements

- **Java 21**
- **Docker** and **Docker Compose**
- **Maven 3.9+**

---

## 🧪 Profiles and Environment Variables

Profiles: `dev` and `prod`.

Common variables:
- `DB_HOST` (default: `localhost` outside Docker; `db` inside Compose)
- `DB_PORT` (default: `5432` or the mapped port)
- `DB_NAME` (default: `challenge_dev`)
- `DB_USER` / `DB_PASS`

Example **IntelliJ Run Configuration** (Environment variables):
```
SPRING_PROFILES_ACTIVE=dev;DB_HOST=localhost;DB_PORT=5432;DB_NAME=challenge_dev;DB_USER=dev;DB_PASS=dev
```

---

## 🐳 Running with Docker Compose

1) **Start only the database**
```bash
docker compose -f docker-compose.dev.yml up -d db
```

2) **Start app + db** (multi-stage build):
```bash
docker compose -f docker-compose.dev.yml up --build
```

> The `app` service uses a **multi-stage Dockerfile** that packages the JAR inside Docker.  
> If you prefer using your local JAR, run `mvn -DskipTests package` first and use a simple Dockerfile with `COPY target/*.jar`.

---

## 🗄️ Database and Migrations (Flyway)

Migrations live in `src/main/resources/db/migration/`.

At startup, **Flyway** runs **before** JPA.  
If you change the schema, create a new file like `V2__...sql`.

---

## ⚡ Cache (Ehcache 3 + JCache)

- Alias `percentage` with **30-minute TTL** to store the “last known good” percentage value.
- Configured programmatically using `JCacheManagerCustomizer`.
- Fallback: if the external service fails, the cached value is used; if none exists, an error is returned.

---

## 🧪 Quick Tests (curl)

```bash
# calculate with percentage
curl --location 'localhost:8080/api/calculate' \
--header 'Content-Type: application/json' \
--data '{
    "num1": 12,
    "num2": 3
}'

# view history
curl "http://localhost:8080/api/history"

# Process failed async tasks
curl --location --request POST 'localhost:8080/api/task/process'

# health check
curl "http://localhost:8080/actuator/health"
```

---

## 📦 Manual Build

```bash
mvn -DskipTests clean package
java -jar target/*.jar --spring.profiles.active=dev
```

---

## 📚 Project Structure

```
src/main/java/com/tekton/challenge/
  config/        # Cache, OpenAPI, Async, etc.
  domain/        # JPA Entities
  repository/    # Spring Data Repositories
  service/       # Services
  web/           # REST Controllers

src/main/resources/
  db/migration/  # Flyway SQL scripts
```

That’s it!  
Open `http://localhost:8080/swagger-ui.html` to explore the API.
