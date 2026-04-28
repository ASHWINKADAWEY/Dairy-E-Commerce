package com.dairy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.ApiResponse;
import com.dairy.model.User;
import com.dairy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ADMIN: Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
        }
        return ResponseEntity.ok(ApiResponse.success("Users fetched", userService.getAllUsers()));
    }

    // USER or ADMIN: Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
        }
        // User can only see their own profile; admin can see any
        if (!sessionUserId.equals(id) && !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access denied"));
        }
        try {
            return ResponseEntity.ok(ApiResponse.success("User found", userService.getUserById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    // USER: Update own profile | ADMIN: Update any user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id,
                                                         @RequestBody User updatedUser,
                                                         HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Please login first"));
        }
        if (!sessionUserId.equals(id) && !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("You can only edit your own profile"));
        }
        try {
            User user = userService.updateUser(id, updatedUser);
            // Update session name if user updated themselves
            if (sessionUserId.equals(id)) {
                session.setAttribute("userName", user.getFullName());
            }
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully!", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ADMIN: Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Access denied. Admins only."));
        }
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
