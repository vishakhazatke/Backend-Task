package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.mapper.Mapper;
import com.StockInventory.InventoryManagement.repository.RoleRepository;
import com.StockInventory.InventoryManagement.repository.UserRepository;
import com.StockInventory.InventoryManagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() != null) {
            user.setRole(roleRepository.findByName(user.getRole().getName()).orElseThrow());
        }
        User saved = userRepository.save(user);
        return Mapper.toUserDTO(saved);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        return jwtUtil.generateToken(user.getEmail());
    }
}
