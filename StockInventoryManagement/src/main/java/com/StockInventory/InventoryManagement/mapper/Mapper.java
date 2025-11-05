package com.StockInventory.InventoryManagement.mapper;

import com.StockInventory.InventoryManagement.entity.*;
import com.StockInventory.InventoryManagement.dto.*;

public class Mapper {

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
        		.id(String.valueOf(user.getId()))
                .name(user.getName())
                .email(user.getEmail())
                .mobileNo(user.getMobileNo())
                .address(user.getAddress())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    public static ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .brand(product.getBrand())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .minStockLevel(product.getMinStockLevel())
                .dealerId(product.getDealerId())
                .build();
    }

        public static Product toProductEntity(ProductDTO dto) {
            return Product.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .brand(dto.getBrand())
                    .description(dto.getDescription())
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .minStockLevel(dto.getMinStockLevel())
                    .createdAt(dto.getCreatedAt())
                    .updatedAt(dto.getUpdatedAt())
                    .dealerId(dto.getDealerId())
                    .build();
        }

}
