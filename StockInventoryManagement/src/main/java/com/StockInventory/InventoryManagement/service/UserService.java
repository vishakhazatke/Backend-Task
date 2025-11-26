package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.dto.UserDTO;
import com.StockInventory.InventoryManagement.entity.*;
import com.StockInventory.InventoryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final AdminRepository adminRepository;
    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;

    public User registerUser(UserDTO userDTO) {

        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty())
            throw new RuntimeException("Name cannot be empty");
        String name = userDTO.getName().trim();

        if(!name.matches("^[A-Za-z]+$"))
            throw new RuntimeException("Name must contain only Alphabets. No numbers or special characters allowed");

        if (userDTO.getEmail() == null 
                || !userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@gmail\\.com$")) {
            throw new RuntimeException("Invalid Email. Only @gmail.com allowed.");
        }

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent() 
                && !userRepository.findByEmail(userDTO.getEmail()).get().getIsVerified()) {
            throw new RuntimeException("Please verify OTP before registration");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new RuntimeException("Email is already registered");

        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8)
            throw new RuntimeException("Password must be at least 8 characters long");
        if (!userDTO.getPassword().matches(".*[A-Z].*"))
            throw new RuntimeException("Password must contain at least one uppercase letter");
        if (!userDTO.getPassword().matches(".*[a-z].*"))
            throw new RuntimeException("Password must contain at least one lowercase letter");
        if(!userDTO.getPassword().matches(".*[!@#$%^&*()_+=\\-{}|:;<>?,./].*"))
            throw new RuntimeException("Password must contain at least one Special Character");

        if (userDTO.getMobileNo() == null 
                || !userDTO.getMobileNo().matches("^[6-9][0-9]{9}$")) {
            throw new RuntimeException("Invalid Mobile Number. Must be 10 digits and start with 6, 7, 8, or 9.");
        }


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
                .isVerified(true)
                .build();

        long count = userRepository.count() + 1;
        user.setId("U" + count);

        User savedUser = userRepository.save(user);

        String roleName = role.getName().toUpperCase();

        switch (roleName) {

            case "ADMIN":
                long adminCount = adminRepository.count() + 1;
                String adminId = "A" + adminCount;
                adminRepository.save(
                        Admin.builder()
                                .adminId(adminId)
                                .user(savedUser)
                                .name(savedUser.getName())
                                .email(savedUser.getEmail())
                                .password(savedUser.getPassword())
                                .mobileNo(savedUser.getMobileNo())
                                .address(savedUser.getAddress())
                                .status(savedUser.getStatus())
                                .createdAt(savedUser.getCreatedAt())
                                .updatedAt(savedUser.getUpdatedAt())
                                .build()
                );
                break;

            case "DEALER":
                long dealerCount = dealerRepository.count() + 1;
                String dealerId = "D" + dealerCount;
                dealerRepository.save(
                        Dealer.builder()
                                .dealerId(dealerId)
                                .user(savedUser)
                                .name(savedUser.getName())
                                .email(savedUser.getEmail())
                                .password(savedUser.getPassword())
                                .mobileNo(savedUser.getMobileNo())
                                .address(savedUser.getAddress())
                                .status(savedUser.getStatus())
                                .createdAt(savedUser.getCreatedAt())
                                .updatedAt(savedUser.getUpdatedAt())
                                .shopName(userDTO.getShopName() != null ? userDTO.getShopName().trim() : null)
                                .gstNumber(userDTO.getGstNumber() != null ? userDTO.getGstNumber().trim() : null)
                                .build()
                );
                break;

            case "CUSTOMER":
                long customerCount = customerRepository.count() + 1;
                String customerId = "C" + customerCount;
                customerRepository.save(
                        Customer.builder()
                                .customerId(customerId)
                                .user(savedUser)
                                .name(savedUser.getName())
                                .email(savedUser.getEmail())
                                .password(savedUser.getPassword())
                                .mobileNo(savedUser.getMobileNo())
                                .address(savedUser.getAddress())
                                .status(savedUser.getStatus())
                                .createdAt(savedUser.getCreatedAt())
                                .updatedAt(savedUser.getUpdatedAt())
                                .build()
                );
                break;

            default:
                throw new RuntimeException("Unsupported role: " + roleName);
        }

        return savedUser;
    }
    
    @Transactional
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        String roleName = user.getRole().getName().toUpperCase();

        switch (roleName) {

            case "ADMIN":
                adminRepository.deleteByUser(user);
                break;

            case "DEALER":
                dealerRepository.deleteByUser(user);
                break;

            case "CUSTOMER":
                customerRepository.deleteByUser(user);
                break;

            default:
                throw new RuntimeException("Unsupported role: " + roleName);
        }

        userRepository.delete(user);
    }


    
    public String sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String otp = String.format("%04d", new Random().nextInt(10000)); // 4 digit OTP

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), "Your OTP Code", "Your OTP is: " + otp);

        return "OTP Sent Successfully!";
    }

    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 🔥 First: Check expiry properly
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired.");
        }

        // 🔥 Second: Check OTP mismatch
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP!");
        }

        // 🔥 OTP successful
        user.setStatus("ACTIVE");
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "OTP Verified Successfully!";
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
