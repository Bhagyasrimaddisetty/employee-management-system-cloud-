# Cloud-Based Employee Management System

A cloud-ready Employee Management System built with **Spring Boot**, **MySQL**, **Docker**, and deployed on **AWS** with automated CI/CD via GitHub Actions.

## Features

- REST APIs for employee onboarding, role management, and record maintenance
- Department management
- Search and filter employees by department, role, status, or keyword
- Manager / direct-reports hierarchy
- Global exception handling with consistent API responses
- Input validation (Bean Validation)
- Health check endpoint for monitoring
- Swagger / OpenAPI documentation
- Dockerized application with multi-stage build
- CI/CD pipeline: build → test → push to ECR → deploy to ECS

## Tech Stack

| Layer        | Technology                  |
|--------------|------------------------------|
| Language     | Java 17                      |
| Framework    | Spring Boot 3.3 (Web, Data JPA, Validation, Actuator) |
| Database     | MySQL 8 (H2 for local/dev)   |
| Build Tool   | Maven                        |
| Container    | Docker / Docker Compose      |
| Cloud        | AWS (ECR, ECS)                |
| CI/CD        | GitHub Actions               |

## Project Structure

```
src/main/java/com/ems/employeemanagement
├── controller     # REST controllers
├── service        # Business logic
├── repository     # Spring Data JPA repositories
├── model          # JPA entities & enums
├── dto            # Request/response DTOs
├── exception      # Custom exceptions & global handler
└── config         # Configuration classes
```

## Running Locally (H2, no external DB needed)

```bash
mvn spring-boot:run
```

The app runs on `http://localhost:8080` with profile `dev` (H2 in-memory DB).
H2 console: `http://localhost:8080/h2-console`
Swagger UI: `http://localhost:8080/swagger-ui.html`

## Running with Docker Compose (MySQL + App)

```bash
docker-compose up --build
```

This starts MySQL and the application container, connected via the `prod` profile.

## Building the Docker Image Manually

```bash
docker build -t employee-management-system .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=<your-db-host> \
  -e DB_USERNAME=<user> \
  -e DB_PASSWORD=<password> \
  employee-management-system
```

## Environment Variables (prod profile)

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | MySQL host | `localhost` |
| `DB_PORT` | MySQL port | `3306` |
| `DB_NAME` | Database name | `employee_management` |
| `DB_USERNAME` | DB username | `root` |
| `DB_PASSWORD` | DB password | `password` |
| `SERVER_PORT` | App port | `8080` |

## API Endpoints

### Employees
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/employees` | Onboard a new employee |
| GET | `/api/v1/employees` | List all employees |
| GET | `/api/v1/employees/{id}` | Get employee by ID |
| GET | `/api/v1/employees/search?keyword=` | Search employees |
| GET | `/api/v1/employees/department/{departmentId}` | Employees by department |
| GET | `/api/v1/employees/role/{role}` | Employees by role |
| GET | `/api/v1/employees/status/{status}` | Employees by status |
| GET | `/api/v1/employees/{managerId}/direct-reports` | Direct reports of a manager |
| PUT | `/api/v1/employees/{id}` | Update employee record |
| PATCH | `/api/v1/employees/{id}/role` | Update employee role |
| PATCH | `/api/v1/employees/{id}/status?status=` | Update employee status |
| DELETE | `/api/v1/employees/{id}` | Delete employee |

### Departments
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/departments` | Create department |
| GET | `/api/v1/departments` | List departments |
| GET | `/api/v1/departments/{id}` | Get department by ID |
| PUT | `/api/v1/departments/{id}` | Update department |
| DELETE | `/api/v1/departments/{id}` | Delete department |

### Health
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/health` | Service health check |
| GET | `/actuator/health` | Spring Actuator health |

## CI/CD Pipeline

`.github/workflows/ci-cd.yml` defines a pipeline that:
1. Builds and runs tests with Maven on every push/PR
2. Builds a Docker image and pushes it to Amazon ECR (on `main`)
3. Updates the ECS task definition and deploys to the ECS service

### Required GitHub Secrets
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

Update `AWS_REGION`, `ECR_REPOSITORY`, `ECS_CLUSTER`, `ECS_SERVICE`, and `ECS_TASK_DEFINITION` in the workflow file to match your AWS setup.

## Testing

```bash
mvn test
```
