package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.Cart;
import com.StockInventory.InventoryManagement.entity.Product;
import com.StockInventory.InventoryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}
