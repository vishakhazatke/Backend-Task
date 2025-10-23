package com.StockInventory.InventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.StockInventory.InventoryManagement.entity.TransactionLog;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
