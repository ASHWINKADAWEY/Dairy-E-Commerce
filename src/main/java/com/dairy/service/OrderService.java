package com.dairy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.model.Cart;
import com.dairy.model.Order;
import com.dairy.model.OrderItem;
import com.dairy.model.Product;
import com.dairy.model.User;
import com.dairy.repository.CartRepository;
import com.dairy.repository.OrderItemRepository;
import com.dairy.repository.OrderRepository;
import com.dairy.repository.ProductRepository;
import com.dairy.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long userId, String deliveryAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Your cart is empty. Add items before placing an order.");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Calculate total amount
        BigDecimal total = BigDecimal.ZERO;
        for (Cart item : cartItems) {
            total = total.add(item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Save order first
        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .status(Order.OrderStatus.PENDING)
                .paymentMethod("COD")
                .otp(otp)
                .deliveryAddress(deliveryAddress)
                .build();
        order = orderRepository.save(order);

        // Create order items and reduce product stock
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();
            orderItems.add(orderItemRepository.save(orderItem));
        }
        order.setOrderItems(orderItems);

        // Clear cart after order
        cartRepository.deleteByUser(user);

        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }
}
