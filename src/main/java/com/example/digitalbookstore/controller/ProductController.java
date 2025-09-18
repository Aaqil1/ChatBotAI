package com.example.digitalbookstore.controller;

import com.example.digitalbookstore.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<Product> products = List.of(
            new Product(
                    "book-001",
                    "The Minimalist Coder",
                    "Avery Stone",
                    "A practical guide to building clean, maintainable software with minimalist principles.",
                    "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=800&q=80",
                    14.99
            ),
            new Product(
                    "book-002",
                    "Designing Calm Interfaces",
                    "Jordan Rivers",
                    "Discover strategies for creating serene digital experiences through mindful design.",
                    "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=800&q=80",
                    19.99
            )
    );

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(products);
    }
}
