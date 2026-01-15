package com.example.demo.mapper;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.OrderSummaryDTO;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    // Mapping internal Entity logic to DTO display logic
    @Mapping(target = "productCount", expression = "java(order.getProducts() != null ? order.getProducts().size() : 0)")
    OrderSummaryDTO toOrderSummary(Order order);

    CustomerDTO toCustomerDTO(Customer customer);
}