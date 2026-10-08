package com.vehiclerental.controller;

import com.vehiclerental.model.Bike;
import com.vehiclerental.model.Car;
import com.vehiclerental.model.Truck;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.VehicleRentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Vehicle operations.
 * All endpoints are prefixed with /api/vehicles
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRentalService service;

    public VehicleController(VehicleRentalService service) {
        this.service = service;
    }

    /**
     * GET /api/vehicles
     * Returns all vehicles, optionally filtered by type or availability.
     *
     * Query params:
     *   type        = Car | Bike | Truck
     *   available   = true | false
     *   search      = search term
     */
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String available,
            @RequestParam(required = false) String search) {

        List<Vehicle> vehicles;

        if (search != null && !search.isBlank()) {
            vehicles = service.searchVehicles(search);
        } else if (type != null && !type.isBlank()) {
            vehicles = service.getVehiclesByType(type);
        } else if (available != null) {
            vehicles = service.getVehiclesByAvailability(Boolean.parseBoolean(available));
        } else {
            vehicles = service.getAllVehicles();
        }

        return ResponseEntity.ok(vehicles);
    }

    /**
     * GET /api/vehicles/{id}
     * Returns a specific vehicle by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {
        Vehicle vehicle = service.getVehicleById(id);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicle);
    }

    /**
     * POST /api/vehicles
     * Register a new vehicle.
     *
     * Request body example for Car:
     * {
     *   "vehicleType": "Car",
     *   "vehicleId": "C104",
     *   "model": "Toyota Innova",
     *   "baseRate": 2500
     * }
     *
     * For Truck, also include:
     *   "perKilometerCharge": 20.0
     */
    @PostMapping
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Map<String, Object> body) {
        String vehicleType = (String) body.get("vehicleType");
        String vehicleId = (String) body.get("vehicleId");
        String model = (String) body.get("model");
        double baseRate = Double.parseDouble(body.get("baseRate").toString());

        if (vehicleType == null || vehicleId == null || model == null) {
            throw new IllegalArgumentException("vehicleType, vehicleId, and model are required.");
        }
        if (baseRate <= 0) {
            throw new IllegalArgumentException("Base rate must be greater than zero.");
        }

        Vehicle vehicle;
        switch (vehicleType.toLowerCase()) {
            case "car" -> vehicle = new Car(vehicleId, model, baseRate);
            case "bike" -> vehicle = new Bike(vehicleId, model, baseRate);
            case "truck" -> {
                double perKmCharge = body.containsKey("perKilometerCharge")
                        ? Double.parseDouble(body.get("perKilometerCharge").toString())
                        : 10.0;
                vehicle = new Truck(vehicleId, model, baseRate, perKmCharge);
            }
            default -> throw new IllegalArgumentException(
                    "Invalid vehicle type: " + vehicleType + ". Must be Car, Bike, or Truck.");
        }

        Vehicle registered = service.registerVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    /**
     * DELETE /api/vehicles/{id}
     * Remove a vehicle (only if not currently rented).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVehicle(@PathVariable String id) {
        boolean deleted = service.deleteVehicle(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Vehicle " + id + " deleted successfully.");
        return ResponseEntity.ok(resp);
    }

    /**
     * GET /api/vehicles/stats
     * Returns dashboard statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }
}
