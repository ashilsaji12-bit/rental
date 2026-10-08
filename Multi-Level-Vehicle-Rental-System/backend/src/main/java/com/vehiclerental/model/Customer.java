package com.vehiclerental.model;

/**
 * OOP Concept: ENCAPSULATION
 * Customer represents a person who rents vehicles.
 * All fields are private with getters/setters.
 */
public class Customer {

    // ENCAPSULATION: private fields
    private String customerId;
    private String name;
    private String phone;
    private String email;

    // Constructor
    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // ENCAPSULATION: Getters and setters

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Phone: %s | Email: %s",
                customerId, name, phone, email);
    }
}
