## UserHub

UserHub is a RESTful API built with Spring Boot for managing 
users and their external projects...

## Tech Stack
- Java 21
- Spring Boot 3.5.14
- MySQL 8
- Flyway
- Docker & Docker Compose
- Prometheus
- Logback

## Running the project
docker compose up --build

## API Endpoints
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST   | /users | Create user | ❌ |
| GET    | /users/{id} | Get user | ✅ |
| PATCH  | /users/{id} | Update user | ✅ |
| DELETE | /users/{id} | Delete user | ✅ |
| POST   | /users/{id}/projects | Add project | ✅ |
| GET    | /users/{id}/projects | List projects | ✅ |
