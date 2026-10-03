package com.example.springapplication;

import java.time.LocalDateTime;

public interface CustomerOrderQueryRow {

    Long getCustomerId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhoneNumber();

    String getAddLine1();

    String getAddLine2();

    String getState();

    String getCountry();

    Long getOrderId();

    Long getOrderCustomerId();

    LocalDateTime getOrderPlacedAt();
}