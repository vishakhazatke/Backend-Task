package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.entity.Role;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.repository.RoleRepository;
import com.StockInventory.InventoryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Register a new user
    public User registerUser(UserDTO userDTO) {

        // --- Validation ---
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty())
            throw new RuntimeException("Name cannot be empty");

        if (userDTO.getEmail() == null || !userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new RuntimeException("Invalid email format");

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new RuntimeException("Email is already registered");

        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8)
            throw new RuntimeException("Password must be at least 8 characters long");
        if (!userDTO.getPassword().matches(".*[A-Z].*"))
            throw new RuntimeException("Password must contain at least one uppercase letter");
        if (!userDTO.getPassword().matches(".*[a-z].*"))
            throw new RuntimeException("Password must contain at least one lowercase letter");

        // --- Find role ---
        Role role = roleRepository.findByName(userDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDTO.getRoleName()));

        // --- Create user ---
        User user = User.builder()
                .name(userDTO.getName().trim())
                .email(userDTO.getEmail().trim())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .mobileNo(userDTO.getMobileNo().trim())
                .address(userDTO.getAddress() != null ? userDTO.getAddress().trim() : "")
                .role(role)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}
