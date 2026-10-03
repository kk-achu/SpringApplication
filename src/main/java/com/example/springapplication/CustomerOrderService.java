package com.example.springapplication;

import java.util.List;
import java.util.Optional;
import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrderService {

        private static final long MAX_CUSTOMER_LIMITERS = 10_000;
        private static final Duration LIMITER_IDLE_EXPIRY = Duration.ofHours(1);

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
        private final RateLimiterConfig customerRateLimiterConfig;
        private final Cache<Long, RateLimiter> customerRateLimiters;

    public CustomerOrderService(
            CustomerRepository customerRepository,
                        OrderRepository orderRepository,
                        RateLimiterRegistry rateLimiterRegistry) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
                this.customerRateLimiterConfig = rateLimiterRegistry
                                .rateLimiter("customerOrder")
                                .getRateLimiterConfig();
                this.customerRateLimiters = Caffeine.newBuilder()
                                .maximumSize(MAX_CUSTOMER_LIMITERS)
                                .expireAfterAccess(LIMITER_IDLE_EXPIRY)
                                .build();
    }

    public Optional<CustomerWithOrdersDTO> getCustomerWithOrders(Long customerId) {
                checkRateLimit(customerId);
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

    public Optional<CustomerWithOrdersDTO> getCustomerWithOrdersSql(Long customerId) {
        checkRateLimit(customerId);
        List<CustomerOrderQueryRow> rows = orderRepository.findCustomerWithOrdersNative(customerId);
        if (rows.isEmpty()) {
            return Optional.empty();
        }

        CustomerOrderQueryRow customer = rows.get(0);
        List<OrderDTO> orders = rows.stream()
                .filter(row -> row.getOrderId() != null)
                .map(row -> new OrderDTO(
                        row.getOrderId(),
                        row.getOrderCustomerId(),
                        row.getOrderPlacedAt()))
                .toList();

        return Optional.of(new CustomerWithOrdersDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddLine1(),
                customer.getAddLine2(),
                customer.getState(),
                customer.getCountry(),
                orders));
    }

    private void checkRateLimit(Long customerId) {
        RateLimiter rateLimiter = customerRateLimiters.get(customerId,
                id -> RateLimiter.of("customerOrder-" + id, customerRateLimiterConfig));
        if (!rateLimiter.acquirePermission()) {
            throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
        }
    }
}