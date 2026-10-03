package com.example.springapplication;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getOrderPlacedAt()))
                .toList();
    }
}