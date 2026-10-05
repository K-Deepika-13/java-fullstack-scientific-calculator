# Scientific Calculator - Java Full Stack

A full-stack scientific calculator built to demonstrate Java, Spring Boot, REST APIs, JavaScript, and MySQL.

## Technology Stack

- Frontend: HTML, CSS, JavaScript
- Backend: Java 17, Spring Boot, Spring Web
- Persistence: Spring Data JPA / Hibernate
- Database: MySQL
- Build tool: Maven
- Testing/API tool: Postman

## Features

- Addition, subtraction, multiplication and division
- Percentage
- Power
- Parentheses
- Square root
- sin, cos, tan (degrees)
- log, ln and absolute value
- Calculation history
- Clear history
- REST API
- MySQL persistence

## Project Structure

```text
scientific-calculator-java-fullstack/
├── frontend/
│   └── index.html
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/calculator/
│       │   ├── CalculatorApplication.java
│       │   ├── controller/
│       │   ├── model/
│       │   ├── repository/
│       │   └── service/
│       └── resources/
│           └── application.properties
├── schema.sql
├── .gitignore
└── README.md
```

## Requirements

Install:

1. JDK 17 or later
2. MySQL Server + MySQL Workbench
3. Maven 3.9+ (or use Maven Wrapper if you add one)
4. VS Code or IntelliJ IDEA
5. A browser

## Database Setup

Open MySQL Workbench and run:

```sql
CREATE DATABASE IF NOT EXISTS calculator_db;
```

Then open:

`backend/src/main/resources/application.properties`

Change:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

to your actual MySQL password.

The application uses JPA/Hibernate with `ddl-auto=update`, so the `calculations` table is created automatically when the backend starts.

## Run the Backend

Open PowerShell in:

```text
scientific-calculator-java-fullstack/backend
```

Run:

```powershell
mvn spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

## Run the Frontend

The frontend is a static HTML page and should be served by a small local web server.

If VS Code is installed, install the **Live Server** extension and open `frontend/index.html` with Live Server.

Alternatively, from the `frontend` folder, if Python is installed:

```powershell
python -m http.server 5500
```

Then open:

```text
http://localhost:5500
```

The frontend sends requests to the Spring Boot API at port 8080.

## API Endpoints

### Health

```text
GET /api/health
```

### Calculate

```text
POST /api/calculate
Content-Type: application/json

{
  "expression": "sqrt(144) + 5^2"
}
```

### Get history

```text
GET /api/calculations
```

### Save calculation

```text
POST /api/calculations
Content-Type: application/json

{
  "expression": "25 + 30"
}
```

### Clear history

```text
DELETE /api/calculations
```

## Resume Description

**Scientific Calculator | Java, Spring Boot, JavaScript, MySQL**

Developed a full-stack scientific calculator using Java Spring Boot REST APIs, JavaScript, and MySQL. Implemented arithmetic and scientific operations, calculation history, REST API integration, and persistent storage using Spring Data JPA.

## Interview Explanation

The project demonstrates how a Java backend communicates with a web frontend through REST APIs. The Spring Boot service evaluates expressions, while Spring Data JPA stores calculation history in MySQL. The frontend provides the calculator interface and sends calculation requests to the backend.
