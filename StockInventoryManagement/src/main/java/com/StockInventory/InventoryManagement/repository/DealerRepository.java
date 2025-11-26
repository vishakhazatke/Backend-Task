package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Dealer;
import com.StockInventory.InventoryManagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerRepository extends JpaRepository<Dealer, String> {
	
	void deleteByUser(User user);

}
