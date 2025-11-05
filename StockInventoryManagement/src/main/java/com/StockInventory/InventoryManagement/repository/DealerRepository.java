package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerRepository extends JpaRepository<Dealer, String> {
}
