package com.vehiclerental.service;

import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * VehicleRentalService is the central business logic layer of the application.
 *
 * OOP Concepts demonstrated here:
 * - POLYMORPHISM: vehicle.calculateRent(days) calls different methods depending on
 *                 whether the vehicle is a Car, Bike, or Truck at runtime.
 * - ArrayList: used to store all vehicles, customers, and rentals.
 * - HashMap: used for fast O(1) lookup by ID.
 * - Custom Exception: VehicleNotAvailableException is thrown here.
 *
 * Both the REST API (controllers) and the terminal application (ConsoleApplication)
 * use this same service — no duplicate business logic.
 */
@Service
public class VehicleRentalService {

    // ==============================
    // DATA STORAGE: ArrayList + HashMap (OOP requirement)
    // ==============================

    /** ArrayList to maintain an ordered list of all vehicles */
    private final List<Vehicle> vehicleList = new ArrayList<>();

    /** HashMap for O(1) lookup of vehicles by vehicleId */
    private final Map<String, Vehicle> vehicleMap = new HashMap<>();

    /** ArrayList to maintain an ordered list of all customers */
    private final List<Customer> customerList = new ArrayList<>();

    /** HashMap for O(1) lookup of customers by customerId */
    private final Map<String, Customer> customerMap = new HashMap<>();

    /** ArrayList to maintain all rental transactions */
    private final List<Rental> rentalList = new ArrayList<>();

    /** Counter for generating unique rental IDs */
    private final AtomicInteger rentalCounter = new AtomicInteger(1000);

    // ==============================
    // CONSTRUCTOR: Load sample data
    // ==============================

    public VehicleRentalService() {
        loadSampleData();
    }

    /**
     * Loads initial demo data so the application is not empty on first launch.
     */
    private void loadSampleData() {
        // Sample Cars
        registerVehicle(new Car("C101", "Maruti Swift", 1500.0));
        registerVehicle(new Car("C102", "Hyundai i20", 1800.0));
        registerVehicle(new Car("C103", "Honda City", 2200.0));

        // Sample Bikes
        registerVehicle(new Bike("B101", "Yamaha R15", 800.0));
        registerVehicle(new Bike("B102", "Royal Enfield Classic 350", 1000.0));
        registerVehicle(new Bike("B103", "Honda Activa", 500.0));

        // Sample Trucks (with per-km charge)
        registerVehicle(new Truck("T101", "Tata 407", 3000.0, 15.0));
        registerVehicle(new Truck("T102", "Ashok Leyland Partner", 3500.0, 18.0));

        // Sample Customers
        registerCustomer(new Customer("CUST001", "Ashil Saji", "9876543210", "ashil@example.com"));
        registerCustomer(new Customer("CUST002", "Priya Nair", "9123456780", "priya@example.com"));
        registerCustomer(new Customer("CUST003", "Rahul Kumar", "9988776655", "rahul@example.com"));
    }

    // ==============================
    // VEHICLE MANAGEMENT
    // ==============================

    /**
     * Register a new vehicle.
     * Prevents duplicate vehicle IDs.
     *
     * @param vehicle The vehicle to register
     * @throws IllegalArgumentException if the vehicle ID already exists
     */
    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleMap.containsKey(vehicle.getVehicleId())) {
            throw new IllegalArgumentException(
                    "Vehicle ID '" + vehicle.getVehicleId() + "' already exists.");
        }
        vehicleList.add(vehicle);
        vehicleMap.put(vehicle.getVehicleId(), vehicle);
        System.out.println("[INFO] Vehicle registered: " + vehicle);
        return vehicle;
    }

    /**
     * Get all vehicles.
     */
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicleList);
    }

    /**
     * Get a vehicle by its ID.
     *
     * @param vehicleId The vehicle ID to search for
     * @return The vehicle, or null if not found
     */
    public Vehicle getVehicleById(String vehicleId) {
        return vehicleMap.get(vehicleId);
    }

    /**
     * Search vehicles by ID, model, or type (case-insensitive).
     *
     * @param query Search term
     * @return List of matching vehicles
     */
    public List<Vehicle> searchVehicles(String query) {
        String lowerQuery = query.toLowerCase();
        return vehicleList.stream()
                .filter(v -> v.getVehicleId().toLowerCase().contains(lowerQuery)
                        || v.getModel().toLowerCase().contains(lowerQuery)
                        || v.getVehicleType().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Get vehicles filtered by availability.
     *
     * @param available true = available only, false = rented only
     */
    public List<Vehicle> getVehiclesByAvailability(boolean available) {
        return vehicleList.stream()
                .filter(v -> v.isAvailable() == available)
                .collect(Collectors.toList());
    }

    /**
     * Get vehicles filtered by type (Car, Bike, Truck).
     */
    public List<Vehicle> getVehiclesByType(String type) {
        return vehicleList.stream()
                .filter(v -> v.getVehicleType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Delete a vehicle by ID.
     */
    public boolean deleteVehicle(String vehicleId) {
        Vehicle vehicle = vehicleMap.get(vehicleId);
        if (vehicle == null) return false;
        if (!vehicle.isAvailable()) {
            throw new IllegalArgumentException(
                    "Cannot delete vehicle " + vehicleId + " because it is currently rented.");
        }
        vehicleList.remove(vehicle);
        vehicleMap.remove(vehicleId);
        return true;
    }

    // ==============================
    // CUSTOMER MANAGEMENT
    // ==============================

    /**
     * Register a new customer.
     *
     * @param customer Customer to register
     * @throws IllegalArgumentException if the customer ID already exists
     */
    public Customer registerCustomer(Customer customer) {
        if (customerMap.containsKey(customer.getCustomerId())) {
            throw new IllegalArgumentException(
                    "Customer ID '" + customer.getCustomerId() + "' already exists.");
        }
        customerList.add(customer);
        customerMap.put(customer.getCustomerId(), customer);
        System.out.println("[INFO] Customer registered: " + customer);
        return customer;
    }

    /**
     * Get all customers.
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerList);
    }

    /**
     * Get a customer by ID.
     */
    public Customer getCustomerById(String customerId) {
        return customerMap.get(customerId);
    }

    /**
     * Search customers by ID or name (case-insensitive).
     */
    public List<Customer> searchCustomers(String query) {
        String lowerQuery = query.toLowerCase();
        return customerList.stream()
                .filter(c -> c.getCustomerId().toLowerCase().contains(lowerQuery)
                        || c.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // ==============================
    // RENTAL MANAGEMENT
    // ==============================

    /**
     * Rent a vehicle to a customer.
     *
     * IMPORTANT OOP: This method uses POLYMORPHISM.
     *   vehicle.calculateRent(days) is called on a Vehicle reference,
     *   but the actual method that runs depends on whether the object is
     *   a Car, Bike, or Truck — this is runtime polymorphism.
     *
     * @param customerId Customer ID
     * @param vehicleId  Vehicle ID
     * @param days       Number of rental days
     * @param kilometers Kilometers (only relevant for Truck)
     * @return The created Rental object
     * @throws VehicleNotAvailableException if the vehicle is already rented
     * @throws IllegalArgumentException     if IDs are invalid or days <= 0
     */
    public Rental rentVehicle(String customerId, String vehicleId, int days, double kilometers) {
        // Validate inputs
        if (days <= 0) {
            throw new IllegalArgumentException("Number of rental days must be greater than zero.");
        }
        if (kilometers < 0) {
            throw new IllegalArgumentException("Kilometers cannot be negative.");
        }

        // Look up customer using HashMap (O(1))
        Customer customer = customerMap.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        // Look up vehicle using HashMap (O(1))
        Vehicle vehicle = vehicleMap.get(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
        }

        // Check availability — throw custom exception if not available
        if (!vehicle.isAvailable()) {
            throw new VehicleNotAvailableException(vehicleId);
        }

        // POLYMORPHISM: vehicle.calculateRent() calls the correct override
        // based on whether vehicle is Car, Bike, or Truck at RUNTIME.
        double baseAmount;
        if (vehicle instanceof Truck truck) {
            // Trucks use kilometer-based calculation
            baseAmount = truck.calculateRentWithKm(days, kilometers);
        } else {
            // Cars and Bikes use their own overridden calculateRent()
            baseAmount = vehicle.calculateRent(days);
        }

        // Generate a unique rental ID
        String rentalId = "R" + rentalCounter.incrementAndGet();

        // Create the Rental object
        Rental rental = new Rental(rentalId, customer, vehicle, days, kilometers, baseAmount);

        // Mark the vehicle as unavailable (RENTED)
        vehicle.setAvailable(false);

        // Store in ArrayList and (optionally) could add to a map if needed
        rentalList.add(rental);

        System.out.println("[INFO] Rental created: " + rentalId + " | " +
                customer.getName() + " | " + vehicle.getModel() +
                " | Amount: " + baseAmount);

        return rental;
    }

    /**
     * Return a vehicle.
     *
     * @param rentalId   The rental ID
     * @param returnDate The actual return date
     * @return The updated Rental object
     */
    public Rental returnVehicle(String rentalId, LocalDate returnDate) {
        Rental rental = rentalList.stream()
                .filter(r -> r.getRentalId().equals(rentalId))
                .findFirst()
                .orElse(null);

        if (rental == null) {
            throw new IllegalArgumentException("Rental not found: " + rentalId);
        }

        if (rental.getStatus() == Rental.RentalStatus.RETURNED) {
            throw new IllegalArgumentException("Rental " + rentalId + " has already been returned.");
        }

        if (returnDate.isBefore(rental.getRentalDate())) {
            throw new IllegalArgumentException("Return date cannot be before the rental date.");
        }

        // Process return (calculates late fee, marks vehicle available)
        rental.processReturn(returnDate);

        System.out.println("[INFO] Vehicle returned: " + rentalId +
                " | Late fee: " + rental.getLateFee() +
                " | Total: " + rental.getTotalAmount());

        return rental;
    }

    /**
     * Return vehicle by extra days (used by console application).
     */
    public Rental returnVehicleWithExtraDays(String rentalId, int extraDays) {
        Rental rental = rentalList.stream()
                .filter(r -> r.getRentalId().equals(rentalId))
                .findFirst()
                .orElse(null);

        if (rental == null) {
            throw new IllegalArgumentException("Rental not found: " + rentalId);
        }

        if (rental.getStatus() == Rental.RentalStatus.RETURNED) {
            throw new IllegalArgumentException("Rental " + rentalId + " has already been returned.");
        }

        rental.processReturnWithExtraDays(extraDays);
        return rental;
    }

    /**
     * Get all rental transactions.
     */
    public List<Rental> getAllRentals() {
        return new ArrayList<>(rentalList);
    }

    /**
     * Get a single rental by ID.
     */
    public Rental getRentalById(String rentalId) {
        return rentalList.stream()
                .filter(r -> r.getRentalId().equals(rentalId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all rentals for a specific customer.
     */
    public List<Rental> getRentalsByCustomer(String customerId) {
        return rentalList.stream()
                .filter(r -> r.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    /**
     * Get all rentals for a specific vehicle.
     */
    public List<Rental> getRentalsByVehicle(String vehicleId) {
        return rentalList.stream()
                .filter(r -> r.getVehicle().getVehicleId().equals(vehicleId))
                .collect(Collectors.toList());
    }

    /**
     * Get all active rentals (not yet returned).
     */
    public List<Rental> getActiveRentals() {
        return rentalList.stream()
                .filter(r -> r.getStatus() == Rental.RentalStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    // ==============================
    // DASHBOARD STATISTICS
    // ==============================

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalVehicles", (long) vehicleList.size());
        stats.put("availableVehicles", vehicleList.stream().filter(Vehicle::isAvailable).count());
        stats.put("rentedVehicles", vehicleList.stream().filter(v -> !v.isAvailable()).count());
        stats.put("totalCustomers", (long) customerList.size());
        stats.put("activeRentals", rentalList.stream()
                .filter(r -> r.getStatus() == Rental.RentalStatus.ACTIVE).count());
        stats.put("totalRentals", (long) rentalList.size());
        return stats;
    }
}
