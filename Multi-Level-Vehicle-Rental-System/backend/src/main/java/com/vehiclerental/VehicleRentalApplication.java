package com.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Multi-Level Vehicle Rental System.
 *
 * Run: mvn spring-boot:run
 * Then open: http://localhost:8080
 *
 * For terminal application, run: VehicleRentalApplication --console
 */
@SpringBootApplication
public class VehicleRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleRentalApplication.class, args);
    }
}
