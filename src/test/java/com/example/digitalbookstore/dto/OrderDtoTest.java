package com.example.digitalbookstore.dto;

import com.example.digitalbookstore.model.CustomerInfo;
import com.example.digitalbookstore.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoTest {

    @Test
    void orderRequestExposesCartAndCustomer() {
        CustomerInfo customer = new CustomerInfo("Jamie Doe", "45 Elm Street", "555-0100");
        List<OrderItem> items = List.of(new OrderItem("book-001", 1, 10.0));

        OrderRequest request = new OrderRequest(items, customer);

        assertThat(request.items()).containsExactlyElementsOf(items);
        assertThat(request.customer()).isEqualTo(customer);
    }

    @Test
    void orderResponseProvidesOrderSummary() {
        OrderResponse response = new OrderResponse("order-123", "Order placed successfully.");

        assertThat(response.orderId()).isEqualTo("order-123");
        assertThat(response.message()).isEqualTo("Order placed successfully.");
        assertThat(response).isEqualTo(new OrderResponse("order-123", "Order placed successfully."));
    }
}
