# Task Tracker REST API

A full-featured task tracking system built with **Spring Boot**, designed to manage tasks, users, and their statuses in a role-secured environment. It supports caching via Redis, email notifications for overdue tasks and user summaries, JWT authentication, and Dockerized deployment for production-readiness.

---

## 🚀 Features

### ✅ Core Functionality

* **CRUD for Users and Tasks**
* **Role-Based Access Control (RBAC)** — via Spring Security & JWT
* **Authentication Endpoint** (`POST /api/auth/login`) returns JWT tokens
* **Task Filtering** by due date, status, and assigned user
* **Redis Caching** for fast retrieval of task and user data
* **Email Notifications**

  * Admin-triggered overdue task checks
  * Summary emails per user
* **Docker Support** for seamless containerized deployment

### 📦 Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security + JWT
* Spring Cache + Redis
* H2 Database (in-memory)
* Jakarta Validation
* Swagger (SpringDoc OpenAPI)
* Docker + Docker Compose
* Flyway (placeholder, not used in current version)

---

## 📂 Project Structure Overview

```
├── config
|    ├── RedisConfig
|    ├── RedisTestRunner
|    ├── SecurityConfig
├── controller
│   ├── AuthController
│   ├── UserController
│   ├── TaskController
│   └── EmailController
├── dto
|    ├── AuthRequest
|    ├── AuthReponse
|    ├── TaskRequestDTO
|    ├── TaskResponseDTO
|    ├── UserRequestDTO
|    ├── UserResponseDTO
|    ├── UserTaskDTO
├── entity
|    ├── Task
|    ├── User
├── exception
|    ├── GlobalExceptionHandler
├── repository
|    ├── TaskRepository
|    ├── UserRepository
├── scheduler
|    ├── OverdueTaskNotifier
|    ├── OverdueTaskScheduler
├── security
|    ├── JwtAuthFilter
|    ├── JwtUtil
├── service
|    ├── CustomUserDetailsService
|    ├── EmailService
|    ├── TaskService
|    ├── TaskServiceImpl
|    ├── UserService
|    ├── UserServiceImpl
```

---

## 🧪 API Endpoints Overview

### 🔐 Authentication

```
POST /api/auth/login
```

**Request Body:**

```json
{
  "@class": "com.sgs.tasktracker.dto.AuthRequest",
  "username": "lindt",
  "password": "MuchToLose#1"
}
```

**Returns:** JWT token to add on Headers tab -> Authorization(key) with "Bearer <token>"(value)

### 👤 Users

```
GET    /api/users
GET    /api/users/{id}
POST   /api/users 			#Sample data (2 users exist already, submit on endpoint thrice for it to work for the first time)

Body
{
    "@class": "com.sgs.tasktracker.dto.UserRequestDTO",
    "username": "roney",
    "password": "MuchToLose#3",
    "email": "roney@gmail.com",
    "role": "User"
}

PUT    /api/users/{id}		      # Samee body as above
DELETE /api/users/{id}
GET    /api/users/{id}/tasks          # Get user + tasks
GET    /api/users/redis-test          # Test Redis connection
```

### ✅ Tasks

```
GET    /api/tasks                     # With optional filters (?status=(Task.STATUS) / dueDate=20250620 / assignedUserId=1)
GET    /api/tasks/{id}
POST   /api/tasks		      #Sample data (2 tasks exist already, submit on endpoint thrice for it to work for the first time)

Body
{
    "@class": "com.sgs.tasktracker.dto.TaskRequestDTO",
    "title": "Listen to Wheezy for the second time.",
    "description": "Judge Tha Carter VI, just for vibes.",
    "status": "In_Progress",
    "dueDate": "2025-06-12",
    "assignedUserId": 2
}

PUT    /api/tasks/{id}		      # Body used above, edited
PUT    /api/tasks/{id}/{status}       # Quick status update (NEW/IN_PROGRESS/DELAYED etc)
DELETE /api/tasks/{id}
POST   /api/tasks/trigger-overdue     # Admin: mark tasks overdue
```

### 📧 Emails

```
POST /api/email/overdue               # Admin-only access: notify overdue tasks
POST /api/email/summary/{userId}      # Admin/User: send summary to user
```

---

## 🐳 Docker Deployment

### Prerequisites

* Docker
* Docker Compose

### Build and Run

From the project root:

```bash
docker-compose up --build
```

### Access

* Base App URL: `http://localhost:8080`
* H2 Console: `http://localhost:8080/h2-console`

> **Note**: Redis and the Spring Boot app share a Docker network to enable inter-container communication.

---

## ⚙️ Manual Setup (Without Docker)

### 1. Clone Repo

```bash
git clone https://github.com/LyndtBravos/task-tracker.git
cd task-tracker
```

### 2. Set Environment Variables (optional for Redis)

```bash
export SPRING_PROFILES_ACTIVE=default
```

### 3. Run Redis Locally (optional)

```bash
docker run -p 6379:6379 redis:7.2-alpine
```

### 4. Run App

```bash
./mvnw spring-boot:run
```

---

## 🧪 Sample Auth & Data Seeding

To test the API manually, create users and tasks via the `/api/users` and `/api/tasks` endpoints.

Use `POST /api/auth/login` to get a token and authorize requests by setting:

```
Authorization: Bearer <your-token>
```

``` Sample data
{
     "@class": "com.sgs.tasktracker.dto.AuthRequest",
     "username": "lindt",
     "password": "MuchToLose#1"
}
---

## 🧠 What Could Be Added With More Time

> These features were considered but not prioritized due to time constraints:

1. **Integration Tests** — Full coverage for API endpoints
2. **Swagger UI Enhancements** — Include examples & schemas
3. **PostgreSQL Support** — Switch from in-memory DB to production-ready DB.
4. **React UI** — To preview all this functionality via forms etc.
5. **Rate Limiting** — Avoid brute force login attempts
6. **Redis TTL Policies** — Cache eviction, smarter key handling
7. **Task Assignment History** — Log who assigned what task to whom
8. **Notification Logs** — Store email dispatch history per user

---

## 📄 License

MIT

---

## 💬 Contact

For any questions, reach out via GitHub or email.

---

Made with ❤️ by Brian.
