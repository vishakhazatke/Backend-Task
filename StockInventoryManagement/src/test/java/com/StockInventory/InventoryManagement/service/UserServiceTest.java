package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.RegisterRequest;
import com.StockInventory.InventoryManagement.entity.Role;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.repository.RoleRepository;
import com.StockInventory.InventoryManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest request;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new RegisterRequest("Vishakha", "vishakha@gmail.com", "password123", "9999999999", "Pune", "DEALER");
        role = Role.builder().id(3L).name("ROLE_DEALER").build();
        user = User.builder()
                .id(1L)
                .name("Vishakha")
                .email("vishakha@gmail.com")
                .password("encodedPassword")
                .role(role)
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        when(roleRepository.findByName("ROLE_DEALER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(request);

        assertNotNull(savedUser);
        assertEquals("Vishakha", savedUser.getName());
        assertEquals("ROLE_DEALER", savedUser.getRole().getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_RoleNotFound() {
        when(roleRepository.findByName("ROLE_DEALER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(request)
        );

        assertEquals("Role not found", exception.getMessage());
    }
}
