package com.revature.revworkforce.security;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserPrincipal - Represents the authenticated user.
 * 
 * Implements Spring Security's UserDetails interface.
 * Contains employee information and authorities (roles).
 * 
 * @author RevWorkForce Team
 */
public class UserPrincipal implements UserDetails {
    
    private String employeeId;
    private String email;
    private String fullName;
    private String passwordHash;
    private boolean accountLocked;
    private boolean accountActive;
    private Set<GrantedAuthority> authorities;
    
    /**
     * Build UserPrincipal from Employee entity.
     * 
     * @param employee the employee entity
     * @return UserPrincipal object
     */
    public static UserPrincipal build(Employee employee) {
        Set<GrantedAuthority> authorities = employee.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
            .collect(Collectors.toSet());
        
        UserPrincipal principal = new UserPrincipal();
        principal.employeeId = employee.getEmployeeId();
        principal.email = employee.getEmail();
        principal.fullName = employee.getFullName();
        principal.passwordHash = employee.getPasswordHash();
        principal.accountLocked = employee.getAccountLocked() != null && 
                                  employee.getAccountLocked() == 'Y';
        principal.accountActive = employee.getStatus() != null && 
                                  (employee.getStatus().name().equals("ACTIVE") || 
                                   employee.getStatus().name().equals("ON_LEAVE"));
        principal.authorities = authorities;
        
        return principal;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return passwordHash;
    }
    
    @Override
    public String getUsername() {
        return employeeId;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return accountActive;
    }
    
    // Additional getters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    /**
     * Check if user has a specific role.
     * 
     * @param roleName the role name (without ROLE_ prefix)
     * @return true if user has the role
     */
    public boolean hasRole(String roleName) {
        return authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + roleName));
    }
}