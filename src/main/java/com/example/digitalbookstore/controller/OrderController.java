package com.example.digitalbookstore.controller;

import com.example.digitalbookstore.dto.OrderRequest;
import com.example.digitalbookstore.dto.OrderResponse;
import com.example.digitalbookstore.model.Order;
import com.example.digitalbookstore.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) throws IOException {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(new OrderResponse(order.orderId(), "Order placed successfully."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<OrderResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new OrderResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OrderResponse> handleServerError(Exception ex) {
        log.error("Failed to process order", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new OrderResponse(null, "An unexpected error occurred. Please try again."));
    }
}
