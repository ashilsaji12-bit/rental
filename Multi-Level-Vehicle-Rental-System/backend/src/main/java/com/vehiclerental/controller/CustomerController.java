package com.vehiclerental.controller;

import com.vehiclerental.model.Customer;
import com.vehiclerental.service.VehicleRentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Customer operations.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final VehicleRentalService service;

    public CustomerController(VehicleRentalService service) {
        this.service = service;
    }

    /**
     * GET /api/customers
     * Returns all customers (or search results).
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(
            @RequestParam(required = false) String search) {

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(service.searchCustomers(search));
        }
        return ResponseEntity.ok(service.getAllCustomers());
    }

    /**
     * GET /api/customers/{id}
     * Returns a specific customer.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = service.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * POST /api/customers
     * Register a new customer.
     *
     * Request body:
     * {
     *   "customerId": "CUST004",
     *   "name": "John Doe",
     *   "phone": "9876543210",
     *   "email": "john@example.com"
     * }
     */
    @PostMapping
    public ResponseEntity<Customer> registerCustomer(@RequestBody Map<String, String> body) {
        String customerId = body.get("customerId");
        String name = body.get("name");
        String phone = body.get("phone");
        String email = body.get("email");

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (phone.length() < 10) {
            throw new IllegalArgumentException("Phone number must be at least 10 digits.");
        }

        Customer customer = new Customer(customerId, name, phone,
                email != null ? email : "");
        Customer registered = service.registerCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    /**
     * DELETE /api/customers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable String id) {
        Customer customer = service.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Customer " + id + " found. (Delete not implemented in demo mode.)");
        return ResponseEntity.ok(resp);
    }
}
