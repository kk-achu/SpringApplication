package com.example.springapplication;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CustomerOrder")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Long orderId;

    @Column(name = "customerid", nullable = false)
    private Long customerId;

    @Column(name = "orderPlacedAt", nullable = false)
    private LocalDateTime orderPlacedAt;

    public CustomerOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderPlacedAt() {
        return orderPlacedAt;
    }

    public void setOrderPlacedAt(LocalDateTime orderPlacedAt) {
        this.orderPlacedAt = orderPlacedAt;
    }
}