package com.dairy.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Required by JPA
    public OrderItem() {}

    // Getters
    public Long getId()           { return id; }
    public Order getOrder()       { return order; }
    public Product getProduct()   { return product; }
    public Integer getQuantity()  { return quantity; }
    public BigDecimal getPrice()  { return price; }

    // Setters
    public void setId(Long id)               { this.id = id; }
    public void setOrder(Order order)        { this.order = order; }
    public void setProduct(Product product)  { this.product = product; }
    public void setQuantity(Integer quantity){ this.quantity = quantity; }
    public void setPrice(BigDecimal price)   { this.price = price; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Order order;
        private Product product;
        private Integer quantity;
        private BigDecimal price;

        public Builder id(Long id)             { this.id = id; return this; }
        public Builder order(Order v)          { this.order = v; return this; }
        public Builder product(Product v)      { this.product = v; return this; }
        public Builder quantity(Integer v)     { this.quantity = v; return this; }
        public Builder price(BigDecimal v)     { this.price = v; return this; }

        public OrderItem build() {
            OrderItem i = new OrderItem();
            i.id       = this.id;
            i.order    = this.order;
            i.product  = this.product;
            i.quantity = this.quantity;
            i.price    = this.price;
            return i;
        }
    }
}