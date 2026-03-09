package com.revature.revworkforce.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomAuthenticationSuccessHandler.
 *
 * Verifies correct dashboard redirection after successful login
 * based on user roles.
 *
 * ADMIN    -> /admin/dashboard
 * MANAGER  -> /manager/dashboard
 * EMPLOYEE -> /employee/dashboard
 * DEFAULT  -> /dashboard
 */
@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

    @InjectMocks
    private CustomAuthenticationSuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    /**
     * Test ADMIN redirect.
     */
    @Test
    void testAdminRedirect() throws Exception {

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        doReturn(authorities).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("adminUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/admin/dashboard");
    }

    /**
     * Test MANAGER redirect.
     */
    @Test
    void testManagerRedirect() throws Exception {

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));

        doReturn(authorities).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("managerUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/manager/dashboard");
    }

    /**
     * Test EMPLOYEE redirect.
     */
    @Test
    void testEmployeeRedirect() throws Exception {

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        doReturn(authorities).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("employeeUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/employee/dashboard");
    }

    /**
     * Test default redirect when role is unknown.
     */
    @Test
    void testDefaultRedirect() throws Exception {

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_UNKNOWN"));

        doReturn(authorities).when(authentication).getAuthorities();
        when(authentication.getName()).thenReturn("unknownUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/dashboard");
    }
}