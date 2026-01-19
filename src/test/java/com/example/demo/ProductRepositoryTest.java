package com.example.demo;

import com.example.demo.qahandler.Product;
import com.example.demo.qahandler.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@RequiredArgsConstructor
class ProductRepositoryTest {

    private final TestEntityManager entityManager;

    private final ProductRepository repository;

    @Test
    void findByNameContaining_ShouldReturnMatchingProducts() {
        // Arrange
        Product laptop = new Product("Gaming Laptop", 1500.0, 5);
        Product mouse = new Product("Gaming Mouse", 50.0, 20);
        entityManager.persist(laptop);
        entityManager.persist(mouse);
        entityManager.flush();

        // Act
        List<Product> results = repository.findByNameContaining("Gaming");

        // Assert
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(p -> p.getName().contains("Gaming")));
    }

    @Test
    void findByPriceLessThan_ShouldReturnCheaperProducts() {
        // Arrange
        entityManager.persist(new Product("Laptop", 1500.0, 5));
        entityManager.persist(new Product("Mouse", 50.0, 20));
        entityManager.persist(new Product("Keyboard", 80.0, 15));
        entityManager.flush();

        // Act
        List<Product> results = repository.findByPriceLessThan(100.0);

        // Assert
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(p -> p.getPrice() < 100.0));
    }
}