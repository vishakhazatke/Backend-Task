package com.StockInventory.InventoryManagement.controller;

import com.StockInventory.InventoryManagement.entity.Cart;
import com.StockInventory.InventoryManagement.service.CartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartRequest request) {
        Cart cart = cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Cart>> viewCart(@PathVariable String userId) {
        List<Cart> cartList = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody CartRequest request) {
        cartService.removeFromCart(request.getUserId(), request.getProductId());
        return ResponseEntity.ok("Product removed from cart successfully!");
    }

    @Data
    public static class CartRequest {
        private String userId;
        private Long productId;
        private int quantity;
    }
}
