package com.StockInventory.InventoryManagement.service;

import com.StockInventory.InventoryManagement.entity.Cart;
import com.StockInventory.InventoryManagement.entity.Product;
import com.StockInventory.InventoryManagement.entity.User;
import com.StockInventory.InventoryManagement.repository.CartRepository;
import com.StockInventory.InventoryManagement.repository.ProductRepository;
import com.StockInventory.InventoryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // ➕ Add product to cart
    public Cart addToCart(String userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    // 👀 View user's cart
    public List<Cart> getUserCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return cartRepository.findByUser(user);
    }

    // ❌ Remove product from cart
    public void removeFromCart(String userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Optional<Cart> cart = cartRepository.findByUserAndProduct(user, product);
        cart.ifPresent(cartRepository::delete);
    }
}
