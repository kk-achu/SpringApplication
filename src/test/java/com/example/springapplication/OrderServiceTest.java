package com.example.springapplication;

import org.mockito.InOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Test
    void savesOrderBeforeStartingConfirmationTask() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderNotificationService notificationService = mock(OrderNotificationService.class);
        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> {
            CustomerOrder order = invocation.getArgument(0);
            order.setOrderId(17L);
            return order;
        });
        OrderService orderService = new OrderService(orderRepository, notificationService);

        OrderDTO createdOrder = orderService.createOrder(42L);

        assertEquals(17L, createdOrder.orderid());
        assertEquals(42L, createdOrder.customerid());
        InOrder sequence = inOrder(orderRepository, notificationService);
        sequence.verify(orderRepository).save(any(CustomerOrder.class));
        sequence.verify(notificationService).sendOrderConfirmation(17L, 42L);
    }
}