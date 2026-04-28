package com.dairy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.model.Cart;
import com.dairy.model.Product;
import com.dairy.model.User;
import com.dairy.repository.CartRepository;
import com.dairy.repository.ProductRepository;
import com.dairy.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Cart> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user);
    }

    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity());
        }

        Optional<Cart> existingItem = cartRepository.findByUserAndProductId(user, productId);
        if (existingItem.isPresent()) {
            Cart cart = existingItem.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        }

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public Cart updateCartQuantity(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (quantity <= 0) {
            cartRepository.delete(cart);
            return null;
        }
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartRepository.deleteByUser(user);
    }
}
