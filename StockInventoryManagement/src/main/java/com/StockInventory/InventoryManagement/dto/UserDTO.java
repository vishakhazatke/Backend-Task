package com.StockInventory.InventoryManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String mobileNo;
    private String address;
    private String roleName;
}
