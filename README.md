# MyShop Application

## Overview
The system manages clients and products. There is a clear separation between regular client operations and admin operations, and all actions are protected using authentication and authorization mechanisms.

---

## System Flow

### 1. Create Client
The first step is creating a new client using the create client endpoint.  
The client is stored in the system with a REGULAR role and is not authenticated at this stage.

### 2. Authenticate Client
The client performs authentication using a contact method such as email or phone.  
If the provided details match one of the stored contact methods, the client is marked as authenticated.

### 3. Purchase Product
Only an authenticated client with a REGULAR role can purchase a product.  
Before the purchase, authorization is validated using the AuthService.  
If everything is valid, the product is added to the client’s list of purchased products.

### 4. Product Management (Admin)
An admin can:
- Add products to the catalog
- Update existing products
- Delete products from the catalog  

All these actions require ADMIN privileges.

### 5. Client Management (Admin)
An admin can:
- Delete a client from the system
- Remove purchased products from a client ()

### 6. View Data
- A client can view their purchased products  
- An admin can retrieve all clients along with their purchased products  

---

## Security

All sensitive operations go through the AuthService, which validates:
- Authentication (is the client logged in)
- Authorization (does the client have the required role)

---

## Architecture

The system is built using a layered architecture:

- Controller Layer – Handles HTTP requests and responses
- Service Layer – Contains business logic
- Repository Layer – Stores data in-memory using HashMaps

---

## Error Handling

The system uses a GlobalExceptionHandler to centralize error handling.

It handles:
- Business exceptions (GenericException)
- Validation errors (@Valid / @Validated)
- Missing parameters
- Invalid enum values

All errors return consistent HTTP responses.

---

## Configuration

The system uses application configuration (application.yml) to initialize core data at startup.

### Product Catalog

The product catalog is preloaded from the application configuration file (application.yml).  
This allows the system to start with a predefined set of products.

### Admin User

An ADMIN user is also loaded from the configuration.  
This is required because admin operations (such as managing products and clients) are part of the system's core functionality.

The admin is treated as a built-in system user rather than a dynamically created client.

### Suggested Flow
1. View All Products (Public)

The first step is to retrieve the full product catalog.

This endpoint is public, meaning no authentication is required.
Any user (even without login) can access it to see available products before purchasing.

2. Create Client

After viewing products, a new client can be created using the create client endpoint.
The client is stored with a REGULAR role.

3. Authenticate Client

The client authenticates using email or phone.
If the details match, the client becomes authenticated and can perform secured actions.

4. Purchase Product

An authenticated REGULAR client can purchase a product.
Authorization is validated before the purchase is processed.

5. Admin Operations

Admin users can manage the system:

Add / update / delete products
Manage clients and their purchases




## Swagger API Documentation

The project uses Swagger (springdoc-openapi) for API documentation.

Swagger provides:
- Description for each endpoint
- Request and response schemas
- Error responses
- Ability to test endpoints directly from the UI

Swagger UI is available at:
http://localhost:8080/swagger-ui/index.html

---
