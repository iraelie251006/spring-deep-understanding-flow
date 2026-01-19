package com.example.demo;

import com.example.demo.qahandler.Product;
import com.example.demo.qahandler.ProductRepository;
import com.example.demo.qahandler.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        // Arrange
        Product product = new Product("Laptop", 999.99, 10);
        when(repository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = service.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(repository, times(1)).save(product);
    }

    @Test
    void createProduct_WithNegativePrice_ShouldThrowException() {
        // Arrange
        Product product = new Product("Laptop", -100.0, 10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.createProduct(product);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("Laptop", 999.99, 10),
                new Product("Mouse", 25.99, 50)
        );
        when(repository.findAll()).thenReturn(products);

        // Act
        List<Product> result = service.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}
