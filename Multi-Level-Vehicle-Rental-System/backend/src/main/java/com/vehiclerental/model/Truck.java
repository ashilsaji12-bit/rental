package com.vehiclerental.model;

/**
 * OOP Concepts: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Truck extends Vehicle (INHERITANCE).
 * Truck provides its own implementation of calculateRent() (METHOD OVERRIDING).
 *
 * Truck Pricing Logic:
 *   totalAmount = (baseRate × numberOfDays) + (kilometers × perKilometerCharge)
 *
 * Trucks have an additional kilometer-based charge because they are heavy vehicles
 * used for transportation - distance matters for fuel and wear.
 */
public class Truck extends Vehicle {

    // Truck-specific field: charge per kilometer driven
    private double perKilometerCharge;

    // Kilometers to be driven (set at rental time)
    private double estimatedKilometers;

    // Constructor
    public Truck(String vehicleId, String model, double baseRate, double perKilometerCharge) {
        super(vehicleId, model, baseRate);
        this.perKilometerCharge = perKilometerCharge;
        this.estimatedKilometers = 0;
    }

    /**
     * OOP: METHOD OVERRIDING
     * Truck's rental calculation includes both daily rate AND kilometer charge.
     * This is a genuinely different calculation from Car and Bike.
     *
     * Formula: (baseRate × days) + (kilometers × perKilometerCharge)
     *
     * @param days Number of rental days
     * @return Total rental amount (NOTE: use calculateRentWithKm for full calculation)
     */
    @Override
    public double calculateRent(int days) {
        // Base calculation without kilometer info
        return (getBaseRate() * days) + (estimatedKilometers * perKilometerCharge);
    }

    /**
     * Full truck rental calculation including kilometers.
     *
     * @param days       Number of rental days
     * @param kilometers Estimated kilometers to be driven
     * @return Total rental amount
     */
    public double calculateRentWithKm(int days, double kilometers) {
        this.estimatedKilometers = kilometers;
        return (getBaseRate() * days) + (kilometers * perKilometerCharge);
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }

    // Getters and setters for truck-specific fields

    public double getPerKilometerCharge() {
        return perKilometerCharge;
    }

    public void setPerKilometerCharge(double perKilometerCharge) {
        this.perKilometerCharge = perKilometerCharge;
    }

    public double getEstimatedKilometers() {
        return estimatedKilometers;
    }

    public void setEstimatedKilometers(double estimatedKilometers) {
        this.estimatedKilometers = estimatedKilometers;
    }
}
