markdown
# Food Order App Backend

Spring Boot backend for a food ordering and delivery application handling authentication, 
orders, payments, and restaurant management.

## Development Environment

- **Java**: 21
- **Spring Boot**: 3.5.6
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **IDE**: IntelliJ IDEA

## Quick Start

### Prerequisites
- Java 21
- MySQL 8.0
- Maven

### Setup
1. **Clone and import** into IntelliJ IDEA
2. **Create database**:
   ```sql
   CREATE DATABASE foodorderapp;
Configure src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodorderapp
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
stripe.secret.key=your_stripe_secret_key
Run the main application class

Default URL
http://localhost:8080

Features
-JWT authentication with role-based access (Admin, Customer, Delivery)

- menu management

- Order placement and status tracking

- Stripe payment integration

- Email notifications

- AWS S3 file uploads


# API Endpoints
Method	Endpoint	Description
POST	/api/auth/signup	User registration
POST	/api/auth/login	User login
GET	/api/restaurants	List restaurants
POST	/api/orders	Create order
GET	/api/orders/{id}	Get order details
Configuration
External services required:

Stripe: For payment processing

AWS S3: For file storage (optional)

# Troubleshooting
Issue	Solution
Database connection error	Check MySQL service and credentials
JWT validation fails	Verify JWT secret in configuration
Build failures	Ensure Java 21 and Maven are installed
Port 8080 in use	Change server.port in properties
