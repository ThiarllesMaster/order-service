package com.example.order_service.repository;

import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "orders";

    public OrderRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // CREATE
    public Order save(Order order) {

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", AttributeValue.builder()
                .s(order.getId())
                .build());

        item.put("customerId", AttributeValue.builder()
                .n(order.getCustomerId().toString())
                .build());

        item.put("product", AttributeValue.builder()
                .s(order.getProduct())
                .build());

        item.put("quantity", AttributeValue.builder()
                .n(order.getQuantity().toString())
                .build());

        item.put("total", AttributeValue.builder()
                .n(order.getTotal().toString())
                .build());

        item.put("status", AttributeValue.builder()
                .s(order.getStatus().name())
                .build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);

        return order;
    }

    // READ ALL
    public List<Order> findAll() {

        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        ScanResponse response = dynamoDbClient.scan(request);

        return response.items()
                .stream()
                .map(this::mapToOrder)
                .toList();
    }

    // READ BY ID
    public Order findById(String id) {

        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder()
                        .s(id)
                        .build()
        );

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem()) {
            throw new RuntimeException("Order not found: " + id);
        }

        return mapToOrder(response.item());
    }

    // DELETE
    public void deleteById(String id) {

        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder()
                        .s(id)
                        .build()
        );

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        dynamoDbClient.deleteItem(request);
    }

    // DynamoDB -> Order

    private Order mapToOrder(Map<String, AttributeValue> item) {

        AttributeValue totalAttribute = item.get("total");

        BigDecimal total = BigDecimal.ZERO;

        if (totalAttribute != null) {
            if (totalAttribute.s() != null) {
                total = new BigDecimal(totalAttribute.s());
            } else if (totalAttribute.n() != null) {
                total = new BigDecimal(totalAttribute.n());
            }
        }

        return new Order(
                item.get("id").s(),
                item.get("customerId").n() != null
                        ? Long.valueOf(item.get("customerId").n())
                        : null,
                item.get("product").s(),
                Integer.valueOf(item.get("quantity").n()),
                total,
                OrderStatus.valueOf(item.get("status").s())
        );
    }

    public Order update(Order order) {

        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder()
                        .s(order.getId())
                        .build()
        );

        Map<String, AttributeValue> expressionValues = Map.of(
                ":customerId", AttributeValue.builder()
                        .n(order.getCustomerId().toString())
                        .build(),

                ":product", AttributeValue.builder()
                        .s(order.getProduct())
                        .build(),

                ":quantity", AttributeValue.builder()
                        .n(order.getQuantity().toString())
                        .build(),

                ":total", AttributeValue.builder()
                        .s(order.getTotal().toString())
                        .build(),

                ":status", AttributeValue.builder()
                        .s(order.getStatus().name())
                        .build()
        );

        Map<String, String> expressionNames = Map.of(
                "#total", "total",
                "#status", "status"
        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("orders")
                .key(key)
                .updateExpression(
                        "SET customerId = :customerId, " +
                                "product = :product, " +
                                "quantity = :quantity, " +
                                "#total = :total, " +
                                "#status = :status"
                )
                .expressionAttributeNames(expressionNames)
                .expressionAttributeValues(expressionValues)
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        UpdateItemResponse response = dynamoDbClient.updateItem(request);

        return mapToOrder(response.attributes());
    }

}
