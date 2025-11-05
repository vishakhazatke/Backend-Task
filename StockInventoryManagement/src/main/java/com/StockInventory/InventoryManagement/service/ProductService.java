package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.ProductDTO;
import com.StockInventory.InventoryManagement.dto.StockUpdateRequest;
import com.StockInventory.InventoryManagement.entity.Product;
import com.StockInventory.InventoryManagement.entity.TransactionLog;
import com.StockInventory.InventoryManagement.mapper.Mapper;
import com.StockInventory.InventoryManagement.repository.ProductRepository;
import com.StockInventory.InventoryManagement.repository.TransactionLogRepository;
import com.StockInventory.InventoryManagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final TransactionLogRepository transactionLogRepository;

    public ProductDTO addProduct(ProductDTO dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(Mapper.toProductEntity(dto));
        return Mapper.toProductDTO(saved);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return Mapper.toProductDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(Mapper::toProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setUpdatedAt(LocalDateTime.now());
        return Mapper.toProductDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product updateStock(Long productId, Long dealerId, StockUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int oldQuantity = product.getQuantity();
        product.setQuantity(product.getQuantity() + request.getQuantityChange());
        productRepository.save(product);

        TransactionLog log = TransactionLog.builder()
                .productId(productId)
                .userId(dealerId)
                .changeType(request.getQuantityChange() > 0 ? "INCREASE" : "DECREASE")
                .quantityChanged(request.getQuantityChange())
                .createdAt(LocalDateTime.now())
                .build();
        transactionLogRepository.save(log);

        return product;
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getQuantity() < p.getMinStockLevel())
                .toList();
    }

    public List<TransactionLog> getAllTransactionLogs() {
        return transactionLogRepository.findAll();
    }
}
