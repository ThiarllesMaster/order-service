package com.example.order_service.controller;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = new Order(
                null,
                request.customerId(),
                request.product(),
                request.quantity(),
                BigDecimal.ZERO,
                OrderStatus.PENDING
        );

        Order savedOrder = orderService.create(order);

        return toResponse(savedOrder);
    }

    @GetMapping
    public List<OrderResponse> findAll() {

        return orderService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable String id) {

        return toResponse(orderService.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {

        orderService.deleteById(id);
    }

    @PutMapping("/{id}")
    public OrderResponse update(
            @PathVariable String id,
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = new Order(
                id,
                request.customerId(),
                request.product(),
                request.quantity(),
                BigDecimal.ZERO,
                OrderStatus.PENDING
        );

        Order updatedOrder = orderService.update(id, order);

        return toResponse(updatedOrder);
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getProduct(),
                order.getQuantity(),
                order.getTotal(),
                order.getStatus()
        );
    }
}