package com.revature.revworkforce.security;

import com.revature.revworkforce.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Custom Logout Handler to audit logout events.
 */
@Component
public class AuditLogoutHandler implements LogoutHandler {

    @Autowired
    private AuditService auditService;

    @Override
    public void logout(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            String ipAddress = request.getRemoteAddr();

            // Log the logout action
            auditService.logLogout(username, ipAddress);

            System.out.println("User '" + username + "' logged out successfully.");
        }
    }
}
