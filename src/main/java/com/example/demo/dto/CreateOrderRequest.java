package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String orderType;
    private String productLocation;
    private List<Long> productIds; // User sends IDs, not Product objects
}
