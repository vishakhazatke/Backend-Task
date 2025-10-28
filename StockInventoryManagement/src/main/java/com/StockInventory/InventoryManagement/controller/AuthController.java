package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.dto.BaseResponseDTO;
import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.service.UserService;
import com.StockInventory.InventoryManagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO<Void>> register(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);

        return ResponseEntity.ok(
                new BaseResponseDTO<>(
                        200,
                        "User registered successfully",
                        null, 
                        LocalDateTime.now()
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<String>> login(@RequestBody UserDTO userDTO) {
        userService.loginUser(userDTO);

        String token = jwtUtil.generateToken(userDTO.getEmail());

        return ResponseEntity.ok(
                new BaseResponseDTO<>(
                        200,
                        "Login successful",
                        token,
                        LocalDateTime.now()
                )
        );
    }
}