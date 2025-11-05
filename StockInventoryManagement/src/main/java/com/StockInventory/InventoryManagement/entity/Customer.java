package com.StockInventory.InventoryManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder

public class Customer {

    @Id
    private String customerId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    private String name;
    private String email;
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
}

