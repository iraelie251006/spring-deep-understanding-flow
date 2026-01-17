package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;           // e.g., 404
    private String message;       // e.g., "User not found"
    private LocalDateTime timestamp;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
