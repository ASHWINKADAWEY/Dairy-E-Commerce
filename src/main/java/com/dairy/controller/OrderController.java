package com.dairy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.ApiResponse;
import com.dairy.dto.OrderRequest;
import com.dairy.model.Order;
import com.dairy.service.OrderService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // USER: Place order from cart
    @PostMapping("/place")
    public ResponseEntity<ApiResponse<Map<String, Object>>> placeOrder(
            @Valid @RequestBody OrderRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        try {
            Order order = orderService.placeOrder(userId, request.getDeliveryAddress());
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", order.getId());
            data.put("otp", order.getOtp());
            data.put("totalAmount", order.getTotalAmount());
            data.put("status", order.getStatus());
            data.put("paymentMethod", order.getPaymentMethod());
            data.put("deliveryAddress", order.getDeliveryAddress());
            return ResponseEntity.ok(ApiResponse.success("Order placed successfully! Save your OTP.", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // USER: View own orders
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        return ResponseEntity.ok(ApiResponse.success("Your orders", orderService.getUserOrders(userId)));
    }

    // ADMIN: View all orders
    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
        }
        return ResponseEntity.ok(ApiResponse.success("All orders", orderService.getAllOrders()));
    }

    // ADMIN: Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(@PathVariable Long id,
                                                            @RequestBody Map<String, String> body,
                                                            HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
        }
        try {
            Order order = orderService.updateOrderStatus(id, body.get("status"));
            return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Get single order by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
			return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
		}
        try {
            return ResponseEntity.ok(ApiResponse.success("Order found", orderService.getOrderById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}