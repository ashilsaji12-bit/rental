package com.vehiclerental.exception;

/**
 * OOP Concept: CUSTOM EXCEPTION HANDLING
 *
 * VehicleNotAvailableException is a custom exception that is thrown when
 * a user tries to rent a vehicle that is already rented out.
 *
 * It extends RuntimeException so it does not need to be declared in method signatures,
 * but it can also be caught specifically to show a clear error message.
 */
public class VehicleNotAvailableException extends RuntimeException {

    private final String vehicleId;

    /**
     * Constructor with a vehicle ID for a descriptive message.
     *
     * @param vehicleId The ID of the vehicle that is not available
     */
    public VehicleNotAvailableException(String vehicleId) {
        super("Vehicle " + vehicleId + " is currently unavailable.");
        this.vehicleId = vehicleId;
    }

    /**
     * Constructor with a custom message.
     *
     * @param vehicleId The ID of the vehicle
     * @param message   Custom message
     */
    public VehicleNotAvailableException(String vehicleId, String message) {
        super(message);
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }
}
