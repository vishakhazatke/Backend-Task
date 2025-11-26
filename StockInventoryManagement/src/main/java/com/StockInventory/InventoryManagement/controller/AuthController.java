package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.dto.BaseResponseDTO;
import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.service.UserService;
import com.StockInventory.InventoryManagement.util.JwtUtil;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
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
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponseDTO<Void>> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                new BaseResponseDTO<>(
                        200,
                        "User deleted successfully",
                        null,
                        LocalDateTime.now()
                )
        );
    }
    @PostMapping("/send-otp")
    @PermitAll
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendOtp(email));
    }

    @PostMapping("/verify-otp")
    @PermitAll
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyOtp(email, otp));
    }

}