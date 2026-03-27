package com.example.secondhand.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
}
