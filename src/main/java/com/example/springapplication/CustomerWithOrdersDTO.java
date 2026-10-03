package com.example.springapplication;

import java.util.List;

public record CustomerWithOrdersDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String addLine1,
        String addLine2,
        String state,
        String country,
        List<OrderDTO> orders) {
}