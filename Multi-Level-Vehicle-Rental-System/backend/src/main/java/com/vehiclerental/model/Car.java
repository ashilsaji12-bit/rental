package com.vehiclerental.model;

/**
 * OOP Concepts: INHERITANCE + METHOD OVERRIDING + POLYMORPHISM
 *
 * Car extends Vehicle (INHERITANCE).
 * Car provides its own implementation of calculateRent() (METHOD OVERRIDING).
 *
 * Car Pricing Logic:
 *   totalAmount = baseRate × numberOfDays
 */
public class Car extends Vehicle {

    // Constructor calls the parent (Vehicle) constructor using super()
    public Car(String vehicleId, String model, double baseRate) {
        super(vehicleId, model, baseRate);
    }

    /**
     * OOP: METHOD OVERRIDING
     * Car's rental calculation: straightforward per-day rate.
     *
     * @param days Number of rental days
     * @return Total rental amount
     */
    @Override
    public double calculateRent(int days) {
        // Car: simple baseRate × days
        return getBaseRate() * days;
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
