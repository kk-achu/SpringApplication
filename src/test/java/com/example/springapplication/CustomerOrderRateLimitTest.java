package com.example.springapplication;

import java.time.Duration;
import java.util.Optional;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerOrderRateLimitTest {

    @Test
    void appliesIndependentLimitsPerCustomerAcrossBothLookupMethods() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        CustomerOrderService service = new CustomerOrderService(
                customerRepository,
                orderRepository,
                RateLimiterRegistry.of(config));

        assertTrue(service.getCustomerWithOrders(1L).isEmpty());
        assertThrows(RequestNotPermitted.class, () -> service.getCustomerWithOrdersSql(1L));
        assertTrue(service.getCustomerWithOrders(2L).isEmpty());
    }
}