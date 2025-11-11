package com.StockInventory.InventoryManagement.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private String brand;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer minStockLevel;
    private Long dealerId;

    // For file upload
    private MultipartFile imageFile; // for uploading
    private byte[] image;            // stored in DB
    private String imageBase64;      // for GET responses

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
