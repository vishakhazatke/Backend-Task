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

    // ✅ Register user (delegates logic to service)
    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO<UserDTO>> register(@RequestBody UserDTO userDTO) {

        User savedUser = userService.registerUser(userDTO);

        // ✅ Build UserDTO for response (don’t expose password)
        UserDTO savedUserDTO = UserDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(null) // hide password
                .mobileNo(savedUser.getMobileNo())
                .address(savedUser.getAddress())
                .roleName(savedUser.getRole().getName())
                .build();


        return ResponseEntity.ok(
                new BaseResponseDTO<>(
                        200,
                        "User registered successfully",
                        savedUserDTO,
                        LocalDateTime.now()
                )
        );
    }

    // ✅ Login (JWT)
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<String>> login(@RequestBody UserDTO userDTO) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

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
