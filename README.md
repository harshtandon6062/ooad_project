# InsureFlow — Insurance Workflow Automation Software

Java Spring Boot application for insurance policy, quote, and claim management.

## Quick Start

### Prerequisites
- Java 17
- Maven

### Run locally
```bash
mvn clean package
mvn spring-boot:run
```

Open http://localhost:8081

> If port 8080 is already in use, start with `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.

## Demo Credentials

| Role | Email | Password |
|---|---|---|
| Admin | admin@insureflow.com | admin123 |
| Customer | john@example.com | password123 |
| Agent | priya@example.com | password123 |
| Claims Adjuster | raj@example.com | password123 |

## Architecture

Spring Boot MVC architecture with a Thymeleaf frontend and JPA persistence.

```
Browser → Thymeleaf templates → Controller → Service → Repository → H2 database
```

## Tech Stack

- Java 17
- Spring Boot 3.1.0
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- H2 in-memory database
- Maven

## Project Structure

- `src/main/java/com/example/insureflow/`
  - `config/` — security and app configuration
  - `controller/` — MVC controllers and route handlers
  - `service/` — business logic and validation
  - `repository/` — JPA repositories
  - `model/` — entity classes, enums, and domain models
  - `patterns/` — design pattern implementations
- `src/main/resources/templates/` — Thymeleaf views
- `src/main/resources/application.properties` — Spring Boot configuration

## Key Features

- Role-based login and authorization
- Customer policy and claim management
- Claims adjuster review workflow
- Thymeleaf view rendering
- H2 database persistence with seeded demo data
- Global exception handling and validation

## Useful Commands

- Compile: `mvn compile`
- Run tests: `mvn test`
- Package jar: `mvn package`
- Run app: `mvn spring-boot:run`

## Notes

- The app uses an in-memory H2 database, so seeded demo data resets on each restart.
- If you need a different port, pass `--server.port=<port>` as an application argument.
