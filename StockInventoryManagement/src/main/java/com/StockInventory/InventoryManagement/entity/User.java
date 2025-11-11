package com.StockInventory.InventoryManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class User {

    @Id
    @Column(length = 50)
    private String id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String mobileNo;
    private String address;

    @Column(nullable = false)
    private String status = "Active";

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_Expiry")
    private LocalDateTime otpExpiry;

    @Column(name = "is_verified")
    private boolean isVerified = false;

}
