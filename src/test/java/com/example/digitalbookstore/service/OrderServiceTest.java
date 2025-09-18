package com.example.digitalbookstore.service;

import com.example.digitalbookstore.dto.OrderRequest;
import com.example.digitalbookstore.model.CustomerInfo;
import com.example.digitalbookstore.model.Order;
import com.example.digitalbookstore.model.OrderItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {

    @TempDir
    Path tempDir;

    private Path ordersFile;
    private ObjectMapper objectMapper;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        ordersFile = tempDir.resolve("orders.json");
        orderService = new OrderService(objectMapper, ordersFile.toString());
    }

    @Test
    void createOrderGeneratesIdPersistsAndCalculatesTotal() throws IOException {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItem("book-001", 2, 14.99)),
                new CustomerInfo("Morgan", "10 Downing Street", "1234567890")
        );

        Order order = orderService.createOrder(request);

        assertThat(order.orderId()).isNotBlank();
        assertThat(order.total()).isEqualTo(29.98);
        assertThat(order.items()).hasSize(1);
        assertThat(order.customer().fullName()).isEqualTo("Morgan");
        assertThat(Files.exists(ordersFile)).isTrue();

        List<Order> persisted = objectMapper.readValue(ordersFile.toFile(), new TypeReference<>() {});
        assertThat(persisted).hasSize(1);
        assertThat(persisted.get(0).orderId()).isEqualTo(order.orderId());
    }

    @Test
    void createOrderRejectsEmptyCart() {
        OrderRequest request = new OrderRequest(List.of(), new CustomerInfo("Morgan", "10 Downing", "123"));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cart cannot be empty");
    }

    @Test
    void createOrderRejectsMissingCustomer() {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItem("book-001", 1, 9.99)),
                new CustomerInfo(" ", "", null)
        );

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer information is incomplete");
    }

    @Test
    void createOrderRejectsInvalidItem() {
        OrderRequest request = new OrderRequest(
                List.of(new OrderItem("book-001", 0, 9.99)),
                new CustomerInfo("Morgan", "10 Downing", "123")
        );

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid cart item");
    }
}
