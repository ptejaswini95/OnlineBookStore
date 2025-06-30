# Online Book Store Backend

A Spring Boot application for an online book store with features like user authentication, book management, and order processing.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd backend
```

2. Create a PostgreSQL database:
```sql
CREATE DATABASE bookstore;
```

3. Configure the application:
   - Copy `application.properties` to `application-local.properties`
   - Update database credentials and other properties as needed

4. Build the application:
```bash
mvn clean install
```

5. Run the application:
```bash
# Development mode
mvn spring-boot:run -Dspring.profiles.active=dev

# Production mode
mvn spring-boot:run -Dspring.profiles.active=prod
```

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/api-docs

## Features

- User Authentication and Authorization
- Book Management (CRUD operations)
- Order Processing
- Search Functionality
- Rate Limiting
- Caching
- Request/Response Logging

## Security

The application uses JWT for authentication. To access protected endpoints:
1. Register a new user or login
2. Use the returned JWT token in the Authorization header:
```
Authorization: Bearer <your-token>
```

## Testing

Run the tests using:
```bash
mvn test
```

## Environment Variables (Production)

In production, set these environment variables:
- `JWT_SECRET`: Secret key for JWT token generation
- `JWT_EXPIRATION`: JWT token expiration time in milliseconds
- `SPRING_DATASOURCE_URL`: Database URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 