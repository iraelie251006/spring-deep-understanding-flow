package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    // We only return IDs or simplified objects, not full Entities
    private List<OrderSummaryDTO> orders;
}
