package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.RegisterRequest;
import com.StockInventory.InventoryManagement.entity.Role;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.repository.RoleRepository;
import com.StockInventory.InventoryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user (Customer, Dealer, or Admin)
    public User registerUser(RegisterRequest request) {
        Optional<Role> roleOpt = roleRepository.findByName(request.getRole());
        Role role = roleOpt.orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobileNo(request.getMobileNo());
        user.setAddress(request.getAddress());
        user.setRole(role);
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
