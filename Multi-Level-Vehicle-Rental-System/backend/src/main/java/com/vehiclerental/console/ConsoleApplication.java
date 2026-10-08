package com.vehiclerental.console;

import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.model.*;
import com.vehiclerental.service.VehicleRentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Terminal/Console Application for the Multi-Level Vehicle Rental System.
 *
 * This is a standalone console program that uses the SAME VehicleRentalService
 * as the REST API — no duplicate business logic.
 *
 * To run: java -cp target/vehicle-rental-system-1.0.0.jar
 *              com.vehiclerental.console.ConsoleApplication
 *
 * Or use the provided run-console.bat script.
 */
public class ConsoleApplication {

    private final VehicleRentalService service;
    private final Scanner scanner;

    public ConsoleApplication(VehicleRentalService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Start the console application loop.
     */
    public void run() {
        System.out.println("\n==========================================");
        System.out.println("     RENTX - MULTI-LEVEL VEHICLE RENTAL");
        System.out.println("==========================================");
        System.out.println("  Terminal Application Started");
        System.out.println("  Sample data loaded. Ready to use.\n");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> registerVehicle();
                case 2 -> viewVehicles();
                case 3 -> searchVehicle();
                case 4 -> registerCustomer();
                case 5 -> viewCustomers();
                case 6 -> rentVehicle();
                case 7 -> returnVehicle();
                case 8 -> viewRentalHistory();
                case 9 -> searchRental();
                case 10 -> {
                    System.out.println("\nThank you for using RENTX. Goodbye!");
                    running = false;
                }
                default -> System.out.println("[ERROR] Invalid choice. Please enter 1-10.");
            }
        }
        scanner.close();
    }

    // ==============================
    // MENU
    // ==============================

    private void printMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("      MULTI-LEVEL VEHICLE RENTAL SYSTEM");
        System.out.println("==========================================");
        System.out.println("  1.  Register Vehicle");
        System.out.println("  2.  View Vehicles");
        System.out.println("  3.  Search Vehicle");
        System.out.println("  4.  Register Customer");
        System.out.println("  5.  View Customers");
        System.out.println("  6.  Rent Vehicle");
        System.out.println("  7.  Return Vehicle");
        System.out.println("  8.  View Rental History");
        System.out.println("  9.  Search Rental");
        System.out.println("  10. Exit");
        System.out.println("==========================================");
    }

    // ==============================
    // 1. REGISTER VEHICLE
    // ==============================

    private void registerVehicle() {
        System.out.println("\n--- REGISTER VEHICLE ---");
        System.out.println("Select vehicle type:");
        System.out.println("  1. Car");
        System.out.println("  2. Bike");
        System.out.println("  3. Truck");

        int typeChoice = readInt("Enter type (1-3): ");
        if (typeChoice < 1 || typeChoice > 3) {
            System.out.println("[ERROR] Invalid type selection.");
            return;
        }

        String vehicleId = readString("Enter Vehicle ID (e.g. C104): ");
        String model = readString("Enter Model name: ");
        double baseRate = readDouble("Enter Base Rate per day (INR): ");

        if (baseRate <= 0) {
            System.out.println("[ERROR] Base rate must be greater than zero.");
            return;
        }

        try {
            Vehicle vehicle;
            switch (typeChoice) {
                case 1 -> vehicle = new Car(vehicleId, model, baseRate);
                case 2 -> vehicle = new Bike(vehicleId, model, baseRate);
                case 3 -> {
                    double perKmCharge = readDouble("Enter Per Kilometer Charge (INR): ");
                    vehicle = new Truck(vehicleId, model, baseRate, perKmCharge);
                }
                default -> {
                    System.out.println("[ERROR] Invalid type.");
                    return;
                }
            }

            service.registerVehicle(vehicle);
            System.out.println("\n✓ Vehicle registered successfully!");
            System.out.println("  " + vehicle);

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ==============================
    // 2. VIEW VEHICLES
    // ==============================

    private void viewVehicles() {
        System.out.println("\n--- VIEW VEHICLES ---");
        System.out.println("Filter by:");
        System.out.println("  1. All Vehicles");
        System.out.println("  2. Available Only");
        System.out.println("  3. Rented Only");

        int choice = readInt("Enter choice (1-3): ");
        List<Vehicle> vehicles;

        switch (choice) {
            case 1 -> vehicles = service.getAllVehicles();
            case 2 -> vehicles = service.getVehiclesByAvailability(true);
            case 3 -> vehicles = service.getVehiclesByAvailability(false);
            default -> {
                System.out.println("[ERROR] Invalid choice.");
                return;
            }
        }

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println("\n" + String.format("%-8s %-8s %-30s %-12s %-12s",
                "ID", "Type", "Model", "Rate/Day", "Status"));
        System.out.println("-".repeat(75));

        for (Vehicle v : vehicles) {
            String perKm = "";
            if (v instanceof Truck t) {
                perKm = " (+₹" + t.getPerKilometerCharge() + "/km)";
            }
            System.out.printf("%-8s %-8s %-30s %-12s %-12s%n",
                    v.getVehicleId(),
                    v.getVehicleType(),
                    v.getModel(),
                    "₹" + String.format("%.0f", v.getBaseRate()) + perKm,
                    v.isAvailable() ? "AVAILABLE" : "RENTED");
        }
        System.out.println("-".repeat(75));
        System.out.println("Total: " + vehicles.size() + " vehicle(s)");
    }

    // ==============================
    // 3. SEARCH VEHICLE
    // ==============================

    private void searchVehicle() {
        System.out.println("\n--- SEARCH VEHICLE ---");
        String query = readString("Enter search term (ID / Model / Type): ");

        List<Vehicle> results = service.searchVehicles(query);
        if (results.isEmpty()) {
            System.out.println("No vehicles found for: " + query);
            return;
        }

        System.out.println("\nSearch results (" + results.size() + " found):");
        for (Vehicle v : results) {
            System.out.println("  " + v);
        }
    }

    // ==============================
    // 4. REGISTER CUSTOMER
    // ==============================

    private void registerCustomer() {
        System.out.println("\n--- REGISTER CUSTOMER ---");
        String customerId = readString("Enter Customer ID (e.g. CUST004): ");
        String name = readString("Enter Full Name: ");
        String phone = readString("Enter Phone Number: ");
        String email = readString("Enter Email: ");

        if (phone.length() < 10) {
            System.out.println("[ERROR] Phone number must be at least 10 digits.");
            return;
        }

        try {
            Customer customer = new Customer(customerId, name, phone, email);
            service.registerCustomer(customer);
            System.out.println("\n✓ Customer registered successfully!");
            System.out.println("  " + customer);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ==============================
    // 5. VIEW CUSTOMERS
    // ==============================

    private void viewCustomers() {
        System.out.println("\n--- VIEW CUSTOMERS ---");
        List<Customer> customers = service.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        System.out.println("\n" + String.format("%-12s %-25s %-15s %-30s",
                "ID", "Name", "Phone", "Email"));
        System.out.println("-".repeat(85));

        for (Customer c : customers) {
            System.out.printf("%-12s %-25s %-15s %-30s%n",
                    c.getCustomerId(), c.getName(), c.getPhone(), c.getEmail());
        }
        System.out.println("-".repeat(85));
        System.out.println("Total: " + customers.size() + " customer(s)");
    }

    // ==============================
    // 6. RENT VEHICLE
    // ==============================

    private void rentVehicle() {
        System.out.println("\n--- RENT VEHICLE ---");

        String customerId = readString("Enter Customer ID: ");
        Customer customer = service.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("[ERROR] Customer not found: " + customerId);
            return;
        }
        System.out.println("  Customer: " + customer.getName());

        String vehicleId = readString("Enter Vehicle ID: ");
        Vehicle vehicle = service.getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("[ERROR] Vehicle not found: " + vehicleId);
            return;
        }
        System.out.println("  Vehicle: " + vehicle.getModel() + " (" + vehicle.getVehicleType() + ")");

        if (!vehicle.isAvailable()) {
            System.out.println("[ERROR] Vehicle " + vehicleId + " is currently unavailable (already rented).");
            return;
        }

        int days = readInt("Enter number of rental days: ");
        if (days <= 0) {
            System.out.println("[ERROR] Number of days must be greater than zero.");
            return;
        }

        double kilometers = 0;
        if (vehicle instanceof Truck) {
            kilometers = readDouble("Enter estimated kilometers: ");
            if (kilometers < 0) {
                System.out.println("[ERROR] Kilometers cannot be negative.");
                return;
            }
        }

        try {
            Rental rental = service.rentVehicle(customerId, vehicleId, days, kilometers);
            printRentalConfirmation(rental);
        } catch (VehicleNotAvailableException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void printRentalConfirmation(Rental rental) {
        System.out.println("\n========================================");
        System.out.println("         RENTAL CONFIRMATION");
        System.out.println("========================================");
        System.out.printf("Rental ID       : %s%n", rental.getRentalId());
        System.out.printf("Customer        : %s (%s)%n",
                rental.getCustomer().getName(), rental.getCustomer().getCustomerId());
        System.out.printf("Vehicle         : %s%n", rental.getVehicle().getVehicleId());
        System.out.printf("Vehicle Type    : %s%n", rental.getVehicle().getVehicleType());
        System.out.printf("Model           : %s%n", rental.getVehicle().getModel());
        System.out.printf("Rental Days     : %d%n", rental.getNumberOfDays());
        System.out.printf("Daily Rate      : ₹%.2f%n", rental.getVehicle().getBaseRate());
        if (rental.getVehicle() instanceof Truck t && rental.getKilometers() > 0) {
            System.out.printf("Kilometers      : %.0f km%n", rental.getKilometers());
            System.out.printf("Per Km Charge   : ₹%.2f%n", t.getPerKilometerCharge());
        }
        System.out.printf("Total Amount    : ₹%.2f%n", rental.getBaseAmount());
        System.out.printf("Rental Date     : %s%n", rental.getRentalDate());
        System.out.printf("Expected Return : %s%n", rental.getExpectedReturnDate());
        System.out.println("----------------------------------------");
        System.out.println("Vehicle Status  : RENTED");
        System.out.println("========================================");
    }

    // ==============================
    // 7. RETURN VEHICLE
    // ==============================

    private void returnVehicle() {
        System.out.println("\n--- RETURN VEHICLE ---");

        // Show active rentals
        List<Rental> activeRentals = service.getActiveRentals();
        if (activeRentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }

        System.out.println("\nActive Rentals:");
        System.out.printf("%-10s %-12s %-15s %-12s %-15s%n",
                "Rental ID", "Customer", "Vehicle", "Days", "Expected Return");
        System.out.println("-".repeat(70));
        for (Rental r : activeRentals) {
            System.out.printf("%-10s %-12s %-15s %-12d %-15s%n",
                    r.getRentalId(),
                    r.getCustomer().getCustomerId(),
                    r.getVehicle().getVehicleId(),
                    r.getNumberOfDays(),
                    r.getExpectedReturnDate());
        }

        String rentalId = readString("\nEnter Rental ID to return: ");

        System.out.println("Return options:");
        System.out.println("  1. Return today");
        System.out.println("  2. Enter specific return date (YYYY-MM-DD)");
        System.out.println("  3. Enter number of extra days (after expected return)");
        int returnChoice = readInt("Choose option (1-3): ");

        try {
            Rental rental;
            switch (returnChoice) {
                case 1 -> rental = service.returnVehicle(rentalId, LocalDate.now());
                case 2 -> {
                    String dateStr = readString("Enter return date (YYYY-MM-DD): ");
                    LocalDate returnDate = LocalDate.parse(dateStr);
                    rental = service.returnVehicle(rentalId, returnDate);
                }
                case 3 -> {
                    int extraDays = readInt("Enter number of extra days: ");
                    rental = service.returnVehicleWithExtraDays(rentalId, extraDays);
                }
                default -> {
                    System.out.println("[ERROR] Invalid choice.");
                    return;
                }
            }
            printReturnReceipt(rental);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void printReturnReceipt(Rental rental) {
        System.out.println("\n========================================");
        System.out.println("            RETURN RECEIPT");
        System.out.println("========================================");
        System.out.printf("Rental ID       : %s%n", rental.getRentalId());
        System.out.printf("Vehicle         : %s (%s)%n",
                rental.getVehicle().getVehicleId(), rental.getVehicle().getModel());
        System.out.printf("Customer        : %s%n", rental.getCustomer().getName());
        System.out.println("----------------------------------------");
        System.out.printf("Original Amount : ₹%.2f%n", rental.getBaseAmount());
        System.out.printf("Late Fee        : ₹%.2f%n", rental.getLateFee());
        System.out.printf("Final Amount    : ₹%.2f%n", rental.getTotalAmount());
        System.out.println("----------------------------------------");
        System.out.printf("Return Date     : %s%n", rental.getActualReturnDate());
        System.out.println("Vehicle Status  : AVAILABLE");
        System.out.println("========================================");
    }

    // ==============================
    // 8. VIEW RENTAL HISTORY
    // ==============================

    private void viewRentalHistory() {
        System.out.println("\n--- RENTAL HISTORY ---");
        System.out.println("  1. All Rentals");
        System.out.println("  2. Active Rentals Only");
        System.out.println("  3. Rental History for a Customer");
        System.out.println("  4. Rental History for a Vehicle");

        int choice = readInt("Enter choice (1-4): ");
        List<Rental> rentals;

        switch (choice) {
            case 1 -> rentals = service.getAllRentals();
            case 2 -> rentals = service.getActiveRentals();
            case 3 -> {
                String cId = readString("Enter Customer ID: ");
                rentals = service.getRentalsByCustomer(cId);
            }
            case 4 -> {
                String vId = readString("Enter Vehicle ID: ");
                rentals = service.getRentalsByVehicle(vId);
            }
            default -> {
                System.out.println("[ERROR] Invalid choice.");
                return;
            }
        }

        if (rentals.isEmpty()) {
            System.out.println("No rental records found.");
            return;
        }

        printRentalTable(rentals);
    }

    private void printRentalTable(List<Rental> rentals) {
        System.out.println("\n" + String.format("%-10s %-12s %-10s %-6s %-12s %-12s %-10s",
                "Rental ID", "Customer", "Vehicle", "Days", "Amount", "Date", "Status"));
        System.out.println("-".repeat(80));

        for (Rental r : rentals) {
            System.out.printf("%-10s %-12s %-10s %-6d ₹%-11.0f %-12s %-10s%n",
                    r.getRentalId(),
                    r.getCustomer().getCustomerId(),
                    r.getVehicle().getVehicleId(),
                    r.getNumberOfDays(),
                    r.getTotalAmount(),
                    r.getRentalDate(),
                    r.getStatus());
        }
        System.out.println("-".repeat(80));
        System.out.println("Total: " + rentals.size() + " rental(s)");
    }

    // ==============================
    // 9. SEARCH RENTAL
    // ==============================

    private void searchRental() {
        System.out.println("\n--- SEARCH RENTAL ---");
        String rentalId = readString("Enter Rental ID: ");

        Rental rental = service.getRentalById(rentalId);
        if (rental == null) {
            System.out.println("[ERROR] Rental not found: " + rentalId);
            return;
        }

        System.out.println("\nRental Details:");
        System.out.println("  Rental ID    : " + rental.getRentalId());
        System.out.println("  Customer     : " + rental.getCustomer().getName() +
                " (" + rental.getCustomer().getCustomerId() + ")");
        System.out.println("  Vehicle      : " + rental.getVehicle().getModel() +
                " [" + rental.getVehicle().getVehicleId() + "]");
        System.out.println("  Type         : " + rental.getVehicle().getVehicleType());
        System.out.println("  Days         : " + rental.getNumberOfDays());
        System.out.println("  Base Amount  : ₹" + String.format("%.2f", rental.getBaseAmount()));
        System.out.println("  Late Fee     : ₹" + String.format("%.2f", rental.getLateFee()));
        System.out.println("  Total Amount : ₹" + String.format("%.2f", rental.getTotalAmount()));
        System.out.println("  Rental Date  : " + rental.getRentalDate());
        System.out.println("  Expected     : " + rental.getExpectedReturnDate());
        System.out.println("  Returned     : " + (rental.getActualReturnDate() != null ?
                rental.getActualReturnDate() : "Not yet returned"));
        System.out.println("  Status       : " + rental.getStatus());
    }

    // ==============================
    // INPUT HELPERS
    // ==============================

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid number.");
            }
        }
    }

    // ==============================
    // MAIN METHOD (standalone entry)
    // ==============================

    /**
     * Entry point to run the console application standalone (without Spring Boot).
     */
    public static void main(String[] args) {
        VehicleRentalService rentalService = new VehicleRentalService();
        ConsoleApplication app = new ConsoleApplication(rentalService);
        app.run();
    }
}
