package com.example.springapplication;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationService.class);

    @Async("taskExecutor")
    public CompletableFuture<Void> sendOrderConfirmation(Long orderId, Long customerId) {
        logger.info("Async order confirmation task started for orderId={} customerId={}",
                orderId,
                customerId);
        return CompletableFuture.completedFuture(null);
    }
}