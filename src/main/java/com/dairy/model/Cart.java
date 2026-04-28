package com.dairy.model;

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
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Required by JPA
    public Cart() {}

    // Getters
    public Long getId()          { return id; }
    public User getUser()        { return user; }
    public Product getProduct()  { return product; }
    public Integer getQuantity() { return quantity; }

    // Setters
    public void setId(Long id)               { this.id = id; }
    public void setUser(User user)           { this.user = user; }
    public void setProduct(Product product)  { this.product = product; }
    public void setQuantity(Integer quantity){ this.quantity = quantity; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private Product product;
        private Integer quantity;

        public Builder id(Long id)           { this.id = id; return this; }
        public Builder user(User v)          { this.user = v; return this; }
        public Builder product(Product v)    { this.product = v; return this; }
        public Builder quantity(Integer v)   { this.quantity = v; return this; }

        public Cart build() {
            Cart c = new Cart();
            c.id       = this.id;
            c.user     = this.user;
            c.product  = this.product;
            c.quantity = this.quantity;
            return c;
        }
    }
}