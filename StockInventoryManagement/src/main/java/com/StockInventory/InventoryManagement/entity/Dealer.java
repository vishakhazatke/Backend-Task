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
	    private String dealerId;

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
	    
	    private String shopName;
	    private String gstNumber;

	    @ManyToOne
	    @JoinColumn(name = "role_id")
	    private Role role;
}
