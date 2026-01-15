package com.example.demo.service;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerMapper customerMapper;

    // 1. Get Customer (Optimized)
    public CustomerDTO getCustomer(Long id) {
        Customer customer = customerRepository.findByIdWithOrders(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return customerMapper.toCustomerDTO(customer);
    }

    // 2. Create Order for Customer
    @Transactional
    public void createOrder(Long customerId, CreateOrderRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Fetch all products requested by ID
        List<Product> products = productRepository.findAllById(request.getProductIds());

        if (products.size() != request.getProductIds().size()) {
            throw new RuntimeException("Some products not found");
        }

        // Create Order Entity
        Order order = new Order();
        order.setOrderType(request.getOrderType());
        order.setProductLocation(request.getProductLocation());
        order.setProducts(products); // Associate Many-to-Many

        // Associate using Helper Method (Manages the bidirectional link)
        customer.addOrder(order);

        // Save Customer (Cascade.ALL will save the Order automatically)
        customerRepository.save(customer);
    }
}
