# Banking Service

A comprehensive Spring Boot-based banking service application for managing customer accounts and financial transactions. This service provides RESTful APIs for account creation, transaction management, and service health monitoring.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Building the Project](#building-the-project)
- [Running Tests](#running-tests)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Docker Deployment](#docker-deployment)
- [Project Structure](#project-structure)

---

## Project Overview

Banking Service is a fintech application built with Spring Boot that manages:
- **Customer Accounts**: Create and retrieve customer bank accounts
- **Financial Transactions**: Record and manage account transactions (debit/credit operations)
- **Operation Types**: Support for multiple transaction types including cash purchases, installment purchases, withdrawals, and payments
- **Service Health Monitoring**: Built-in health check endpoints

The application uses PostgreSQL as the database and provides comprehensive API documentation through Swagger/OpenAPI.

---

## Tech Stack

| Component | Technology                                 | Version |
|-----------|--------------------------------------------|--------|
| **Framework** | Spring Boot                                | 4.0.0  |
| **Language** | Java                                       | 21     |
| **Database** | PostgreSQL                                 | 18     |
| **Build Tool** | Maven                                      | 3.9.2  |
| **ORM** | Spring Data JPA                            | 4.0.0  |
| **Security** | Spring Security                            | 4.0.0  |
| **API Documentation** | Springdoc OpenAPI (Swagger)                | 3.0.0  |
| **Validation** | Spring Validation (Jakarta)                | 4.0.0  |
| **Logging** | Logback                                    | 1.5.21 |
| **Utilities** | Lombok                                     | 1.18.42 |
| **Testing** | Spring Boot Test (JUnit, Mockito, AssertJ) | 4.0.0  |

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
  - Download from: https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version`

- **Maven 3.9.2** or higher
  - Download from: https://maven.apache.org/download.cgi
  - Verify: `mvn -version`

- **PostgreSQL 18** or higher
  - Download from: https://www.postgresql.org/download/
  - Verify: `psql --version`

- **Git** (optional, for cloning)
  - Download from: https://git-scm.com/

---

## Project Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd BankingService
```

### 2. Verify Project Structure

```bash
ls -la
```

You should see:
- `pom.xml` - Maven configuration
- `src/` - Source code directory
- `Dockerfile` - Docker configuration
- `README.md` - This file

### 3. Install Dependencies

The dependencies will be automatically downloaded when you build the project. Key dependencies include:
- Spring Boot Starters (Web, Security, JPA, Validation)
- PostgreSQL Driver
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Testing libraries

---

## Database Setup

### Step 1: Install PostgreSQL

If not already installed, download and install PostgreSQL from https://www.postgresql.org/download/

### Step 2: Create Database and User

Open PostgreSQL command line or use pgAdmin:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE banking_db;

# Create user
CREATE USER ndk1996 WITH PASSWORD 'localpassword@999';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE banking_db TO ndk1996;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ndk1996;

# Exit PostgreSQL
\q
```

### Step 3: Verify Connection

```bash
psql -U ndk1996 -d banking_db -h localhost
```

### Database Schema
Refer to following file to see the database schema and entity relationships: [schema.sql](src/main/resources/schema.sql)

#### Initial Data
The operation_types table needs to be pre-populated. 
Refer to following file: [data.sql](src/main/resources/data.sql) and run the insert queries.

---

## Running the Application

### Option 1: Run Using Maven

```bash
# Navigate to project directory
cd /Users/ndk-1996/Projects/BankingService

# Run the application
mvn spring-boot:run
```

### Option 2: Run Using JAR File

```bash
# Build the project first
mvn clean package

# Run the JAR
java -jar target/banking-service-1.0.0-SNAPSHOT.jar
```

### Option 3: Run from IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Right-click on `BankingServiceApplication.java`
3. Select "Run" or "Debug"

### Verify Application is Running

The application starts on `http://localhost:8080`

Check health status:
```bash
curl http://localhost:8080/
```

Expected response:
```
Service is up and running
```

---

### Security

The application includes Spring Security with basic authentication:

**Default Credentials**:
- Username: `user@fintech.banking.com`
- Password: `password@fintech.banking.com`

You can modify find these in the `application.yml` and modify it as per your need.

To access protected endpoints, provide these credentials in the login screen, which comes when you hit the localhost url for first time after running the application.

---

## Building the Project

### Clean Build

```bash
mvn clean build
```

### Build Without Running Tests

```bash
mvn clean package -DskipTests
```

### Build With All Tests

```bash
mvn clean package
```

### Build Output

The generated JAR file will be located at:
```
target/banking-service-1.0.0-SNAPSHOT.jar
```

---

## Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=AccountControllerTest
```

### Run Tests with Coverage Report

```bash
mvn clean test
```

### Test Structure

Tests are organized in `src/test/java/com/banking/fintech/` with the following categories:
- **Controller Tests**: API endpoint testing
- **Service Tests**: Business logic testing
- **Validator Tests**: Input validation testing

---

## API Documentation

### Access Swagger UI

Once the application is running, visit:

```
http://localhost:8080/swagger-ui.html
```

### Access OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

The Swagger UI provides interactive API documentation where you can:
- View all endpoints
- Test API calls directly
- See request/response schemas
- Understand authentication requirements

---

## Docker Deployment

### Build Docker Image

```bash
docker build -t banking-service:1.0.0 .
```

### Run Docker Container

```bash
docker run -d \
  --name banking-service \
  -p 8080:8080 \
  --env-file .env \
  banking-service:1.0.0
```

### Run with Docker Compose:
```bash
docker-compose up
```

---

## Application Configuration

Key configuration properties in `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_db
    username: ndk1996
    password: localpassword@999
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

Modify these settings as needed for your environment.

---

## Development Notes

- **Java Version**: The project targets Java 21. Ensure your environment uses Java 21.
- **Maven**: Maven 3.9.2 or higher is required.
- **IDE Recommendation**: IntelliJ IDEA Community Edition or VS Code with Java extensions.
- **Lombok**: The project uses Lombok for reducing boilerplate code. Enable annotation processing in your IDE.

---
