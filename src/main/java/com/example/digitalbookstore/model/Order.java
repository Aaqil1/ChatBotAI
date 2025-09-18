package com.example.digitalbookstore.model;

import java.time.Instant;
import java.util.List;

public record Order(
        String orderId,
        List<OrderItem> items,
        CustomerInfo customer,
        double total,
        Instant createdAt
) {
}
