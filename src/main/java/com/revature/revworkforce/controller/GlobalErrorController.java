package com.revature.revworkforce.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Global Error Controller
 * Handles all HTTP errors and displays custom error pages
 * 
 * ERROR PAGES LOCATION: templates/frontend/error/
 * 
 * With spring.thymeleaf.prefix=classpath:/templates/frontend/
 * Return "error/404" resolves to: templates/frontend/error/404.html
 * 
 * @author RevWorkForce Team
 */
@Controller
public class GlobalErrorController implements ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorController.class);
    
    /**
     * Main error handler
     * Routes to appropriate error page based on HTTP status code
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        // Get error details
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        // Log the error
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            logger.error("Error {} occurred - Path: {}, Message: {}", 
                        statusCode, path, message);
            
            if (exception != null) {
                logger.error("Exception: ", exception);
            }
            
            // Add details to model
            model.addAttribute("path", path);
            model.addAttribute("message", message);
            
            // Route to appropriate error page
            // Note: "error/404" resolves to "templates/frontend/error/404.html"
            // because spring.thymeleaf.prefix=classpath:/templates/frontend/
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // 404 - Page Not Found
                logger.info("Returning 404 error page");
                return "error/404";
                
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // 403 - Forbidden / Access Denied
                logger.info("Returning 403 error page");
                return "error/403";
                
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // 500 - Internal Server Error
                model.addAttribute("error", exception != null ? exception.getMessage() : message);
                
                // Add stack trace for debugging (only in dev mode)
                if (exception != null && isDevelopmentMode()) {
                    model.addAttribute("trace", getStackTrace(exception));
                }
                
                logger.info("Returning 500 error page");
                return "error/500";
                
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                // 400 - Bad Request
                logger.info("Returning 400 error page");
                return "error/400";
                
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                // 401 - Unauthorized (use same as 403)
                logger.info("Returning 403 error page for 401 Unauthorized");
                return "error/403";
                
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                // 405 - Method Not Allowed (use same as 400)
                logger.info("Returning 400 error page for 405 Method Not Allowed");
                return "error/400";
            }
        }
        
        // Default: 500 error page
        logger.error("Unknown error occurred - Path: {}", path);
        model.addAttribute("path", path);
        model.addAttribute("message", message != null ? message : "An unexpected error occurred");
        
        return "error/500";
    }
    
    /**
     * Check if running in development mode
     */
    private boolean isDevelopmentMode() {
        String env = System.getProperty("spring.profiles.active");
        return env != null && (env.contains("dev") || env.contains("local"));
    }
    
    /**
     * Get stack trace as string
     */
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
        
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
            
            // Limit stack trace length
            if (sb.length() > 2000) {
                sb.append("\t... (truncated)");
                break;
            }
        }
        
        return sb.toString();
    }
}