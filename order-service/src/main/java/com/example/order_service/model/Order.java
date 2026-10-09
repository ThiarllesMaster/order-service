package com.example.order_service.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;
    private Long customerId;
    private String product;
    private Integer quantity;
    private BigDecimal total;
    private OrderStatus status;
}
