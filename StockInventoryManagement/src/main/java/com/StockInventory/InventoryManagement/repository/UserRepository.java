package com.StockInventory.InventoryManagement.repository;

import com.StockInventory.InventoryManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByMobileNo(String mobileNo);
}
