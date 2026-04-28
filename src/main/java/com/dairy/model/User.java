package com.dairy.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum Role { USER, ADMIN }

    // Required by JPA
    public User() {}

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // Getters
    public Long getId()                  { return id; }
    public String getFullName()          { return fullName; }
    public String getEmail()             { return email; }
    public String getPhone()             { return phone; }
    public String getPassword()          { return password; }
    public Role getRole()                { return role; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setFullName(String fullName)          { this.fullName = fullName; }
    public void setEmail(String email)                { this.email = email; }
    public void setPhone(String phone)                { this.phone = phone; }
    public void setPassword(String password)          { this.password = password; }
    public void setRole(Role role)                    { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String fullName, email, phone, password;
        private Role role = Role.USER;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder fullName(String v)        { this.fullName = v; return this; }
        public Builder email(String v)           { this.email = v; return this; }
        public Builder phone(String v)           { this.phone = v; return this; }
        public Builder password(String v)        { this.password = v; return this; }
        public Builder role(Role v)              { this.role = v; return this; }

        public User build() {
            User u = new User();
            u.id       = this.id;
            u.fullName = this.fullName;
            u.email    = this.email;
            u.phone    = this.phone;
            u.password = this.password;
            u.role     = this.role;
            return u;
        }
    }
}