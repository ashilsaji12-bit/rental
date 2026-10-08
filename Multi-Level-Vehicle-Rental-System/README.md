# 🚗 RENTX – Multi-Level Vehicle Rental System

> **KTU S3 B.Tech CSE / Data Science OOP Project**

A complete Multi-Level Vehicle Rental System built with **Java Spring Boot** (backend) and **HTML/CSS/JavaScript** (frontend), demonstrating all core Java OOP concepts.

---

## 📋 Features

- ✅ Vehicle registration (Car, Bike, Truck)
- ✅ Customer registration and management
- ✅ Vehicle rental with polymorphic pricing
- ✅ Vehicle return with automatic late-fee calculation
- ✅ Rental history and transaction tracking
- ✅ Search and filter vehicles and customers
- ✅ Custom exception handling (`VehicleNotAvailableException`)
- ✅ Dashboard with live statistics
- ✅ RESTful API backend
- ✅ Modern responsive web interface
- ✅ Terminal/console application
- ✅ In-memory data storage (ArrayList + HashMap)

---

## 🎓 OOP Concepts Used

| Concept | Where |
|--------|-------|
| **Abstraction** | `Vehicle.java` — abstract class with abstract `calculateRent()` |
| **Inheritance** | `Car`, `Bike`, `Truck` extend `Vehicle` |
| **Polymorphism** | `vehicle.calculateRent(days)` — runtime polymorphism in `VehicleRentalService` |
| **Encapsulation** | All model fields are `private` with getters/setters |
| **Method Overriding** | `calculateRent()` overridden in Car, Bike, Truck differently |
| **Custom Exception** | `VehicleNotAvailableException` — thrown when renting an unavailable vehicle |
| **Exception Handling** | `GlobalExceptionHandler`, try-catch blocks throughout |
| **ArrayList** | `vehicleList`, `customerList`, `rentalList` in service |
| **HashMap** | `vehicleMap`, `customerMap` for O(1) lookup |

---

## 💡 Pricing Logic (Polymorphism Demo)

| Vehicle Type | Formula |
|-------------|---------|
| **Car** | `baseRate × days` |
| **Bike** | `baseRate × days × 0.85` (15% discount) |
| **Truck** | `(baseRate × days) + (km × perKmCharge)` |
| **Late Fee** | `lateDays × ₹500/day` |

---

## 🛠️ Technologies

| Layer | Technology |
|-------|-----------|
| Backend | Java 17+, Spring Boot 3.2.5 |
| Build | Apache Maven 3.9.6 |
| API | REST API (JSON) |
| Data | In-memory (ArrayList + HashMap) |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Server | Embedded Apache Tomcat (via Spring Boot) |

---

## 📁 Project Structure

```
Multi-Level-Vehicle-Rental-System/
├── start-backend.bat          ← Start Spring Boot server
├── run-console.bat            ← Start terminal application
│
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/vehiclerental/
│       ├── VehicleRentalApplication.java  ← Main class
│       ├── CorsConfig.java                ← CORS configuration
│       │
│       ├── model/
│       │   ├── Vehicle.java      ← Abstract base class (ABSTRACTION)
│       │   ├── Car.java          ← Inherits Vehicle (INHERITANCE)
│       │   ├── Bike.java         ← Inherits Vehicle (INHERITANCE)
│       │   ├── Truck.java        ← Inherits Vehicle (INHERITANCE)
│       │   ├── Customer.java     ← Customer model (ENCAPSULATION)
│       │   └── Rental.java       ← Rental transaction model
│       │
│       ├── service/
│       │   └── VehicleRentalService.java  ← Business logic + POLYMORPHISM
│       │
│       ├── controller/
│       │   ├── VehicleController.java     ← Vehicle REST endpoints
│       │   ├── CustomerController.java    ← Customer REST endpoints
│       │   └── RentalController.java      ← Rental REST endpoints
│       │
│       ├── exception/
│       │   ├── VehicleNotAvailableException.java  ← Custom Exception
│       │   └── GlobalExceptionHandler.java        ← Exception handling
│       │
│       └── console/
│           └── ConsoleApplication.java    ← Terminal application
│
└── frontend/
    ├── index.html      ← Dashboard with live stats
    ├── vehicles.html   ← Vehicle list, search, add
    ├── customers.html  ← Customer management
    ├── rental.html     ← Rent a vehicle
    ├── return.html     ← Return + late fee
    ├── history.html    ← Rental history
    │
    ├── css/
    │   └── style.css   ← Complete dark-theme stylesheet
    │
    └── js/
        ├── app.js       ← Shared utilities
        ├── vehicles.js  ← Vehicles page logic
        ├── customers.js ← Customers page logic
        ├── rental.js    ← Rental page logic
        └── history.js   ← History page logic
```

---

## ⚙️ Requirements

- **Java** 17 or later (Java 25 recommended)
- **Apache Maven** 3.9+ (included at `C:\tools\apache-maven-3.9.6`)
- **Node.js** (optional — only for running a local frontend server)
- A modern web browser

---

## 🚀 How to Run

### 1. Start the Backend (Spring Boot)

**Option A: Using the batch script (recommended)**
```
Double-click: start-backend.bat
```

**Option B: Using terminal**
```powershell
cd backend
# Add Maven to PATH if needed:
$env:PATH = "C:\tools\apache-maven-3.9.6\bin;" + $env:PATH

mvn spring-boot:run
```

The server starts on: `http://localhost:8080`

---

### 2. Open the Frontend (Website)

After the backend is running:

**Option A: Open directly** (may have CORS issues with file://)
```
Open: frontend/index.html  in your browser
```

**Option B: Run a local server (recommended)**
```powershell
cd frontend
npx -y http-server . -p 3000
# Then open: http://localhost:3000
```

---

### 3. Run the Terminal Application

```
Double-click: run-console.bat
```

Or manually:
```powershell
$env:PATH = "C:\tools\apache-maven-3.9.6\bin;" + $env:PATH
cd backend
mvn package -DskipTests
java -cp "target\vehicle-rental-system-1.0.0.jar" com.vehiclerental.console.ConsoleApplication
```

---

## 🌐 REST API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/api/vehicles` | List all vehicles |
| GET | `/api/vehicles?search=swift` | Search vehicles |
| GET | `/api/vehicles?type=Car` | Filter by type |
| GET | `/api/vehicles?available=true` | Filter by availability |
| GET | `/api/vehicles/{id}` | Get vehicle by ID |
| POST | `/api/vehicles` | Register a new vehicle |
| DELETE | `/api/vehicles/{id}` | Delete a vehicle |
| GET | `/api/vehicles/stats` | Dashboard statistics |
| GET | `/api/customers` | List all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Register a customer |
| GET | `/api/rentals` | List all rentals |
| GET | `/api/rentals?active=true` | Active rentals only |
| GET | `/api/rentals/{id}` | Get rental by ID |
| POST | `/api/rentals` | Create a rental |
| POST | `/api/rentals/{id}/return` | Return a vehicle |
| GET | `/api/rentals/customer/{id}` | Customer's rental history |
| GET | `/api/rentals/vehicle/{id}` | Vehicle's rental history |

---

## 📝 Sample API Usage

### Register a Car
```json
POST /api/vehicles
{
  "vehicleType": "Car",
  "vehicleId": "C104",
  "model": "Toyota Innova",
  "baseRate": 2500
}
```

### Register a Truck
```json
POST /api/vehicles
{
  "vehicleType": "Truck",
  "vehicleId": "T103",
  "model": "Mahindra Bolero",
  "baseRate": 2800,
  "perKilometerCharge": 12.0
}
```

### Rent a Vehicle
```json
POST /api/rentals
{
  "customerId": "CUST001",
  "vehicleId": "C101",
  "days": 3,
  "kilometers": 0
}
```

### Return a Vehicle
```json
POST /api/rentals/R1001/return
{
  "returnDate": "2025-04-15"
}
```

---

## 🗂️ Sample Data (Preloaded)

### Vehicles
| ID | Model | Type | Rate |
|----|-------|------|------|
| C101 | Maruti Swift | Car | ₹1500/day |
| C102 | Hyundai i20 | Car | ₹1800/day |
| C103 | Honda City | Car | ₹2200/day |
| B101 | Yamaha R15 | Bike | ₹800/day |
| B102 | Royal Enfield Classic 350 | Bike | ₹1000/day |
| B103 | Honda Activa | Bike | ₹500/day |
| T101 | Tata 407 | Truck | ₹3000/day + ₹15/km |
| T102 | Ashok Leyland Partner | Truck | ₹3500/day + ₹18/km |

### Customers
| ID | Name | Phone |
|----|------|-------|
| CUST001 | Ashil Saji | 9876543210 |
| CUST002 | Priya Nair | 9123456780 |
| CUST003 | Rahul Kumar | 9988776655 |

---

## 🔮 Future Enhancements

- Database integration (MySQL / PostgreSQL)
- User authentication (Login/Register)
- Vehicle image uploads
- PDF invoice generation
- Email confirmation on rental
- Multi-branch support
- Payment gateway integration

---

## 👨‍💻 Author

**KTU S3 B.Tech CSE / Data Science OOP Project**  
Technology: Java Spring Boot + HTML/CSS/JS  
Pattern: MVC Architecture + REST API
