package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.ProductDTO;
import com.StockInventory.InventoryManagement.dto.StockUpdateRequest;
import com.StockInventory.InventoryManagement.entity.Product;
import com.StockInventory.InventoryManagement.entity.TransactionLog;
import com.StockInventory.InventoryManagement.repository.ProductRepository;
import com.StockInventory.InventoryManagement.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .category("Electronics")
                .brand("Dell")
                .quantity(10)
                .minStockLevel(5)
                .price(BigDecimal.valueOf(80000.0))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setCategory("Electronics");
        dto.setBrand("Dell");
        dto.setQuantity(10);
        dto.setPrice(BigDecimal.valueOf(80000.0));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.addProduct(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateStock() {
        StockUpdateRequest request = new StockUpdateRequest();
        request.setQuantityChange(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updated = productService.updateStock(1L, 2L, request);

        assertEquals(15, updated.getQuantity());
        verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
    }

    @Test
    void testGetLowStockProducts() {
        Product lowStock = Product.builder()
                .id(2L)
                .name("Mouse")
                .quantity(3)
                .minStockLevel(5)
                .build();

        when(productRepository.findAll()).thenReturn(List.of(product, lowStock));

        List<Product> result = productService.getLowStockProducts();

        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }
}
