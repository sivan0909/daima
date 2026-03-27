package com.example.secondhand.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {

    private Long userId;
    private Long addressId;
    private String paymentMethod;
    private List<OrderItemRequest> items;
}
