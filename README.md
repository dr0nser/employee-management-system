# Employee Management System
This is an employee management web application that features a well-designed UI and robust CRUD functionalities along with authentication and role-based authorization for employees.

[Live Link 👉](https://ems.up.railway.app/)

## Features
1. Authentication
2. Role-based authorization
3. Adding New Employees
4. Update Existing Employee Details
5. Removing Employees
6. Searching Employees
7. Pagination

## Tech Stack
1. Spring Boot
2. Spring Data JPA
3. PostgreSQL
4. Spring Security

# How to clone application in your local machine?
## Prerequisites
1. Git
2. PostgreSQL
3. Any IDE

## Steps
1. Run `git clone https://github.com/whyucode/employee-management-system.git` in your terminal.
2. Open the project in your IDE of choice.
3. Navigate to `ems/src/resources/application.properties` and set the property `spring.profiles.active=dev`
4. Similarly, navigate to `ems/src/resources/application-dev.properties` and set the properties as below:
   1. `spring.datasource.url`: Set this with the PORT number of pgAdmin (`5432` is the default) and provide database name of your choice(here, its `ems`)
   2. `spring.datasource.username`: This is your pgAdmin profile username(default is `postgres`)
   3. `spring.datasource.password`: This is the root password you provided during PostgreSQL installation.
