# Uber-like Backend Application

## Overview
This is a comprehensive Spring Boot backend application for an Uber-like ride-sharing service. It includes 4 main entities with proper relationships, Spring Security with JWT authentication, and comprehensive error handling.

## Features
- **JWT Authentication & Authorization**
- **Role-based Access Control** (ADMIN, DRIVER, PASSENGER)
- **Global Exception Handling**
- **Input Validation**
- **H2 In-Memory Database** (easily configurable for production databases)
- **Comprehensive API Documentation**

## Entities

### 1. User
- Basic user information (name, email, phone, password)
- User roles: PASSENGER, DRIVER, ADMIN
- Relationship with Driver entity (one-to-one)
- Relationship with Ride entity (one-to-many as passenger)

### 2. Driver
- Driver-specific information (license number, rating, status)
- Current location tracking
- Relationship with User entity (one-to-one)
- Relationship with Vehicle entity (one-to-many)
- Relationship with Ride entity (one-to-many)

### 3. Vehicle
- Vehicle information (make, model, year, license plate)
- Vehicle types: STANDARD, PREMIUM, SUV, ELECTRIC
- Relationship with Driver entity (many-to-one)

### 4. Ride
- Ride request and tracking information
- Pickup and destination coordinates and addresses
- Ride status tracking (REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED)
- Fare calculation and duration tracking
- Relationship with User entity (many-to-one as passenger)
- Relationship with Driver entity (many-to-one)

## API Endpoints

### Public Endpoints (No Authentication Required)

#### Health Check
```
GET /api/public/health
```
Returns application status and information.

#### Application Info
```
GET /api/public/info
```
Returns application features and entity information.

### Authentication Endpoints

#### User Registration
```
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "password": "password123",
  "role": "PASSENGER"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Driver Registration (for existing users with DRIVER role)
```
POST /api/auth/driver/register/{userId}
Content-Type: application/json

{
  "licenseNumber": "DL123456789",
  "vehicleMake": "Toyota",
  "vehicleModel": "Camry",
  "vehicleYear": 2022,
  "vehicleLicensePlate": "ABC123",
  "vehicleColor": "Silver",
  "vehicleType": "STANDARD"
}
```

### User Management Endpoints (Authenticated)

#### Get All Users (Admin only)
```
GET /api/users
Authorization: Bearer {jwt-token}
```

#### Get User by ID
```
GET /api/users/{id}
Authorization: Bearer {jwt-token}
```

#### Toggle User Status (Admin only)
```
PUT /api/users/{id}/enable
Authorization: Bearer {jwt-token}
```

### Driver Endpoints (Authenticated)

#### Get All Drivers (Admin only)
```
GET /api/drivers
Authorization: Bearer {jwt-token}
```

#### Get Driver by ID
```
GET /api/drivers/{id}
Authorization: Bearer {jwt-token}
```

#### Get Online Drivers (Admin only)
```
GET /api/drivers/online
Authorization: Bearer {jwt-token}
```

#### Update Driver Status (Driver/Admin)
```
PUT /api/drivers/{id}/status?status=ONLINE
Authorization: Bearer {jwt-token}
```

#### Update Driver Location (Driver/Admin)
```
PUT /api/drivers/{id}/location?latitude=40.7128&longitude=-74.0060
Authorization: Bearer {jwt-token}
```

### Ride Endpoints (Authenticated)

#### Request a Ride (Passenger/Admin)
```
POST /api/rides/request/{passengerId}
Content-Type: application/json
Authorization: Bearer {jwt-token}

{
  "pickupLatitude": 40.7128,
  "pickupLongitude": -74.0060,
  "pickupAddress": "New York, NY",
  "destinationLatitude": 40.7580,
  "destinationLongitude": -73.9855,
  "destinationAddress": "Times Square, NY"
}
```

#### Accept a Ride (Driver/Admin)
```
PUT /api/rides/{rideId}/accept/{driverId}
Authorization: Bearer {jwt-token}
```

#### Update Ride Status (Driver/Admin)
```
PUT /api/rides/{rideId}/status?status=IN_PROGRESS
Authorization: Bearer {jwt-token}
```

#### Get Available Rides (Driver/Admin)
```
GET /api/rides/available
Authorization: Bearer {jwt-token}
```

#### Get Ride History
```
GET /api/rides/history/{userId}?userType=PASSENGER
Authorization: Bearer {jwt-token}
```

#### Get Nearby Drivers (Passenger/Admin)
```
GET /api/rides/nearby-drivers?latitude=40.7128&longitude=-74.0060&radiusKm=5.0
Authorization: Bearer {jwt-token}
```

## Sample Data

The application initializes with the following sample data:

### Admin User
- **Email:** admin@uber.com
- **Password:** admin123
- **Role:** ADMIN

### Passengers
- **Email:** john.doe@email.com, **Password:** password123
- **Email:** jane.smith@email.com, **Password:** password123

### Drivers
- **Email:** mike.johnson@email.com, **Password:** password123
- **Email:** sarah.wilson@email.com, **Password:** password123

## Running the Application

1. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Access H2 Console (Development):**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:uberdb
   - Username: sa
   - Password: password

3. **Test the API:**
   ```bash
   curl -X GET http://localhost:8080/api/public/health
   ```

## Authentication Flow

1. **Register a new user** or use existing sample data
2. **Login** to get JWT token
3. **Include token** in Authorization header: `Bearer {jwt-token}`
4. **Make authenticated requests** to protected endpoints

## Error Handling

The application includes comprehensive error handling:
- **400 Bad Request:** Validation errors, business logic errors
- **401 Unauthorized:** Authentication failures
- **403 Forbidden:** Authorization failures
- **404 Not Found:** Resource not found
- **500 Internal Server Error:** Unexpected errors

All error responses include:
- HTTP status code
- Error type
- Detailed message
- Request path
- Timestamp
- Validation details (when applicable)

## Security Features

- **JWT Token-based Authentication**
- **Role-based Authorization**
- **Password Encryption** (BCrypt)
- **Request Validation**
- **SQL Injection Prevention**
- **CORS Configuration**

## Database Schema

The application automatically creates the following tables:
- `users` - User account information
- `drivers` - Driver-specific information
- `vehicles` - Vehicle information
- `rides` - Ride request and tracking data

All tables include audit fields (created_at, updated_at) and proper foreign key relationships.

## Configuration

Key configuration in `application.properties`:
- Database connection (H2 in-memory)
- JWT secret and expiration
- Security settings
- Logging levels

## Development Notes

- The application uses H2 in-memory database for development
- All passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours by default
- The application includes comprehensive logging
- Sample data is automatically loaded on startup
