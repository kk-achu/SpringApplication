package com.example.springapplication;

import java.time.LocalDateTime;

public record OrderDTO(
        Long orderid,
        Long customerid,
        LocalDateTime orderPlacedAt) {
}