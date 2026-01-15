package com.example.demo.controller;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PostMapping("/{customerId}/orders")
    public ResponseEntity<Void> createOrder(
            @PathVariable Long customerId,
            @RequestBody CreateOrderRequest request) {

        customerService.createOrder(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
