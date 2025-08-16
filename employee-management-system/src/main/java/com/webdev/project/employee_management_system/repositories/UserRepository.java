package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.User;
import com.webdev.project.employee_management_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if username already exists
    boolean existsByUsername(String username);

    // Find user by email (optional)
    Optional<User> findByEmail(String email);


    Optional<Object> findByPhone(String phone);

    List<User> findByRole(Role role);
}
