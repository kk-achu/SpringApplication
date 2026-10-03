package com.example.springapplication;

public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String addLine1,
        String addLine2,
        String state,
        String country) {

    public static CustomerDTO sample() {
        return new CustomerDTO(
                1L,
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "555-0100",
                "123 Main Street",
                "Apartment 4",
                "California",
                "USA");
    }
}