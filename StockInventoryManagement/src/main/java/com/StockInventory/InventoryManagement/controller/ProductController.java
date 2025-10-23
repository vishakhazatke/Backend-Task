package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.dto.BaseResponseDTO;
import com.StockInventory.InventoryManagement.dto.ProductDTO;
import com.StockInventory.InventoryManagement.dto.StockUpdateRequest;
import com.StockInventory.InventoryManagement.entity.Product;
import com.StockInventory.InventoryManagement.entity.TransactionLog;
import com.StockInventory.InventoryManagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponseDTO<ProductDTO>> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.addProduct(productDTO);
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Product added successfully",
                null,
                LocalDateTime.now()
            )
        );
    }

 // All roles
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<ProductDTO>> getProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Product fetched successfully",
                productDTO,
                LocalDateTime.now()
            )
        );
    }


    // ✅ All roles
    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "All products fetched successfully",
                products,
                LocalDateTime.now()
            )
        );
    }

    // ✅ Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        ProductDTO updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Product updated successfully",
                null,
                LocalDateTime.now()
            )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


    // ✅ Update stock (Dealer Only)
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('DEALER')")
    public ResponseEntity<BaseResponseDTO<Product>> updateStock(@PathVariable Long id,
                                                                @RequestBody StockUpdateRequest request,
                                                                @RequestParam Long dealerId) {
        Product updatedProduct = productService.updateStock(id, dealerId, request);
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Stock updated successfully",
                null,
                LocalDateTime.now()
            )
        );
    }

    // ✅ Low stock products (Admin Only)
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<List<Product>>> getLowStockProducts() {
        List<Product> lowStock = productService.getLowStockProducts();
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Low stock products fetched successfully",
                lowStock,
                LocalDateTime.now()
            )
        );
    }

    // ✅ Transaction logs (Admin Only)
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponseDTO<List<TransactionLog>>> getTransactionLogs() {
        List<TransactionLog> logs = productService.getAllTransactionLogs();
        return ResponseEntity.ok(
            new BaseResponseDTO<>(
                200,
                "Transaction logs fetched successfully",
                logs,
                LocalDateTime.now()
            )
        );
    }
}
