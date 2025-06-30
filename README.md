# Online Book Store

A modern web application for an online book store with React frontend and Spring Boot backend.

## Tech Stack

### Frontend
- React with TypeScript
- React Router for navigation
- Material-UI for components
- Axios for API calls

### Backend
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Spring Boot Actuator for monitoring

### Infrastructure
- Docker for containerization
- PostgreSQL for database

## Features

1. User Authentication & Authorization
   - JWT-based authentication
   - Two roles: User and Admin
   - Secure password handling

2. Book Management
   - Browse books
   - Search and filter books
   - Book details view
   - Admin: Add/Edit/Delete books

3. Shopping Cart
   - Add/Remove books
   - Update quantities
   - Save cart for logged-in users

4. Order Management
   - Place orders
   - View order history
   - Order status tracking

5. Admin Features
   - Manage books
   - View all orders
   - Manage users
   - View system metrics (Actuator)

## Project Structure

```
bookstore/
├── frontend/           # React frontend application
├── backend/           # Spring Boot backend application
├── docker/            # Docker configuration files
└── README.md         # Project documentation
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Docker and Docker Compose
- PostgreSQL (if running locally)

### Running with Docker

1. Build and start the containers:
```bash
docker-compose up --build
```

2. Access the applications:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Actuator: http://localhost:8080/actuator

### Development Setup

#### Backend
1. Navigate to backend directory:
```bash
cd backend
```

2. Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

#### Frontend
1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

## API Documentation

The API documentation will be available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator: http://localhost:8080/actuator

## Database Schema

The application uses PostgreSQL with the following main tables:
- users
- books
- orders
- order_items
- shopping_cart
- shopping_cart_items

## Security

- JWT-based authentication
- Role-based access control
- Password encryption
- CORS configuration
- Input validation

## Monitoring

Spring Boot Actuator provides the following endpoints:
- /actuator/health
- /actuator/metrics
- /actuator/info
- /actuator/prometheus 