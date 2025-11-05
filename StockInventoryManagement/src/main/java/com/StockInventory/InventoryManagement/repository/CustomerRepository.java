package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
