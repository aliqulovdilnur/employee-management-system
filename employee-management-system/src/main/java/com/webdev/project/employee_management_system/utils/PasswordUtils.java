package com.webdev.project.employee_management_system.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encrypts (hashes) the password using BCrypt.
     * @param password Raw password
     * @return Hashed password
     */
    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }

    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }


    /**
     * Verifies a raw password against the stored hash.
     * @param rawPassword Password entered by the user
     * @param storedHash Hashed password from the database
     * @return true if passwords match
     */
    public static boolean matches(String rawPassword, String storedHash) {
        return encoder.matches(rawPassword, storedHash);
    }

    /**
     * Checks if password meets HRM security requirements.
     * Requirements:
     *  - At least 8 characters
     *  - At least 1 uppercase letter
     *  - At least 1 lowercase letter
     *  - At least 1 digit
     *  - At least 1 special character
     * @param password Raw password
     * @return true if secure
     */
    public static boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password != null && password.matches(regex);
    }

    /**
     * Provides feedback if password is weak (optional for HRM).
     * @param password Raw password
     * @return Feedback message
     */
    public static String getPasswordStrengthFeedback(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            return "Password must contain at least one special character.";
        }
        return "Strong password.";
    }
}
