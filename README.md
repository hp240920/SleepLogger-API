# Sleep Log Service

## Overview
The Sleep Log Service is a backend application designed to help users log and manage their sleep data. It provides robust RESTful APIs for creating, updating, and retrieving sleep logs, ensuring a seamless experience for integrating with frontend applications like Noom's web interface. This project emphasizes scalability, maintainability, and test coverage to meet real-world requirements.

## Features
1. **Sleep Log Management**:
    - Create, update, and delete sleep logs with attributes such as time in bed, sleep quality, and total sleep duration.
    - Retrieve sleep logs by username, date, or a range of dates.

2. **Advanced Functionalities**:
    - Update specific fields like sleep times and feelings.
    - Calculate last night's sleep and 30-day averages for sleep duration.

3. **Global Exception Handling**:
    - Centralized error handling with detailed responses for invalid inputs, resource not found, and unexpected system errors.

4. **Comprehensive Testing**:
    - Unit tests for all service methods and exception handlers.
    - Validation of edge cases and error scenarios to ensure robust behavior.

5. **Scalable Design**:
    - Modularized architecture with clear separation of concerns for controllers, services, and repositories.
    - Entity-to-DTO mapping for clean data handling.

## Development Workflow

### 1. **Design and Planning**:
- Defined the requirements for managing sleep logs and their attributes.
- Planned a scalable architecture using Spring Boot and Kotlin.

### 2. **Database Integration**:
- Designed entities for `SleepLog` and integrated them with a repository layer.
- Added support for querying sleep logs by username, date, and date range.

### 3. **API Development**:
- Implemented RESTful APIs for CRUD operations.
- Added custom methods for retrieving last night's sleep and calculating 30-day averages.

### 4. **Error Handling**:
- Developed a global exception handler to manage errors consistently.
- Created meaningful error responses for common issues like invalid arguments and missing resources.

### 5. **Unit Testing**:
- Wrote comprehensive unit tests using JUnit and Mockito.
- Ensured 100% coverage for all service methods and exception handlers.

### 6. **Documentation**:
- Added API documentation and code comments for maintainability.
- Prepared this README to guide developers and stakeholders.

## Technologies Used
- **Kotlin**: Primary programming language.
- **Spring Boot**: Framework for API development.
- **Hibernate/JPA**: ORM for database interactions.
- **Gradle**: Build and dependency management tool.
- **JUnit & Mockito**: For testing.

## Additional Files
- **Postman Collection:** A `SleepLoggerAPI_Postman_Collection.json` file is included to test all endpoints easily. This collection contains pre-configured requests for all APIs.
- **Public Repository Reference:** The project includes references to a [public repository and the pull requests](https://github.com/hp240920/SleepLogger-API/pull/1) created during development. This showcases the incremental development process and adherence to production-level coding standards.
