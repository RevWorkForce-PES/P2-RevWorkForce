package com.revature.revworkforce.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        LoginRequest login = new LoginRequest();

        login.setUsername("john.doe");
        login.setPassword("secret123");
        login.setRememberMe(true);

        assertEquals("john.doe", login.getUsername());
        assertEquals("secret123", login.getPassword());
        assertTrue(login.getRememberMe());
    }

    @Test
    void testAllArgsConstructor() {
        LoginRequest login = new LoginRequest("jane.doe", "pass456");

        assertEquals("jane.doe", login.getUsername());
        assertEquals("pass456", login.getPassword());
        assertNull(login.getRememberMe()); // not set in constructor
    }

    @Test
    void testToStringExcludesPassword() {
        LoginRequest login = new LoginRequest();
        login.setUsername("user123");
        login.setPassword("mypassword");
        login.setRememberMe(false);

        String str = login.toString();
        assertTrue(str.contains("user123"));
        assertTrue(str.contains("false"));
        assertFalse(str.contains("mypassword")); // password should not appear
    }
}