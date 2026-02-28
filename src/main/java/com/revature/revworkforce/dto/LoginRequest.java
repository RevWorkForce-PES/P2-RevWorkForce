package com.revature.revworkforce.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for Login Request.
 * 
 * Used to transfer login credentials from client to server.
 * 
 * @author RevWorkForce Team
 */
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    private String username; // Can be email or employee ID
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private Boolean rememberMe;
    
    // Constructors
    public LoginRequest() {
    }
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Boolean getRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}