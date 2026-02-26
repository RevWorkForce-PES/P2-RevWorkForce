package com.revature.revworkforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCrypt Password Encoder Configuration.
 * 
 * BCrypt is a password hashing function designed for secure password storage.
 * It uses a salt to protect against rainbow table attacks and is
 * computationally
 * expensive to slow down brute force attacks.
 * 
 * Key Features:
 * - One-way hashing (cannot be reversed)
 * - Built-in salt (unique for each password)
 * - Configurable work factor (rounds)
 * - Industry standard for password hashing
 * 
 * Configuration:
 * - Strength: 12 rounds
 * - Hash Length: 60 characters
 * - Format: $2a$rounds$salt+hash
 * 
 * Example:
 * Plain text: password123
 * BCrypt hash: $2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW
 * 
 * @author RevWorkForce Team
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class BCryptConfig {
    private static final Logger log = LoggerFactory.getLogger(BCryptConfig.class);

    /**
     * BCrypt strength (number of rounds).
     * 
     * Rounds determine the computational cost:
     * - 10 rounds: ~100ms (minimum recommended)
     * - 12 rounds: ~300ms (good balance)
     * - 14 rounds: ~1.5s (very secure but slower)
     * 
     * Higher rounds = more secure but slower performance.
     * 12 rounds is recommended for most applications.
     */
    private static final int BCRYPT_STRENGTH = 12;

    /**
     * Creates BCrypt password encoder bean.
     * 
     * This encoder is used throughout the application for:
     * - Encoding passwords during user registration
     * - Encoding passwords during password change
     * - Verifying passwords during login
     * 
     * @return PasswordEncoder configured with BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(
                BCRYPT_STRENGTH);

        log.info("BCrypt Password Encoder Initialized with Strength: {} rounds", BCRYPT_STRENGTH);

        // Removed explicit raw password hash logging for security reasons in production

        return encoder;
    }

    /**
     * Get the BCrypt strength value.
     * Can be used by other components if needed.
     * 
     * @return the number of BCrypt rounds
     */
    public static int getBcryptStrength() {
        return BCRYPT_STRENGTH;
    }
}