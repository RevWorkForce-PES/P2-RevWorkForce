package com.revature.revworkforce.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityUtils.
 */
class SecurityUtilsTest {

    /**
     * Clear security context after each test.
     */
    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Test getCurrentUsername when user is authenticated.
     */
    @Test
    void testGetCurrentUsername() {

        User user = new User(
                "E1001",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        String username = SecurityUtils.getCurrentUsername();

        assertEquals("E1001", username);
    }

    /**
     * Test getCurrentUsername when user is not authenticated.
     */
    @Test
    void testGetCurrentUsername_NotAuthenticated() {

        SecurityContextHolder.clearContext();

        String username = SecurityUtils.getCurrentUsername();

        assertNull(username);
    }

    /**
     * Test isAuthenticated method.
     */
    @Test
    void testIsAuthenticated() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtils.isAuthenticated());
    }

    /**
     * Test hasRole when role exists.
     */
    @Test
    void testHasRole_True() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtils.hasRole("ADMIN"));
    }

    /**
     * Test hasRole when role does not exist.
     */
    @Test
    void testHasRole_False() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertFalse(SecurityUtils.hasRole("ADMIN"));
    }

    /**
     * Test hasAnyRole method.
     */
    @Test
    void testHasAnyRole() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean result = SecurityUtils.hasAnyRole("ADMIN", "MANAGER");

        assertTrue(result);
    }

    /**
     * Test getCurrentUserAuthorities.
     */
    @Test
    void testGetCurrentUserAuthorities() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        var authorities = SecurityUtils.getCurrentUserAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }
}