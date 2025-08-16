package com.webdev.project.employee_management_system.controllers;

import com.webdev.project.employee_management_system.dtos.UserDTO;
import com.webdev.project.employee_management_system.entites.User;
import com.webdev.project.employee_management_system.repositories.UserRepository;
import com.webdev.project.employee_management_system.requests.LoginRequest;
import com.webdev.project.employee_management_system.requests.RegisterRequest;
import com.webdev.project.employee_management_system.responses.ErrorResponse;
import com.webdev.project.employee_management_system.responses.LoginSuccessResponse;
import com.webdev.project.employee_management_system.services.CustomUserDetailService;
import com.webdev.project.employee_management_system.services.UserService;
import com.webdev.project.employee_management_system.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailService userDetailsService,
                          UserService userService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ---------------- Login ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Get user entity
            Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("AUTH_001", "Invalid username or password"));
            }

            User user = userOptional.get();

            // Generate JWT with role
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            return ResponseEntity.ok(
                    new LoginSuccessResponse(token, new UserDTO(user))
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("AUTH_001", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("AUTH_002", "Authentication failed: " + e.getMessage()));
        }
    }

    // ---------------- Register ----------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registrationRequest) {
        try {
            // Validate required fields
            if (registrationRequest.getUsername() == null || registrationRequest.getUsername().isBlank()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (registrationRequest.getPassword() == null || registrationRequest.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password is required");
            }

            // Check if username already exists
            if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("REG_002", "Username already exists!"));
            }

            // Create user entity
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); // Fixed here
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setEmail(registrationRequest.getEmail());
            user.setPhone(registrationRequest.getPhone());
            user.setRole(registrationRequest.getRole() != null ? registrationRequest.getRole() : null);

            // Save user
            User createdUser = userService.createUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserDTO(createdUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("REG_003", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("REG_001", "Registration failed: " + e.getMessage()));
        }
    }
}
