package com.example.secondhand.dto;

import lombok.Data;

@Data
public class EvaluationCreateRequest {

    private Long orderId;
    private Long fromUserId;
    private Long toUserId;
    private Integer rating;
    private String content;
}

