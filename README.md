# jooq-example

A simple Spring Boot project using jOOQ for type-safe SQL query construction and database management in Java.

## Overview
This project demonstrates the integration of Spring Boot and jOOQ. It serves as a clean and minimal example for developers looking to explore or bootstrap applications that rely on jOOQ for database interaction with Java. The project likely uses Maven for build management, includes a docker-compose.yml for spinning up database dependencies easily, and integrates jOOQ as the core database access layer.

## Key Features

- Spring Boot: Rapid application development and management.
- jOOQ: Type-safe, fluent SQL query building directly in Java code.
- Docker Compose: Fast setup of database environment.
- Maven Wrapper: Consistent build environment without locally installing Maven.

## Getting Started

- Prerequisites
  - Java (version as specified in the project's pom.xml, commonly 11+)
  - Docker & Docker Compose
  - Git

## Setup Instructions

- Clone the Repository

```declarative
git clone https://github.com/alanensina/jooq-example.git
cd jooq-example
```

- Start the Database
  - Start the database container (PostgreSQL, as configured in docker-compose.yml):

```declarative
docker-compose up -d
```
This brings up the database service as defined in the project.

- Build the Project
  - Use the Maven Wrapper to compile the code and resolve dependencies:

```declarative
./mvnw spring-boot:run
```
This will start the Spring Boot application, which uses jOOQ for SQL operations.

## Accessing the Application

- Available at http://localhost:8080.
- Review controllers/services in src/main/java to discover available API endpoints.

## How It Works
- jOOQ generates Java classes from your database schema.
- Spring Boot manages the application lifecycle.
- The application leverages jOOQ to perform SQL queries and CRUD operations in a type-safe manner, directly via Java methods instead of raw SQL.
- Docker Compose ensures the required database is available and ready to go for local development and testing.

