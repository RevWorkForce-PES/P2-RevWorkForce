package com.revature.revworkforce.config;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom UserDetails implementation that wraps the Employee entity.
 */
public class CustomUserDetails implements UserDetails {

    private final Employee employee;

    public CustomUserDetails(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return employee.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return employee.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // We use email as the username for login
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Not tracking account expiration specifically in Employee
    }

    @Override
    public boolean isAccountNonLocked() {
        // Account is locked if the flag is 'Y'
        if (employee.getAccountLocked() != null && employee.getAccountLocked() == 'Y') {
            return false;
        }
        // Account is temporarily locked if lockedUntil is in the future
        if (employee.getLockedUntil() != null && employee.getLockedUntil().isAfter(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Not tracking credential expiration
    }

    @Override
    public boolean isEnabled() {
        // Employee is enabled only if their status is ACTIVE
        return employee.getStatus() == EmployeeStatus.ACTIVE;
    }

    // Custom getter to access the underlying employee object if needed
    public Employee getEmployee() {
        return employee;
    }
}
