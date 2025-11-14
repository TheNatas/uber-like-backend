# Uber-like Backend Application

A comprehensive Spring Boot backend application for ride-sharing services with JWT authentication, role-based access control, and comprehensive error handling.

## Quick Start

1. **Clone and run:**
   ```bash
   cd demo
   mvn spring-boot:run
   ```

2. **Test the application:**
   ```bash
   curl http://localhost:8080/api/public/health
   ```

3. **Login with sample data:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{"email":"admin@uber.com","password":"admin123"}'
   ```

## Features

✅ **4 Main Entities:** User, Driver, Vehicle, Ride  
✅ **JWT Authentication & Authorization**  
✅ **Role-based Access Control** (ADMIN, DRIVER, PASSENGER)  
✅ **Global Exception Handling**  
✅ **Input Validation**  
✅ **Location-based Services**  
✅ **Real-time Ride Tracking**  
✅ **Comprehensive API Documentation**  

## Sample Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@uber.com | admin123 |
| Passenger | john.doe@email.com | password123 |
| Passenger | jane.smith@email.com | password123 |
| Driver | mike.johnson@email.com | password123 |
| Driver | sarah.wilson@email.com | password123 |

## Key Endpoints

- `GET /api/public/health` - Health check
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/rides/request/{passengerId}` - Request a ride
- `PUT /api/rides/{rideId}/accept/{driverId}` - Accept a ride
- `GET /api/rides/available` - Get available rides

## Technology Stack

- **Spring Boot 3.5.6**
- **Spring Security 6.5.5**
- **Spring Data JPA**
- **H2 Database**
- **JWT Authentication**
- **Hibernate 6.6.29**
- **Maven**
- **Java 17**

## Database Access

- **H2 Console:** http://localhost:8080/h2-console
- **JDBC URL:** jdbc:h2:mem:uberdb
- **Username:** sa
- **Password:** password

## Documentation

For detailed API documentation, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## Project Structure

```
src/main/java/com/example/demo/
├── entity/          # JPA entities (User, Driver, Vehicle, Ride)
├── repository/      # Data repositories
├── service/         # Business logic
├── controller/      # REST controllers
├── dto/             # Data transfer objects
├── security/        # Security configuration & JWT
├── exception/       # Exception handling
└── config/          # Configuration classes
```

## Architecture Highlights

- **Layered Architecture:** Controller → Service → Repository → Entity
- **JWT Security:** Token-based authentication with role-based authorization
- **Error Handling:** Global exception handler with detailed error responses
- **Data Validation:** Comprehensive input validation using Bean Validation
- **Location Services:** Haversine distance calculation for nearby drivers
- **Audit Trail:** Automatic timestamps for all entities

This application demonstrates modern Spring Boot best practices and can serve as a foundation for building scalable ride-sharing or similar marketplace applications.
