package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderSummaryDTO {
    private Long id;
    private String orderType;
    private int productCount; // Calculated field
}