package com.example.digitalbookstore.model;

public record OrderItem(
        String productId,
        int quantity,
        double price
) {
}
