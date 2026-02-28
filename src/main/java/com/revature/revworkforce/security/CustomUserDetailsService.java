package com.revature.revworkforce.security;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;
import com.revature.revworkforce.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService implementation for Spring Security.
 * 
 * This service loads user details from the database during authentication.
 * It supports login with either email or employee ID.
 * 
 * @author RevWorkForce Team
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * Load user by username (email or employee ID).
     * 
     * This method is called by Spring Security during authentication.
     * 
     * @param username the username (email or employee ID)
     * @return UserDetails object with user information and authorities
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find employee by email or employee ID
        Employee employee = employeeRepository.findByEmailOrEmployeeId(username, username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username
            ));
        
        // Check if account is locked
        if (employee.getAccountLocked() != null && employee.getAccountLocked() == 'Y') {
            throw new UsernameNotFoundException("Account is locked");
        }
        
        // Check if account is active
        if (employee.getStatus() == null || 
            (!employee.getStatus().name().equals("ACTIVE") && 
             !employee.getStatus().name().equals("ON_LEAVE"))) {
            throw new UsernameNotFoundException("Account is not active");
        }
        
        // Get user authorities (roles) with ROLE_ prefix
        Set<GrantedAuthority> authorities = getAuthorities(employee);
        
        // Build UserDetails object
        return User.builder()
            .username(employee.getEmployeeId()) // Use employee ID as username
            .password(employee.getPasswordHash()) // BCrypt hashed password
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(employee.getAccountLocked() != null && employee.getAccountLocked() == 'Y')
            .credentialsExpired(false)
            .disabled(employee.getStatus() == null || 
                     employee.getStatus().name().equals("INACTIVE") ||
                     employee.getStatus().name().equals("TERMINATED"))
            .build();
    }
    
    /**
     * Get user authorities (roles) from employee's roles.
     * 
     * @param employee the employee
     * @return Set of GrantedAuthority
     */
    private Set<GrantedAuthority> getAuthorities(Employee employee) {
        return employee.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
            .collect(Collectors.toSet());
    }
}