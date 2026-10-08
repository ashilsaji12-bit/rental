package com.vehiclerental.model;

/**
 * OOP Concepts: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Bike extends Vehicle (INHERITANCE).
 * Bike provides its own implementation of calculateRent() (METHOD OVERRIDING).
 *
 * Bike Pricing Logic:
 *   totalAmount = baseRate × numberOfDays × 0.85  (15% discount vs Car - bikes are cheaper)
 */
public class Bike extends Vehicle {

    // Bikes get a 10% discount factor compared to base rate
    private static final double BIKE_DISCOUNT_FACTOR = 0.85;

    // Constructor calls the parent (Vehicle) constructor using super()
    public Bike(String vehicleId, String model, double baseRate) {
        super(vehicleId, model, baseRate);
    }

    /**
     * OOP: METHOD OVERRIDING
     * Bike's rental calculation: discounted per-day rate.
     * Bikes are typically cheaper than cars.
     *
     * @param days Number of rental days
     * @return Total rental amount
     */
    @Override
    public double calculateRent(int days) {
        // Bike: baseRate × days × discount factor
        return getBaseRate() * days * BIKE_DISCOUNT_FACTOR;
    }

    @Override
    public String getVehicleType() {
        return "Bike";
    }

    public double getDiscountFactor() {
        return BIKE_DISCOUNT_FACTOR;
    }
}
