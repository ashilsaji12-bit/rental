package com.vehiclerental.model;

/**
 * OOP Concept: ABSTRACTION
 * Vehicle is an abstract class that defines the common blueprint for all vehicle types.
 * It uses private fields (ENCAPSULATION) and declares an abstract method calculateRent()
 * that forces every subclass to provide its own implementation (POLYMORPHISM).
 */
public abstract class Vehicle {

    // ENCAPSULATION: All fields are private
    private String vehicleId;
    private String model;
    private double baseRate;
    private boolean available;

    // Constructor
    public Vehicle(String vehicleId, String model, double baseRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRate = baseRate;
        this.available = true; // By default, a newly registered vehicle is available
    }

    /**
     * ABSTRACTION: Abstract method - each subclass MUST override this.
     * This is the key polymorphic method.
     *
     * @param days Number of rental days
     * @return Calculated rental amount
     */
    public abstract double calculateRent(int days);

    /**
     * Returns the type of the vehicle as a string.
     * Overridden in each subclass.
     */
    public abstract String getVehicleType();

    // ENCAPSULATION: Getters and setters

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Rate: %.2f - %s",
                vehicleId, model, getVehicleType(), baseRate,
                available ? "AVAILABLE" : "RENTED");
    }
}
