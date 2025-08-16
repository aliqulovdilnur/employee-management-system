package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.User;
import com.webdev.project.employee_management_system.enums.Role;
import com.webdev.project.employee_management_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create new user
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update existing user
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user by ID
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Find all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find by role
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

}
