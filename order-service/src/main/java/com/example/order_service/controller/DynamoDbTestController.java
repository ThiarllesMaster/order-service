package com.example.order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

@RestController
public class DynamoDbTestController {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbTestController(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @GetMapping("/dynamodb-test")
    public String testDynamoDb() {

        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName("orders")
                .build();

        DescribeTableResponse response =
                dynamoDbClient.describeTable(request);

        return "Tabela: " + response.table().tableName()
                + " | Status: " + response.table().tableStatus();
    }
}
