package com.example.digitalbookstore.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ModelTest {

    @Test
    void productRecordHoldsMetadata() {
        Product product = new Product("book-001", "Title", "Author", "Description", "http://image", 12.99);

        assertThat(product.id()).isEqualTo("book-001");
        assertThat(product.title()).isEqualTo("Title");
        assertThat(product.author()).isEqualTo("Author");
        assertThat(product.description()).isEqualTo("Description");
        assertThat(product.imageUrl()).isEqualTo("http://image");
        assertThat(product.price()).isEqualTo(12.99);
    }

    @Test
    void customerInfoRecordHoldsContactDetails() {
        CustomerInfo customer = new CustomerInfo("Casey", "221B Baker Street", "555-0199");

        assertThat(customer.fullName()).isEqualTo("Casey");
        assertThat(customer.address()).isEqualTo("221B Baker Street");
        assertThat(customer.phone()).isEqualTo("555-0199");
    }

    @Test
    void orderItemRecordDescribesCartLine() {
        OrderItem item = new OrderItem("book-002", 3, 19.99);

        assertThat(item.productId()).isEqualTo("book-002");
        assertThat(item.quantity()).isEqualTo(3);
        assertThat(item.price()).isEqualTo(19.99);
    }

    @Test
    void orderRecordAggregatesCartAndCustomer() {
        CustomerInfo customer = new CustomerInfo("Alex", "42 Galaxy Way", "555-4242");
        OrderItem item = new OrderItem("book-003", 1, 9.99);
        Instant now = Instant.now();

        Order order = new Order("order-123", List.of(item), customer, 9.99, now);

        assertThat(order.orderId()).isEqualTo("order-123");
        assertThat(order.items()).containsExactly(item);
        assertThat(order.customer()).isEqualTo(customer);
        assertThat(order.total()).isEqualTo(9.99);
        assertThat(order.createdAt()).isEqualTo(now);
    }
}
