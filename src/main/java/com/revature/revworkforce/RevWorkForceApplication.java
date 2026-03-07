package com.revature.revworkforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main Spring Boot Application class for RevWorkForce HRM System.
 * 
 * <p>
 * This class serves as the entry point for the Spring Boot application.
 * It implements(lke from profile {@link CommandLineRunner} to print a dynamic startup banner
 * that shows the actual port the application is running on.
 * </p>
 * 
 * <p>
 * @SpringBootApplication enables:
 * <ul>
 *   <li>@Configuration: Tags the class as a source of bean definitions</li>
 *   <li>@EnableAutoConfiguration: Automatically configures Spring application based on classpath</li>
 *   <li>@ComponentScan: Scans for other components, configurations, and services</li>
 * </ul>
 * </p>
 * 
 * @author RevWorkForce Team
 * @version 1.3.0
 * @since 2026
 */

@EnableMethodSecurity

@EnableScheduling
@SpringBootApplication
public class RevWorkForceApplication implements CommandLineRunner {

    /**
     * Spring Environment to access runtime properties.
     * Used to get the actual port the application is running on.
     */
    @Autowired
    private Environment env;

    /**
     * Main method to start the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RevWorkForceApplication.class, args);
    }

    /**
     * Prints a startup banner with application information, including the
     * dynamically assigned server port, after the application has started.
     * 
     * @param args command line arguments passed by Spring Boot
     */
    @Override
    public void run(String... args) {
        String port = env.getProperty("local.server.port"); // runtime port

        System.out.println("\n" +
            "========================================\n" +
            "  RevWorkForce Application Started!     \n" +
            "========================================\n" +
            "  Application: RevWorkForce HRM        \n" +
            "  Port: " + port + "                      \n" +
            "  URL: http://localhost:" + port + "     \n" +
            "  Database: Oracle 19c                 \n" +
            "========================================\n"
        );
    }
}