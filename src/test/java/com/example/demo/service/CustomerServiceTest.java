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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
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
    private CustomerDTO testCustomerDTO;
    private Product testProduct1;
    private Product testProduct2;
    private Order testOrder;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setOrders(new ArrayList<>());

        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setId(1L);
        testCustomerDTO.setName("John Doe");
        testCustomerDTO.setEmail("john.doe@example.com");

        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("Product 1");
        testProduct1.setPrice(100.0);

        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("Product 2");
        testProduct2.setPrice(200.0);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderType("tech");
        testOrder.setProductLocation("Warehouse A");

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderType("tech");
        createOrderRequest.setProductLocation("Warehouse A");
        createOrderRequest.setProductIds(Arrays.asList(1L, 2L));
    }

    @Nested
    @DisplayName("Get Customer Tests")
    class GetCustomerTests {

        @Test
        @DisplayName("Should return customer DTO when customer exists")
        void shouldReturnCustomerDTOWhenCustomerExists() {
            // Given
            Long customerId = 1L;
            when(CustomerServiceTest.this.customerRepository.findByIdWithOrders(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.customerMapper.toCustomerDTO(CustomerServiceTest.this.testCustomer))
                    .thenReturn(CustomerServiceTest.this.testCustomerDTO);

            // When
            CustomerDTO result = CustomerServiceTest.this.customerService.getCustomer(customerId);

            // Then
            assertNotNull(result);
            assertEquals(CustomerServiceTest.this.testCustomerDTO, result);
            assertEquals("John Doe", result.getName());
            assertEquals("john.doe@example.com", result.getEmail());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findByIdWithOrders(customerId);
            verify(CustomerServiceTest.this.customerMapper, times(1)).toCustomerDTO(CustomerServiceTest.this.testCustomer);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when customer not found")
        void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
            // Given
            Long customerId = 999L;
            when(CustomerServiceTest.this.customerRepository.findByIdWithOrders(customerId))
                    .thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> CustomerServiceTest.this.customerService.getCustomer(customerId)
            );

            assertEquals("Customer not found", exception.getMessage());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findByIdWithOrders(customerId);
            verifyNoInteractions(CustomerServiceTest.this.customerMapper);
        }

        @Test
        @DisplayName("Should handle null customer ID")
        void shouldHandleNullCustomerId() {
            // Given
            when(CustomerServiceTest.this.customerRepository.findByIdWithOrders(null))
                    .thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> CustomerServiceTest.this.customerService.getCustomer(null)
            );

            assertEquals("Customer not found", exception.getMessage());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findByIdWithOrders(null);
            verifyNoInteractions(CustomerServiceTest.this.customerMapper);
        }
    }

    @Nested
    @DisplayName("Create Order Tests")
    class CreateOrderTests {

        @Test
        @DisplayName("Should create order successfully when customer and products exist")
        void shouldCreateOrderSuccessfully() {
            // Given
            Long customerId = 1L;
            List<Product> products = Arrays.asList(CustomerServiceTest.this.testProduct1, CustomerServiceTest.this.testProduct2);

            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.productRepository.findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds()))
                    .thenReturn(products);
            when(CustomerServiceTest.this.customerRepository.save(any(Customer.class)))
                    .thenReturn(CustomerServiceTest.this.testCustomer);

            // When
            CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest);

            // Then
            verify(CustomerServiceTest.this.customerRepository, times(1)).findById(customerId);
            verify(CustomerServiceTest.this.productRepository, times(1))
                    .findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds());
            verify(CustomerServiceTest.this.customerRepository, times(1)).save(CustomerServiceTest.this.testCustomer);

            // Verify that customer's addOrder method was called (customer now has orders)
            verify(CustomerServiceTest.this.customerRepository).save(argThat(customer ->
                    customer.getOrders() != null && customer.getOrders().size() == 1
            ));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when customer not found")
        void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
            // Given
            Long customerId = 999L;
            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest)
            );

            assertEquals("Customer not found", exception.getMessage());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findById(customerId);
            verifyNoInteractions(CustomerServiceTest.this.productRepository);
            verify(CustomerServiceTest.this.customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw RuntimeException when some products not found")
        void shouldThrowRuntimeExceptionWhenSomeProductsNotFound() {
            // Given
            Long customerId = 1L;
            // Only return one product instead of two
            List<Product> products = Arrays.asList(CustomerServiceTest.this.testProduct1);

            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.productRepository.findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds()))
                    .thenReturn(products);

            // When & Then
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest)
            );

            assertEquals("Some products not found", exception.getMessage());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findById(customerId);
            verify(CustomerServiceTest.this.productRepository, times(1))
                    .findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds());
            verify(CustomerServiceTest.this.customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw RuntimeException when no products found")
        void shouldThrowRuntimeExceptionWhenNoProductsFound() {
            // Given
            Long customerId = 1L;
            List<Product> emptyProducts = new ArrayList<>();

            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.productRepository.findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds()))
                    .thenReturn(emptyProducts);

            // When & Then
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest)
            );

            assertEquals("Some products not found", exception.getMessage());
            verify(CustomerServiceTest.this.customerRepository, times(1)).findById(customerId);
            verify(CustomerServiceTest.this.productRepository, times(1))
                    .findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds());
            verify(CustomerServiceTest.this.customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should handle empty product list in request")
        void shouldHandleEmptyProductListInRequest() {
            // Given
            Long customerId = 1L;
            CustomerServiceTest.this.createOrderRequest.setProductIds(new ArrayList<>());
            List<Product> emptyProducts = new ArrayList<>();

            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.productRepository.findAllById(anyList()))
                    .thenReturn(emptyProducts);

            // When & Then
            // This should pass the size check (0 == 0) and create an order with no products
            CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest);

            verify(CustomerServiceTest.this.customerRepository, times(1)).findById(customerId);
            verify(CustomerServiceTest.this.productRepository, times(1)).findAllById(anyList());
            verify(CustomerServiceTest.this.customerRepository, times(1)).save(any(Customer.class));
        }

        @Test
        @DisplayName("Should set order properties correctly")
        void shouldSetOrderPropertiesCorrectly() {
            // Given
            Long customerId = 1L;
            List<Product> products = Arrays.asList(CustomerServiceTest.this.testProduct1, CustomerServiceTest.this.testProduct2);

            when(CustomerServiceTest.this.customerRepository.findById(customerId))
                    .thenReturn(Optional.of(CustomerServiceTest.this.testCustomer));
            when(CustomerServiceTest.this.productRepository.findAllById(CustomerServiceTest.this.createOrderRequest.getProductIds()))
                    .thenReturn(products);
            when(CustomerServiceTest.this.customerRepository.save(any(Customer.class)))
                    .thenReturn(CustomerServiceTest.this.testCustomer);

            // When
            CustomerServiceTest.this.customerService.createOrder(customerId, CustomerServiceTest.this.createOrderRequest);

            // Then
            verify(CustomerServiceTest.this.customerRepository).save(argThat(customer -> {
                if (customer.getOrders().isEmpty()) {
                    return false;
                }
                Order order = customer.getOrders().getFirst();
                return order.getOrderType().equals("tech") &&
                        order.getProductLocation().equals("Warehouse A") &&
                        order.getProducts().size() == 2;
            }));
        }
    }
}