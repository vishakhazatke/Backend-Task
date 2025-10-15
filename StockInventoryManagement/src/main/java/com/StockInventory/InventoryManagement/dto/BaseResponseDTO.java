package com.StockInventory.InventoryManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
