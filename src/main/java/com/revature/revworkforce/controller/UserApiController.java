package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API Controller for User-related operations.
 * Provides user information for frontend components.
 * 
 * @author RevWorkForce Team
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Get basic info for the currently authenticated user.
     * Used by the sidebar component to populate user name and role.
     * 
     * @return Map containing fullName and userRole
     */
    @GetMapping("/info")
    public Map<String, String> getUserInfo() {
        Map<String, String> userInfo = new HashMap<>();

        if (SecurityUtils.isAuthenticated()) {
            String employeeId = SecurityUtils.getCurrentUsername();

            // Get Full Name
            employeeRepository.findById(employeeId).ifPresentOrElse(
                    emp -> userInfo.put("fullName", emp.getFullName()),
                    () -> userInfo.put("fullName", "System Administrator"));

            // Get Primary Role
            Authentication auth = SecurityUtils.getCurrentAuthentication();
            if (auth != null) {
                String primaryRole = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(r -> r.replace("ROLE_", ""))
                        .sorted((r1, r2) -> {
                            int p1 = r1.equals("ADMIN") ? 0 : (r1.equals("MANAGER") ? 1 : 2);
                            int p2 = r2.equals("ADMIN") ? 0 : (r2.equals("MANAGER") ? 1 : 2);
                            return Integer.compare(p1, p2);
                        })
                        .findFirst()
                        .orElse("User");

                String formattedRole = primaryRole.substring(0, 1).toUpperCase() +
                        primaryRole.substring(1).toLowerCase();
                userInfo.put("role", formattedRole);
            }
        } else {
            userInfo.put("fullName", "Guest");
            userInfo.put("role", "None");
        }

        return userInfo;
    }
}
