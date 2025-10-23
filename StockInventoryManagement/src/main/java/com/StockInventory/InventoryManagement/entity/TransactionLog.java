package com.StockInventory.InventoryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long userId;

    private String changeType; // "INCREASE" or "DECREASE"
    private int quantityChanged;

    private LocalDateTime createdAt;
}
