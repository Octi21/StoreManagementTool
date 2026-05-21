# Store Management API

A REST API for managing a small store: products, categories, users, and a price-change audit log. Built with Spring Boot, H2 in-memory database, and HTTP Basic authentication.

## Stack

- Java 21
- Spring Boot 3.5 (Web, Data JPA, Security, Validation)
- H2 (in-memory)
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito + AssertJ

## Run

```bash
mvn clean install
mvn spring-boot:run
```


The app starts on `http://localhost:8080`. The H2 database is in-memory and resets on every restart, with seed data loaded from `data.sql`.

## Run tests

```bash
mvn test
```

## Useful URLs

| URL | Purpose |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI — try endpoints in the browser |
| `http://localhost:8080/h2-console` | H2 web console (JDBC URL: `jdbc:h2:mem:storedb`, user `sa`, no password) |

## Authentication


| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `manager` | `manager123` | MANAGER |
| `user` | `user123` | USER |


## Role permissions

| Action | USER | MANAGER | ADMIN |
|---|---|---|---|
| Read products & categories | ✓ | ✓ | ✓ |
| Create / update products, change price, adjust stock | | ✓ | ✓ |
| View price history | | ✓ | ✓ |
| Delete products | | | ✓ |
| Manage categories (create / update / delete) | | | ✓ |
| Manage users | | | ✓ |

## Endpoints

### Products

- `GET /api/products`
- `GET /api/products/{id}`
- `GET /api/products/{id}/price-history`
- `POST /api/products`
- `PUT /api/products/{id}`
- `PATCH /api/products/{id}/price`
- `PATCH /api/products/{id}/stock`
- `DELETE /api/products/{id}`

### Categories

- `GET /api/categories`
- `GET /api/categories/{id}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Users (admin only)

- `GET /api/users`
- `POST /api/users`
- `DELETE /api/users/{id}`

### Auth

- `GET /api/auth/me` — info about the currently authenticated user.


## Project structure

```
src/main/java/com/store/api/
├── config/          SecurityConfig, OpenApiConfig
├── controller/      REST controllers
├── dto/
│   ├── request/     Validated input DTOs (records)
│   └── response/    Output DTOs + ErrorResponse
├── entity/          JPA entities
├── exception/       Custom exceptions + GlobalExceptionHandler
├── mapper/          Entity → DTO mapping
├── repository/      Spring Data JPA repositories
├── security/        UserDetailsService
└── service/         Business logic, transactions
```

