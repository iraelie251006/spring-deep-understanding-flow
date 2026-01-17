package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private int status;           // e.g., 404
    private String message;       // e.g., "User not found"
    private String error;         // e.g., Error type like Not Found
    private String path;          // Endpoint that caused error

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private List<String> details;  // For validation errors

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ApiError(int status, String message, String error) {
        this(status, message);
        this.error = error;
    }

    public ApiError(int status, String message, String error, String path) {
        this(status, message, error);
        this.path = path;
    }
}
