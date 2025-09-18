package com.example.digitalbookstore.dto;

import com.example.digitalbookstore.model.CustomerInfo;
import com.example.digitalbookstore.model.OrderItem;

import java.util.List;

public record OrderRequest(
        List<OrderItem> items,
        CustomerInfo customer
) {
}
