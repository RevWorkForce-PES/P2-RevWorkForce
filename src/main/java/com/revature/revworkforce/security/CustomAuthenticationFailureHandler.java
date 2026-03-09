package com.revature.revworkforce.security;

import com.revature.revworkforce.service.AuthService;
import com.revature.revworkforce.service.AuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Authentication Failure Handler.
 * Logs failed login attempts to the audit log and tracks for account locking.
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuditService auditService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String ipAddress = request.getRemoteAddr();

        if (username != null && !username.isEmpty()) {
            // Record failed attempt for locking logic
            authService.recordFailedLogin(username);

            // Audit the failure
            auditService.createAuditLog(
                    username,
                    "FAILED_LOGIN",
                    "SESSIONS",
                    null,
                    null,
                    "Failed login attempt: " + exception.getMessage(),
                    ipAddress,
                    request.getHeader("User-Agent"));
        }

        response.sendRedirect("/login?error=true");
    }
}
