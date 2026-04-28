package com.dairy.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.ApiResponse;
import com.dairy.dto.LoginRequest;
import com.dairy.dto.RegisterRequest;
import com.dairy.model.User;
import com.dairy.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("fullName", user.getFullName());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());
            return ResponseEntity.ok(ApiResponse.success("Registration successful! Please login.", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        try {
            User user = authService.login(request);
            // Store user info in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());

            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("fullName", user.getFullName());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());
            return ResponseEntity.ok(ApiResponse.success("Login successful!", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", userId);
        data.put("role", session.getAttribute("userRole"));
        data.put("name", session.getAttribute("userName"));
        data.put("email", session.getAttribute("userEmail"));
        return ResponseEntity.ok(ApiResponse.success("Authenticated", data));
    }
}
