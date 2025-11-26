package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Admin;
import com.StockInventory.InventoryManagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
	
	void deleteByUser(User user);

}
