# Docker Setup Guide for Banking Service

## Prerequisites
- Docker installed on your system
- Docker Compose installed (for running with PostgreSQL)

## Building the Docker Image

### Option 1: Using Docker Compose (Recommended)
This approach automatically sets up both the Banking Service and PostgreSQL database.

```bash
# Navigate to the project directory
cd /Users/ndk-1996/Projects/BankingService

# Build and start all services
docker-compose up --build

# View logs
docker-compose logs -f banking-service

# Stop services
docker-compose down

# Stop services and remove volumes
docker-compose down -v
```

### Option 2: Build and Run Manually

#### Step 1: Build the Docker Image
```bash
cd /Users/ndk-1996/Projects/BankingService
docker build -t banking-service:1.0.0 .
```

#### Step 2: Start PostgreSQL Container
```bash
docker run -d \
  --name banking-db \
  -e POSTGRES_USER=ndk1996 \
  -e POSTGRES_PASSWORD=localpassword@999 \
  -e POSTGRES_DB=banking_db \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:16-alpine
```

#### Step 3: Wait for PostgreSQL to be Ready
```bash
docker exec banking-db pg_isready -U ndk1996
```

#### Step 4: Initialize Database Schema (if needed)
```bash
docker exec -i banking-db psql -U ndk1996 -d banking_db < src/main/resources/schema.sql
docker exec -i banking-db psql -U ndk1996 -d banking_db < src/main/resources/data.sql
```

#### Step 5: Run the Banking Service
```bash
docker run -d \
  --name banking-service \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://banking-db:5432/banking_db \
  -e SPRING_DATASOURCE_USERNAME=ndk1996 \
  -e SPRING_DATASOURCE_PASSWORD=localpassword@999 \
  --link banking-db:postgres \
  banking-service:1.0.0
```

## Accessing the Application

Once running, you can access:
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

## Useful Docker Commands

### View logs
```bash
docker logs banking-service
docker logs -f banking-service  # Follow logs
```

### View running containers
```bash
docker ps
```

### Stop containers
```bash
docker stop banking-service banking-db
```

### Remove containers
```bash
docker rm banking-service banking-db
```

### Remove images
```bash
docker rmi banking-service:1.0.0 postgres:16-alpine
```

### View container details
```bash
docker inspect banking-service
```

## Dockerfile Explanation

The Dockerfile uses a multi-stage build process:

1. **Build Stage**: Uses Maven with Eclipse Temurin JDK 21 to compile and package the application
2. **Runtime Stage**: Uses Eclipse Temurin JDK 21 base image for running the application
3. **Security**: Runs the application with a non-root user (`appuser`)
4. **Health Check**: Monitors the application health every 30 seconds
5. **Size Optimization**: Only the compiled JAR is copied to the runtime image, reducing the final image size

## Docker Compose Explanation

The `docker-compose.yml` file defines:
- **PostgreSQL Service**: Database container with volumes for persistence
- **Banking Service**: Application container that depends on the database
- **Network**: Isolated network for inter-container communication
- **Health Checks**: Ensures services are running correctly before starting dependent services

## Troubleshooting

### Port Already in Use
If port 8080 or 5432 is already in use:
```bash
# Find and stop containers using the port
docker ps
docker stop <container_id>

# Or use different ports in docker-compose.yml or docker run commands
```

### Database Connection Issues
- Ensure PostgreSQL container is running: `docker ps`
- Check logs: `docker logs banking-db`
- Verify network connectivity: `docker network inspect banking-network` (for compose)

### Permission Issues
If you get permission errors, ensure Docker daemon is running and you have appropriate permissions.

## Environment Variables

The following environment variables can be customized:

- `SPRING_DATASOURCE_URL`: Database URL (default: jdbc:postgresql://localhost:5432/banking_db)
- `SPRING_DATASOURCE_USERNAME`: Database username (default: ndk1996)
- `SPRING_DATASOURCE_PASSWORD`: Database password (default: localpassword@999)
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL strategy (default: validate)

## Notes

- The Dockerfile skips tests during build with `-DskipTests` for faster builds
- PostgreSQL data is persisted in a Docker volume (`postgres_data`)
- The application waits for PostgreSQL to be healthy before starting
- The container runs with a non-root user for security best practices

