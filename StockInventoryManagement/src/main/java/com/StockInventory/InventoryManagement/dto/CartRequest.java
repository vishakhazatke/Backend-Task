package com.StockInventory.InventoryManagement.dto;

import lombok.Data;

@Data
public class CartRequest {
    private String customerId;   
    private Long productId;      
    private int quantity;       
}
