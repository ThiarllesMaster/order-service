package com.example.order_service.dto;

import com.example.order_service.model.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        String id,
        Long customerId,
        String product,
        Integer quantity,
        BigDecimal total,
        OrderStatus status
) {
}