package com.dairy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dairy.dto.LoginRequest;
import com.dairy.dto.RegisterRequest;
import com.dairy.model.User;
import com.dairy.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number is already registered");
        }

        // NOTE: In production, use BCrypt to hash passwords.
        // For this fresher-level project, plain text is used.
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(User.Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}
