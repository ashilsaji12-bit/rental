package com.vehiclerental.controller;

import com.vehiclerental.model.Rental;
import com.vehiclerental.service.VehicleRentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Rental operations.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final VehicleRentalService service;

    public RentalController(VehicleRentalService service) {
        this.service = service;
    }

    /**
     * GET /api/rentals
     * Returns all rentals (or active-only with ?active=true).
     */
    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals(
            @RequestParam(required = false) String active) {
        if ("true".equalsIgnoreCase(active)) {
            return ResponseEntity.ok(service.getActiveRentals());
        }
        return ResponseEntity.ok(service.getAllRentals());
    }

    /**
     * GET /api/rentals/{id}
     * Get a specific rental.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable String id) {
        Rental rental = service.getRentalById(id);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rental);
    }

    /**
     * POST /api/rentals
     * Create a new rental (rent a vehicle).
     *
     * Request body:
     * {
     *   "customerId": "CUST001",
     *   "vehicleId": "C101",
     *   "days": 3,
     *   "kilometers": 0   (only for trucks)
     * }
     */
    @PostMapping
    public ResponseEntity<Rental> rentVehicle(@RequestBody Map<String, Object> body) {
        String customerId = (String) body.get("customerId");
        String vehicleId = (String) body.get("vehicleId");

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID is required.");
        }

        int days = Integer.parseInt(body.getOrDefault("days", 1).toString());
        double kilometers = Double.parseDouble(body.getOrDefault("kilometers", 0).toString());

        Rental rental = service.rentVehicle(customerId, vehicleId, days, kilometers);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    /**
     * POST /api/rentals/{id}/return
     * Return a vehicle.
     *
     * Optional request body:
     * {
     *   "returnDate": "2025-04-10"  (ISO format, defaults to today)
     * }
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<Rental> returnVehicle(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> body) {

        LocalDate returnDate;
        if (body != null && body.containsKey("returnDate")) {
            returnDate = LocalDate.parse(body.get("returnDate"));
        } else {
            returnDate = LocalDate.now();
        }

        Rental rental = service.returnVehicle(id, returnDate);
        return ResponseEntity.ok(rental);
    }

    /**
     * GET /api/rentals/customer/{customerId}
     * Get all rentals for a specific customer.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Rental>> getRentalsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(service.getRentalsByCustomer(customerId));
    }

    /**
     * GET /api/rentals/vehicle/{vehicleId}
     * Get all rentals for a specific vehicle.
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Rental>> getRentalsByVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(service.getRentalsByVehicle(vehicleId));
    }
}
