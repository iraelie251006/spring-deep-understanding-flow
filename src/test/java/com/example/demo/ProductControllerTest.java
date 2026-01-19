package com.example.demo;

import com.example.demo.qahandler.Product;
import com.example.demo.qahandler.ProductController;
import com.example.demo.qahandler.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("Laptop", 999.99, 10),
                new Product("Mouse", 25.99, 50)
        );
        when(service.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));
    }

    @Test
    void getProduct_WhenExists_ShouldReturnProduct() throws Exception {
        // Arrange
        Product product = new Product("Laptop", 999.99, 10);
        when(service.getProductById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void getProduct_WhenNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(service.getProductById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        Product product = new Product("Laptop", 999.99, 10);
        when(service.createProduct(any(Product.class))).thenReturn(product);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":999.99,\"quantity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
}

