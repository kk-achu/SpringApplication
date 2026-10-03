package com.example.springapplication;

import java.time.Duration;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerOrderCircuitBreakerTest {

    @Test
    void opensAfterRepeatedDatabaseFailures() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(customerRepository.findById(1L))
                .thenThrow(new DataAccessResourceFailureException("Database unavailable"));

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(2)
                .minimumNumberOfCalls(2)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMinutes(1))
                .recordExceptions(DataAccessResourceFailureException.class)
                .build();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(10)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ZERO)
                .build();

        CustomerOrderService service = new CustomerOrderService(
                customerRepository,
                orderRepository,
                RateLimiterRegistry.of(rateLimiterConfig),
                CircuitBreakerRegistry.of(circuitBreakerConfig),
                ObservationRegistry.create());

        assertThrows(DataAccessResourceFailureException.class,
                () -> service.getCustomerWithOrders(1L));
        assertThrows(DataAccessResourceFailureException.class,
                () -> service.getCustomerWithOrders(1L));
        assertThrows(CallNotPermittedException.class,
                () -> service.getCustomerWithOrders(1L));

        verify(customerRepository, times(2)).findById(1L);
    }
}