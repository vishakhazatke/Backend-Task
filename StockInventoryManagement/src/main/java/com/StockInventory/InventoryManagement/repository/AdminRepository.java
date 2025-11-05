package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
