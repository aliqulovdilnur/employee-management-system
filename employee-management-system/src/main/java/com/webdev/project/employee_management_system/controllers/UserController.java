package com.webdev.project.employee_management_system.controllers;

import com.webdev.project.employee_management_system.entites.User;
import com.webdev.project.employee_management_system.repositories.UserRepository;
import com.webdev.project.employee_management_system.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Create new user (ADMIN only)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        try {
            if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username already exists"));
            }

            // Encrypt password
            newUser.setPassword(PasswordUtils.encryptPassword(newUser.getPassword()));

            User savedUser = userRepository.save(newUser);
            savedUser.setPassword(null); // Hide password in response

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create user: " + e.getMessage()));
        }
    }

    // Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            users.forEach(user -> user.setPassword(null));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch users: " + e.getMessage()));
        }
    }

    // Get user by id (ADMIN, HR_MANAGER)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        user.setPassword(null);
                        return ResponseEntity.ok(user);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body((User) Map.of("error", "User not found with ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user: " + e.getMessage()));
        }
    }

    // Update user (ADMIN only)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setFirstName(updatedUser.getFirstName());
                        existingUser.setLastName(updatedUser.getLastName());
                        existingUser.setEmail(updatedUser.getEmail());
                        existingUser.setPhone(updatedUser.getPhone());
                        existingUser.setRole(updatedUser.getRole());

                        // Update password only if provided
                        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                            existingUser.setPassword(PasswordUtils.encryptPassword(updatedUser.getPassword()));
                        }

                        userRepository.save(existingUser);
                        existingUser.setPassword(null);
                        return ResponseEntity.ok(existingUser);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body((User) Map.of("error", "User not found with ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user: " + e.getMessage()));
        }
    }

    // Delete user (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found with ID: " + id));
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }
}
