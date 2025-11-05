package com.StockInventory.InventoryManagement.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private String address;
    private String roleName;
    private String shopName;
    private String gstNumber;
}
