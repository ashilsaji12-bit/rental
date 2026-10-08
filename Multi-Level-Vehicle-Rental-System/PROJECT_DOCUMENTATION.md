# PROJECT_DOCUMENTATION.md
# RENTX – Multi-Level Vehicle Rental System

**KTU S3 B.Tech CSE / Data Science – OOP Project**

---

## 1. Problem Statement

Managing vehicle rentals manually is error-prone and inefficient. A software-based rental system is needed that can:

- Track multiple vehicle categories (cars, bikes, trucks)
- Handle customer records
- Calculate rental charges automatically using type-specific pricing
- Enforce business rules (unavailable vehicles cannot be rented again)
- Maintain rental history with late-fee support

---

## 2. Objectives

1. Design a multi-level vehicle hierarchy using OOP principles.
2. Implement a RESTful API backend in Java Spring Boot.
3. Create a modern, responsive web frontend.
4. Provide a terminal-based console application.
5. Demonstrate all required OOP concepts genuinely in working code.
6. Use in-memory data structures (ArrayList, HashMap).
7. Handle custom and standard exceptions gracefully.

---

## 3. Proposed System

The system consists of three tiers:

```
Frontend (Browser)  →  REST API (Spring Boot)  →  In-Memory Data (ArrayList/HashMap)
         ↕
Terminal Application  →  VehicleRentalService (shared business logic)
```

---

## 4. System Architecture

### 4.1 Layers

| Layer | Technology | Responsibility |
|-------|-----------|----------------|
| Presentation | HTML/CSS/JS | User interface |
| Controller | Spring Boot REST | HTTP request/response |
| Service | Java (Plain OOP) | Business logic |
| Model | Java Classes | Data representation |
| Data | ArrayList + HashMap | In-memory storage |

### 4.2 MVC Pattern

- **Model** — Vehicle, Car, Bike, Truck, Customer, Rental
- **View** — HTML pages (index, vehicles, customers, rental, return, history)
- **Controller** — VehicleController, CustomerController, RentalController

---

## 5. OOP Concepts – Detailed Explanation

### 5.1 Abstraction

**File:** `Vehicle.java`

```java
public abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRate;
    private boolean available;

    public abstract double calculateRent(int days);  // ABSTRACTION
    public abstract String getVehicleType();
}
```

`Vehicle` is an abstract class. It defines the structure and behavior that all vehicle types must follow, but does not implement `calculateRent()` itself. Each subclass must provide its own implementation.

---

### 5.2 Inheritance

**Files:** `Car.java`, `Bike.java`, `Truck.java`

```java
public class Car extends Vehicle { ... }
public class Bike extends Vehicle { ... }
public class Truck extends Vehicle { ... }
```

All three vehicle types inherit from `Vehicle`, reusing common fields (`vehicleId`, `model`, `baseRate`, `available`) and common methods (getters/setters, toString).

---

### 5.3 Polymorphism

**File:** `VehicleRentalService.java`

```java
// vehicle is declared as Vehicle (parent type)
Vehicle vehicle = vehicleMap.get(vehicleId);

// calculateRent() behaves differently based on actual type at RUNTIME
double amount = vehicle.calculateRent(days);
//   → Car.calculateRent()   if vehicle is a Car
//   → Bike.calculateRent()  if vehicle is a Bike
//   → Truck.calculateRent() if vehicle is a Truck
```

This is **runtime polymorphism** — the JVM decides which method to call based on the actual object type, not the declared type.

---

### 5.4 Encapsulation

**File:** All model classes

```java
public class Customer {
    private String customerId;  // private — hidden from outside
    private String name;
    private String phone;
    private String email;

    public String getCustomerId() { return customerId; }  // controlled access
    public void setCustomerId(String id) { this.customerId = id; }
}
```

Fields are private. Access is controlled through getters and setters.

---

### 5.5 Method Overriding

Each subclass overrides `calculateRent()` with its own pricing logic:

| Class | calculateRent() Logic |
|-------|----------------------|
| `Car.java` | `baseRate × days` |
| `Bike.java` | `baseRate × days × 0.85` (15% discount) |
| `Truck.java` | `(baseRate × days) + (km × perKmCharge)` |

The `@Override` annotation is used in all three classes.

---

### 5.6 Custom Exception Handling

**File:** `VehicleNotAvailableException.java`

```java
public class VehicleNotAvailableException extends RuntimeException {
    private final String vehicleId;

    public VehicleNotAvailableException(String vehicleId) {
        super("Vehicle " + vehicleId + " is currently unavailable.");
        this.vehicleId = vehicleId;
    }
}
```

**Thrown in:** `VehicleRentalService.rentVehicle()` when a vehicle is not available.

**Handled in:** `GlobalExceptionHandler.java` — returns HTTP 409 Conflict with a friendly JSON error message.

---

### 5.7 ArrayList Usage

```java
// VehicleRentalService.java
private final List<Vehicle> vehicleList = new ArrayList<>();
private final List<Customer> customerList = new ArrayList<>();
private final List<Rental> rentalList = new ArrayList<>();
```

ArrayLists are used to maintain ordered lists of all entities, enabling iteration, streaming, and filtering.

---

### 5.8 HashMap Usage

```java
// VehicleRentalService.java
private final Map<String, Vehicle> vehicleMap = new HashMap<>();
private final Map<String, Customer> customerMap = new HashMap<>();
```

HashMaps are used for O(1) lookup by ID. Example:
```java
Vehicle vehicle = vehicleMap.get(vehicleId);  // instant lookup
```

---

## 6. Class Descriptions

### Vehicle (Abstract)
The root of the vehicle hierarchy. Defines the contract: every vehicle must have an ID, model, base rate, availability, and the ability to calculate rent.

### Car
Extends Vehicle. Pricing: `rate × days`. Simple per-day charge.

### Bike
Extends Vehicle. Pricing: `rate × days × 0.85`. Bikes get a 15% discount.

### Truck
Extends Vehicle. Has an extra field `perKilometerCharge`. Pricing: `(rate × days) + (km × perKmCharge)`. Suitable for heavy transport.

### Customer
Stores customer information (ID, name, phone, email). Fully encapsulated.

### Rental
Represents a rental transaction. Contains references to Customer and Vehicle (Composition). Has `processReturn()` which calculates late fees and marks the vehicle as available.

### VehicleRentalService
The core business logic layer. Shared by both the REST API and the terminal application. Contains ArrayList and HashMap storage. Demonstrates polymorphism through `vehicle.calculateRent()`.

### VehicleController / CustomerController / RentalController
Spring Boot REST controllers. Handle HTTP requests and delegate to `VehicleRentalService`.

### ConsoleApplication
A terminal-based application that uses the same `VehicleRentalService` for all operations. Provides a text-menu interface.

### VehicleNotAvailableException
Custom exception thrown when an unavailable vehicle is rented. Extends RuntimeException.

### GlobalExceptionHandler
Catches all exceptions and converts them to JSON responses. Hides stack traces from users.

---

## 7. Feature Descriptions

### 7.1 Vehicle Registration
- User selects type (Car / Bike / Truck)
- Enters Vehicle ID, model, base rate
- For Truck: also enters per-km charge
- Duplicate IDs are prevented (checked via HashMap)
- Vehicle starts as AVAILABLE

### 7.2 Vehicle Rental
- Validates customer and vehicle IDs
- Checks availability (throws `VehicleNotAvailableException` if not available)
- Calculates amount via polymorphism (`vehicle.calculateRent(days)`)
- Creates a Rental object, marks vehicle as RENTED
- Returns a rental confirmation/receipt

### 7.3 Vehicle Return
- Finds the active rental
- Calculates late fee: `lateDays × ₹500`
- Updates rental status to RETURNED
- Marks vehicle as AVAILABLE
- Returns a return receipt with final amount

### 7.4 Rental History
- Lists all rentals with full details
- Supports filter by status (Active / Returned)
- Supports filter by vehicle type
- Supports text search

---

## 8. Testing Summary

| Test Case | Result |
|-----------|--------|
| Backend compiles | ✅ PASS |
| Spring Boot starts | ✅ PASS |
| Sample data loads | ✅ PASS |
| GET /api/vehicles | ✅ PASS |
| GET /api/customers | ✅ PASS |
| POST /api/vehicles (Car) | ✅ PASS |
| POST /api/vehicles (Bike) | ✅ PASS |
| POST /api/vehicles (Truck) | ✅ PASS |
| POST /api/customers | ✅ PASS |
| POST /api/rentals | ✅ PASS |
| VehicleNotAvailableException | ✅ PASS |
| POST /api/rentals/{id}/return | ✅ PASS |
| Late fee calculation | ✅ PASS |
| Polymorphism (Car/Bike/Truck pricing) | ✅ PASS |
| Terminal application | ✅ PASS |
| Frontend connects to backend | ✅ PASS |
| Duplicate ID prevention | ✅ PASS |

---

## 9. Future Scope

1. **Database Integration** — Persist data to MySQL or PostgreSQL using JPA/Hibernate
2. **Authentication** — JWT-based login for admin and customer roles
3. **PDF Receipts** — Generate printable rental and return receipts
4. **Email Notifications** — Send confirmation emails via SMTP/SendGrid
5. **Multi-branch support** — Support multiple rental office locations
6. **Vehicle Images** — Upload and display vehicle images
7. **Analytics Dashboard** — Revenue reports, vehicle utilization graphs
8. **Mobile App** — React Native or Flutter frontend
9. **Payment Integration** — Razorpay or Stripe for online payments
10. **Advanced Search** — Filter by price range, availability date range

---

*This document accurately describes the features that are fully implemented in this project.*
