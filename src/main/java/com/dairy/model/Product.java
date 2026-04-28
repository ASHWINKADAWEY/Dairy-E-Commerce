package com.dairy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private String category;

    private String imageUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Required by JPA
    public Product() {}

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // Getters
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public BigDecimal getPrice()         { return price; }
    public Integer getQuantity()         { return quantity; }
    public String getCategory()          { return category; }
    public String getImageUrl()          { return imageUrl; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setName(String name)                  { this.name = name; }
    public void setDescription(String description)    { this.description = description; }
    public void setPrice(BigDecimal price)            { this.price = price; }
    public void setQuantity(Integer quantity)         { this.quantity = quantity; }
    public void setCategory(String category)          { this.category = category; }
    public void setImageUrl(String imageUrl)          { this.imageUrl = imageUrl; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name, description, category, imageUrl;
        private BigDecimal price;
        private Integer quantity;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder name(String v)            { this.name = v; return this; }
        public Builder description(String v)     { this.description = v; return this; }
        public Builder price(BigDecimal v)       { this.price = v; return this; }
        public Builder quantity(Integer v)       { this.quantity = v; return this; }
        public Builder category(String v)        { this.category = v; return this; }
        public Builder imageUrl(String v)        { this.imageUrl = v; return this; }

        public Product build() {
            Product p = new Product();
            p.id          = this.id;
            p.name        = this.name;
            p.description = this.description;
            p.price       = this.price;
            p.quantity    = this.quantity;
            p.category    = this.category;
            p.imageUrl    = this.imageUrl;
            return p;
        }
    }
}