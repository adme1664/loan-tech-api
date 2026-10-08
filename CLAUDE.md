# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 4.1.1 REST API using Java 25, JPA/Hibernate, and PostgreSQL. The project is configured for traditional JVM deployment, containerized deployment, and GraalVM native image compilation.

## Build and Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean package

# Run the application (development profile)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Build native image
./mvnw -Pnative native:compile
```

### Testing
```bash
# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName
```

### Database
```bash
# Start PostgreSQL with Docker Compose
docker-compose up -d

# Stop database
docker-compose down

# View logs
docker-compose logs -f postgres
```

## Architecture

### Current State
The project is scaffolded but minimal - only the main application entry point exists. Ready for implementing standard Spring Boot layered architecture.

### Package Structure
Base package: `com.adme.learning.loan.tech.api`

Expected layers to implement:
- **Controllers**: REST endpoints (`@RestController`)
- **Services**: Business logic (`@Service`)
- **Repositories**: Data access (`@Repository`, extending JpaRepository)
- **Entities**: JPA models (`@Entity`)
- **DTOs**: Request/response objects

### Key Technologies
- **Spring Data JPA**: Database operations with Hibernate
- **Spring Web MVC**: REST API framework
- **Lombok**: Annotation processor for boilerplate code (getters/setters/constructors)
- **PostgreSQL**: Primary database

### Configuration

**Profiles**:
- Default: Minimal configuration (application.properties)
- `dev`: Development settings with create-drop schema (application-dev.properties)

**Development Profile Settings**:
- Port: 9091
- DDL Auto: create-drop (recreates schema on startup)
- SQL logging: enabled
- Open-in-view: true

**Database Environment Variables** (required for dev profile):
- `DB_HOST`: Database host (default: localhost)
- `DB_NAME`: Database name (default: dds-operational)
- `DB_USERNAME`: Database username (default: bigdata)
- `DB_PASSWORD`: Database password (default: bigdata)

### Maven Configuration

**Lombok Setup**: The compiler plugin is configured with annotation processor paths for both compile and test-compile phases. Lombok annotations will be processed correctly during builds.

**Hibernate Enhancement**: The hibernate-maven-plugin runs bytecode enhancement on entities during build.

**Native Image**: GraalVM native-maven-plugin is configured for AOT compilation support.

## Development Notes

- The dev profile uses `create-drop` which **destroys and recreates the database schema on every startup**. Use this for rapid development but switch to `update` or `validate` for persistent data.
- Environment variables for database connection are loaded from `.env` file (not committed to git).
- The compose.yaml defines a postgres service but uses different credentials than the .env file - ensure consistency when using Docker Compose.