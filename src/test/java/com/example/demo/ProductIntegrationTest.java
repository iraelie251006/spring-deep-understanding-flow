package com.example.demo;

import com.example.demo.qahandler.Product;
import com.example.demo.qahandler.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class ProductIntegrationTest {

    private final TestRestTemplate restTemplate;

    private final ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void createAndRetrieveProduct_ShouldWork() {
        // Create product
        Product product = new Product("Integration Test Laptop", 1299.99, 3);

        ResponseEntity<Product> createResponse = restTemplate.postForEntity(
                "/api/products",
                product,
                Product.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        Long productId = createResponse.getBody().getId();

        // Retrieve product
        ResponseEntity<Product> getResponse = restTemplate.getForEntity(
                "/api/products/" + productId,
                Product.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Integration Test Laptop", getResponse.getBody().getName());
    }

    @Test
    void searchProducts_ShouldReturnFilteredResults() {
        // Arrange - create test data
        repository.save(new Product("Gaming Laptop", 1500.0, 5));
        repository.save(new Product("Gaming Mouse", 50.0, 20));
        repository.save(new Product("Office Laptop", 800.0, 10));

        // Act
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                "/api/products/search?name=Gaming",
                Product[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }
}
