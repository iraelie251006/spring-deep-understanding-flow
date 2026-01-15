package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String address;

    // One Customer has Many Orders
    // Cascade ALL: If we delete the Customer, their Orders are deleted too.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    // One Customer has Many Invoices
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    // Helper method for Bidirectional Sync
    public void addOrder(Order order) {
        if (orders == null) orders = new ArrayList<>();
        orders.add(order);
        order.setCustomer(this);
    }
}
