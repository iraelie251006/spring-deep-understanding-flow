package com.example.demo.service;

import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void init() {
        this.testCustomer = Customer.builder()
                .id(12L)
                .name("Elie")
                .email("elie@gmail.com")
                .address("Kigali")
                .build();
    }

    @Test
    void shouldGetCustomer() {
//        Given
        final Long customerId = 12L;
//        when
        when(customerRepository.findByIdWithOrders(customerId)).thenReturn(Optional.of(testCustomer));
//        then
        Assertions.assertNotNull();
    }

    @Test
    void getCustomer() {
    }

    @Test
    void createOrder() {
    }
}