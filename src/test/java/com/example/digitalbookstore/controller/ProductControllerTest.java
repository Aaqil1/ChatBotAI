package com.example.digitalbookstore.controller;

import com.example.digitalbookstore.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductControllerTest {

    private final ProductController controller = new ProductController();

    @Test
    void getProductsReturnsStaticCatalog() {
        ResponseEntity<List<Product>> response = controller.getProducts();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody())
                .extracting(Product::id)
                .containsExactly("book-001", "book-002");
    }
}
