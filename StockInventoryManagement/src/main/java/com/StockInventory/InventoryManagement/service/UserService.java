package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.entity.*;
import com.StockInventory.InventoryManagement.repository.*;
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

    private final AdminRepository adminRepository;
    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;

    public User registerUser(UserDTO userDTO) {

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

        Role role = roleRepository.findByName(userDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDTO.getRoleName()));

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

        User savedUser = userRepository.save(user);
        
        String roleName = role.getName().toUpperCase();

        switch (roleName) {
        case "ADMIN":
            adminRepository.save(
                    Admin.builder()
                            .user(savedUser)
                            .name(savedUser.getName())
                            .email(savedUser.getEmail())
                            .password(savedUser.getPassword())
                            .mobileNo(savedUser.getMobileNo())
                            .address(savedUser.getAddress())
                            .status(savedUser.getStatus())           
                            .createdAt(savedUser.getCreatedAt())     
                            .updatedAt(savedUser.getUpdatedAt())     
                            .role(savedUser.getRole())
                            .build()
            );
            break;

        case "DEALER":
            dealerRepository.save(
                    Dealer.builder()
                            .user(savedUser)
                            .name(savedUser.getName())
                            .email(savedUser.getEmail())
                            .password(savedUser.getPassword())
                            .mobileNo(savedUser.getMobileNo())
                            .address(savedUser.getAddress())
                            .status(savedUser.getStatus())           
                            .createdAt(savedUser.getCreatedAt())     
                            .updatedAt(savedUser.getUpdatedAt())     
                            .role(savedUser.getRole())
                            .build()
            );
            break;

        case "CUSTOMER":
            customerRepository.save(
                    Customer.builder()
                            .user(savedUser)
                            .name(savedUser.getName())
                            .email(savedUser.getEmail())
                            .password(savedUser.getPassword())
                            .mobileNo(savedUser.getMobileNo())
                            .address(savedUser.getAddress())
                            .status(savedUser.getStatus())           
                            .createdAt(savedUser.getCreatedAt())    
                            .updatedAt(savedUser.getUpdatedAt())     
                            .role(savedUser.getRole())
                            .build()
            );
            break;

        default:
            throw new RuntimeException("Unsupported role: " + roleName);
        }
        return savedUser;
    }
    public String loginUser(UserDTO userDTO) {
        
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty())
            throw new RuntimeException("Email cannot be empty");

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new RuntimeException("Invalid email format");

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty())
            throw new RuntimeException("Password cannot be empty");

        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDTO.getEmail()));

        if (!passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword()))
            throw new RuntimeException("Invalid password");

        return "Login successful for " + existingUser.getRole().getName() + ": " + existingUser.getName();
    }

}
