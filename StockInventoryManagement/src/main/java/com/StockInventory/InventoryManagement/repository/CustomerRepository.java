package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Customer;
import com.StockInventory.InventoryManagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	void deleteByUser(User user);

}
