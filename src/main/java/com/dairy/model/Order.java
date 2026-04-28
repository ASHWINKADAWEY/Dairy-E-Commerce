package com.dairy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private String paymentMethod = "COD";

    @Column(nullable = false)
    private String otp;

    private String deliveryAddress;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum OrderStatus { PENDING, CONFIRMED, DELIVERED, CANCELLED }

    // Required by JPA
    public Order() {}

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // Getters
    public Long getId()                    { return id; }
    public User getUser()                  { return user; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public BigDecimal getTotalAmount()     { return totalAmount; }
    public OrderStatus getStatus()         { return status; }
    public String getPaymentMethod()       { return paymentMethod; }
    public String getOtp()                 { return otp; }
    public String getDeliveryAddress()     { return deliveryAddress; }
    public LocalDateTime getCreatedAt()    { return createdAt; }

    // Setters
    public void setId(Long id)                           { this.id = id; }
    public void setUser(User user)                       { this.user = user; }
    public void setOrderItems(List<OrderItem> items)     { this.orderItems = items; }
    public void setTotalAmount(BigDecimal totalAmount)   { this.totalAmount = totalAmount; }
    public void setStatus(OrderStatus status)            { this.status = status; }
    public void setPaymentMethod(String paymentMethod)   { this.paymentMethod = paymentMethod; }
    public void setOtp(String otp)                       { this.otp = otp; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setCreatedAt(LocalDateTime createdAt)    { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private List<OrderItem> orderItems;
        private BigDecimal totalAmount;
        private OrderStatus status = OrderStatus.PENDING;
        private String paymentMethod = "COD";
        private String otp;
        private String deliveryAddress;

        public Builder id(Long id)                        { this.id = id; return this; }
        public Builder user(User v)                       { this.user = v; return this; }
        public Builder orderItems(List<OrderItem> v)      { this.orderItems = v; return this; }
        public Builder totalAmount(BigDecimal v)          { this.totalAmount = v; return this; }
        public Builder status(OrderStatus v)              { this.status = v; return this; }
        public Builder paymentMethod(String v)            { this.paymentMethod = v; return this; }
        public Builder otp(String v)                      { this.otp = v; return this; }
        public Builder deliveryAddress(String v)          { this.deliveryAddress = v; return this; }

        public Order build() {
            Order o = new Order();
            o.id              = this.id;
            o.user            = this.user;
            o.orderItems      = this.orderItems;
            o.totalAmount     = this.totalAmount;
            o.status          = this.status;
            o.paymentMethod   = this.paymentMethod;
            o.otp             = this.otp;
            o.deliveryAddress = this.deliveryAddress;
            return o;
        }
    }
}