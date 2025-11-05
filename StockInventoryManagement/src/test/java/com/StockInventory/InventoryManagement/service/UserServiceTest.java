package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.UserDTO;
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

    private UserDTO userDTO;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // match your UserDTO constructor order exactly
        userDTO = new UserDTO(
                null,                         
                "Vishakha",                     
                "vishakha@gmail.com",          
                "password123",                  
                "9999999999",                   
                "Pune",                         
                "DEALER",
                "Electronics Shop",
                "GSTIN1234"
        );

        role = Role.builder()
                .id(3L)
                .name("DEALER")
                .build();

        user = User.builder()
                .id("U1")
                .name("Vishakha")
                .email("vishakha@gmail.com")
                .password("encodedPassword")
                .mobileNo("9999999999")
                .address("Pune")
                .role(role)
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        when(roleRepository.findByName("DEALER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(userDTO);

        assertNotNull(savedUser);
        assertEquals("Vishakha", savedUser.getName());
        assertEquals("DEALER", savedUser.getRole().getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_RoleNotFound() {
        when(roleRepository.findByName("DEALER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(userDTO)
        );

        assertTrue(exception.getMessage().contains("Role not found"));
    }
}
