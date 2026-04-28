package com.dairy.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.ApiResponse;
import com.dairy.model.Cart;
import com.dairy.service.CartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cart>>> getCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        return ResponseEntity.ok(ApiResponse.success("Cart items", cartService.getCartItems(userId)));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestBody Map<String, Object> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        try {
            Long productId = Long.valueOf(body.get("productId").toString());
            int quantity = Integer.parseInt(body.get("quantity").toString());
            Cart cart = cartService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Added to cart!", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Cart>> updateCart(@PathVariable Long cartId,
                                                         @RequestBody Map<String, Object> body,
                                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        try {
            int quantity = Integer.parseInt(body.get("quantity").toString());
            Cart cart = cartService.updateCartQuantity(cartId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Cart updated", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<?>> removeFromCart(@PathVariable Long cartId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        try {
            cartService.removeFromCart(cartId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}