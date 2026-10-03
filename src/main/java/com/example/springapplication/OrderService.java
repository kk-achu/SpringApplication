package com.example.springapplication;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderNotificationService orderNotificationService;

    public OrderService(
            OrderRepository orderRepository,
            OrderNotificationService orderNotificationService) {
        this.orderRepository = orderRepository;
        this.orderNotificationService = orderNotificationService;
    }

    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getOrderPlacedAt()))
                .toList();
    }

    public OrderDTO createOrder(Long customerId) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomerId(customerId);
        order.setOrderPlacedAt(LocalDateTime.now());

        CustomerOrder savedOrder = orderRepository.save(order);
        orderNotificationService.sendOrderConfirmation(savedOrder.getOrderId(), customerId);

        return new OrderDTO(
                savedOrder.getOrderId(),
                savedOrder.getCustomerId(),
                savedOrder.getOrderPlacedAt());
    }
}