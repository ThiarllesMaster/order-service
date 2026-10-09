package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(Order order) {
        order.setId(String.valueOf(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(String id) {
        return orderRepository.findById(id);
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    public Order update(String id, Order order) {
        order.setId(id);
        return orderRepository.update(order);
    }
}