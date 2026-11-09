# AssetFlow — Asset Management System

AssetFlow is a robust, enterprise-grade Asset Management System built with Spring Boot, Thymeleaf, and PostgreSQL. It provides full lifecycle tracking of company hardware, employee assignments, and historical audit logs with role-based access control.

## Tech Stack

* **Backend:** Java 25, Spring Boot 4.0.0

    * **Frameworks:** Spring Data JPA, Spring Security, Spring MVC

    * **Database:** PostgreSQL with Hibernate ORM

    * **Utilities:** Lombok, Jakarta Validation

* **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 5.3

* **Build Tool:** Maven

## Features

* **Employee Management:**

    * Create, read, update, and manage employee records.

    * Active/Inactive status lifecycle tracking.

    * Safeguarded deletion logic to preserve asset audit history.

* **Role-Based Access Control (RBAC):**

    * **ROLE_ADMIN:** Full access to CRUD operations across all modules.

    * **ROLE_USER:** Read-only access to directory and assigned assets.

* **Audit & History Tracking:**

    * Database-backed tracking of asset assignments and transfers using `@OneToMany` relationships.

* **Security:**

    * Session and token handling via Spring Security.

    * Global Exception Handling with custom alert feedback.

## Getting Started

### Prerequisites

* Java OpenJDK 21 or higher

* Maven 3.8+

* PostgreSQL 15+

### Database Configuration

1. Create a PostgreSQL database named `assettracker`:

   ```
   CREATE DATABASE assettracker;
   
   ```

2. Update `src/main/resources/application.properties` (or `application.yml`) with your local database credentials:

   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/assettracker
   spring.datasource.username=your_postgres_user
   spring.datasource.password=your_postgres_password
   spring.jpa.hibernate.ddl-auto=update
   
   ```

### Running the Application

1. Clone the repository:

   ```
   git clone https://github.com/ColeKenneth/asset-tracker.git
   cd assettracker
   
   ```

2. Build and run with Maven:

   ```
   mvn clean spring-boot:run
   
   ```

3. Open your browser and navigate to:

   ```
   http://localhost:8080/employees
   
   ```

## Project Structure

```
assetflow/
 ├── src/main/java/com/example/assettracker/
 │    ├── controller/          # MVC Controllers
 │    ├── domain/              # JPA Entities & Enums
 │    ├── dtos/                # Data Transfer Objects
 │    ├── exception/           # Custom Exceptions & Global Handlers
 │    ├── repository/          # Spring Data JPA Repositories
 │    ├── security/            # Security & JWT Configuration
 │    └── service/             # Business Logic & Implementations
 └── src/main/resources/
      ├── templates/           # Thymeleaf Templates & Fragments
      └── application.properties

```