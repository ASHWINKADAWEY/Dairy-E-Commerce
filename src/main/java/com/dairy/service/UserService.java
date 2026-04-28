package com.dairy.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dairy.model.User;
import com.dairy.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        if (updatedUser.getFullName() != null && !updatedUser.getFullName().isBlank()) {
            existing.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getPhone() != null && !updatedUser.getPhone().isBlank()) {
            existing.setPhone(updatedUser.getPhone());
        }
        // Password update allowed if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existing.setPassword(updatedUser.getPassword());
        }
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

