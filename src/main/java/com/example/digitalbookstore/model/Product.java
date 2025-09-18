package com.example.digitalbookstore.model;

public record Product(
        String id,
        String title,
        String author,
        String description,
        String imageUrl,
        double price
) {
}
