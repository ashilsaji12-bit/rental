package com.vehiclerental.model;

import java.time.LocalDate;

/**
 * Rental represents a single rental transaction.
 * Stores all information about a vehicle rental from start to finish.
 *
 * OOP: Composition - Rental HAS-A Customer and HAS-A Vehicle
 */
public class Rental {

    // Rental status constants
    public enum RentalStatus {
        ACTIVE,
        RETURNED
    }

    // ENCAPSULATION: private fields
    private String rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private int numberOfDays;
    private double kilometers;          // Used for Trucks
    private LocalDate rentalDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double baseAmount;
    private double lateFee;
    private double totalAmount;
    private RentalStatus status;

    // Late fee per extra day (can be made configurable)
    public static final double LATE_FEE_PER_DAY = 500.0;

    // Constructor for creating a new rental
    public Rental(String rentalId, Customer customer, Vehicle vehicle,
                  int numberOfDays, double kilometers, double baseAmount) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.numberOfDays = numberOfDays;
        this.kilometers = kilometers;
        this.rentalDate = LocalDate.now();
        this.expectedReturnDate = rentalDate.plusDays(numberOfDays);
        this.baseAmount = baseAmount;
        this.lateFee = 0.0;
        this.totalAmount = baseAmount;
        this.status = RentalStatus.ACTIVE;
    }

    /**
     * Process the return of this rental.
     * Calculates late fee if the vehicle is returned after the expected date.
     *
     * Late fee formula: lateDays × LATE_FEE_PER_DAY
     *
     * @param returnDate The actual return date
     */
    public void processReturn(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
        this.status = RentalStatus.RETURNED;

        // Calculate late fee
        if (returnDate.isAfter(expectedReturnDate)) {
            long lateDays = java.time.temporal.ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            this.lateFee = lateDays * LATE_FEE_PER_DAY;
        } else {
            this.lateFee = 0.0;
        }

        this.totalAmount = this.baseAmount + this.lateFee;

        // Mark vehicle as available again
        vehicle.setAvailable(true);
    }

    /**
     * Process return given extra days (used in the terminal application).
     *
     * @param extraDays Number of extra days beyond the rental period
     */
    public void processReturnWithExtraDays(int extraDays) {
        LocalDate returnDate = expectedReturnDate.plusDays(extraDays);
        processReturn(returnDate);
    }

    // Getters and setters

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public int getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(int numberOfDays) { this.numberOfDays = numberOfDays; }

    public double getKilometers() { return kilometers; }
    public void setKilometers(double kilometers) { this.kilometers = kilometers; }

    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }

    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public double getBaseAmount() { return baseAmount; }
    public void setBaseAmount(double baseAmount) { this.baseAmount = baseAmount; }

    public double getLateFee() { return lateFee; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }
}
