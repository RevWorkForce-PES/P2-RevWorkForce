package com.revature.revworkforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the RevWorkForce HRM Application.
 *
 * This class bootstraps the entire Spring Boot application,
 * including:
 * - Auto-configuration
 * - Component scanning
 * - Entity detection
 * - Repository initialization
 * - Embedded Tomcat server startup
 *
 * NOTE:
 * Ensure all model (entity) classes and repository interfaces
 * are created before running the application.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class RevWorkForceApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(RevWorkForceApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(RevWorkForceApplication.class, args);

        logger.info("===========================================");
        logger.info("   RevWorkForce Application Started       ");
        logger.info("   Running on: http://localhost:8081      ");
        logger.info("===========================================");
    }
}