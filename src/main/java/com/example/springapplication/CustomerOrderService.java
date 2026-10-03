package com.example.springapplication;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CustomerOrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerOrderService(
            CustomerRepository customerRepository,
            OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public Optional<CustomerWithOrdersDTO> getCustomerWithOrders(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    List<OrderDTO> orders = orderRepository.findAllByCustomerId(customerId).stream()
                            .map(order -> new OrderDTO(
                                    order.getOrderId(),
                                    order.getCustomerId(),
                                    order.getOrderPlacedAt()))
                            .toList();

                    return new CustomerWithOrdersDTO(
                            customer.getId(),
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getPhoneNumber(),
                            customer.getAddLine1(),
                            customer.getAddLine2(),
                            customer.getState(),
                            customer.getCountry(),
                            orders);
                });
    }
}