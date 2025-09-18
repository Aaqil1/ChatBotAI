package com.example.digitalbookstore.service;

import com.example.digitalbookstore.dto.OrderRequest;
import com.example.digitalbookstore.model.CustomerInfo;
import com.example.digitalbookstore.model.Order;
import com.example.digitalbookstore.model.OrderItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {

    private final ObjectMapper objectMapper;
    private final Path ordersFile;
    private final Lock fileLock = new ReentrantLock();

    public OrderService(ObjectMapper objectMapper, @Value("${app.orders.file}") String ordersFilePath) {
        this.objectMapper = objectMapper;
        this.ordersFile = Path.of(ordersFilePath).toAbsolutePath();
        ensureFileExists();
    }

    public Order createOrder(OrderRequest request) throws IOException {
        validateRequest(request);
        String orderId = UUID.randomUUID().toString();
        double total = request.items().stream()
                .mapToDouble(item -> item.price() * item.quantity())
                .sum();
        Order order = new Order(orderId, request.items(), request.customer(), total, Instant.now());
        persistOrder(order);
        return order;
    }

    private void validateRequest(OrderRequest request) {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty.");
        }

        CustomerInfo customer = request.customer();
        if (customer == null
                || isBlank(customer.fullName())
                || isBlank(customer.address())
                || isBlank(customer.phone())) {
            throw new IllegalArgumentException("Customer information is incomplete.");
        }

        for (OrderItem item : request.items()) {
            if (item == null || isBlank(item.productId()) || item.quantity() <= 0 || item.price() < 0) {
                throw new IllegalArgumentException("Invalid cart item provided.");
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void ensureFileExists() {
        fileLock.lock();
        try {
            if (Files.notExists(ordersFile)) {
                Files.createDirectories(ordersFile.getParent());
                Files.writeString(ordersFile, "[]");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize orders storage", e);
        } finally {
            fileLock.unlock();
        }
    }

    private void persistOrder(Order order) throws IOException {
        fileLock.lock();
        try {
            List<Order> existingOrders = readOrders();
            existingOrders.add(order);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(ordersFile.toFile(), existingOrders);
        } finally {
            fileLock.unlock();
        }
    }

    private List<Order> readOrders() throws IOException {
        if (Files.size(ordersFile) == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(ordersFile.toFile(), new TypeReference<>() {});
    }
}
