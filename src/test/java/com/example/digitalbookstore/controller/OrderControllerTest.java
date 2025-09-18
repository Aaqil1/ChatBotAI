package com.example.digitalbookstore.controller;

import com.example.digitalbookstore.dto.OrderRequest;
import com.example.digitalbookstore.dto.OrderResponse;
import com.example.digitalbookstore.model.CustomerInfo;
import com.example.digitalbookstore.model.Order;
import com.example.digitalbookstore.model.OrderItem;
import com.example.digitalbookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    private OrderService orderService;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        controller = new OrderController(orderService);
    }

    @Test
    void createOrderReturnsSuccessfulResponse() throws IOException {
        CustomerInfo customer = new CustomerInfo("Jordan Taylor", "123 Market Street", "+1-555-1234");
        OrderItem item = new OrderItem("book-001", 2, 14.99);
        OrderRequest request = new OrderRequest(List.of(item), customer);
        Order order = new Order("order-123", request.items(), request.customer(), 29.98, Instant.now());
        when(orderService.createOrder(request)).thenReturn(order);

        ResponseEntity<OrderResponse> response = controller.createOrder(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().orderId()).isEqualTo("order-123");
        assertThat(response.getBody().message()).contains("successfully");
        Mockito.verify(orderService).createOrder(request);
    }

    @Test
    void handleBadRequestReturnsClientError() {
        ResponseEntity<OrderResponse> response = controller.handleBadRequest(new IllegalArgumentException("invalid"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("invalid");
        assertThat(response.getBody().orderId()).isNull();
    }

    @Test
    void handleServerErrorReturnsGenericMessage() {
        ResponseEntity<OrderResponse> response = controller.handleServerError(new RuntimeException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().orderId()).isNull();
        assertThat(response.getBody().message()).contains("unexpected error");
    }
}
