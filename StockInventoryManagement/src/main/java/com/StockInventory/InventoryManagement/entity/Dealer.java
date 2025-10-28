package com.StockInventory.InventoryManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dealer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Dealer {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private String address;
    
    @Column(nullable = false)
    private String status = "Active"; // Active / Inactive

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
