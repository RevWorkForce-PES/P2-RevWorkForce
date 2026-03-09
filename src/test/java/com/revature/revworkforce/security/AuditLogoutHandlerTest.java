package com.revature.revworkforce.security;

import com.revature.revworkforce.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;

class AuditLogoutHandlerTest {

    @Mock
    private AuditService auditService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuditLogoutHandler auditLogoutHandler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ===============================
    // Logout Success
    // ===============================

    @Test
    void logout_WithValidAuthentication_ShouldLogAudit() {

        String username = "EMP001";
        String ip = "127.0.0.1";

        when(authentication.getName()).thenReturn(username);
        when(request.getRemoteAddr()).thenReturn(ip);

        auditLogoutHandler.logout(request, response, authentication);

        verify(auditService).logLogout(username, ip);
    }

    // ===============================
    // Null Authentication
    // ===============================

    @Test
    void logout_WithNullAuthentication_ShouldNotCallAudit() {

        auditLogoutHandler.logout(request, response, null);

        verify(auditService, never()).logLogout(anyString(), anyString());
    }

    // ===============================
    // Null Username
    // ===============================

    @Test
    void logout_WithNullUsername_ShouldNotCallAudit() {

        when(authentication.getName()).thenReturn(null);

        auditLogoutHandler.logout(request, response, authentication);

        verify(auditService, never()).logLogout(anyString(), anyString());
    }
}