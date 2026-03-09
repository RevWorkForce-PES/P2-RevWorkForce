package com.revature.revworkforce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.revworkforce.dto.ChangePasswordRequest;
import com.revature.revworkforce.exception.AuthenticationException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.impl.AuthServiceImpl;

/**
 * Unit tests for AuthServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmail("emp@test.com");
        employee.setPasswordHash("encodedPassword");
        employee.setFirstLogin('Y');
        employee.setAccountLocked('N');
        employee.setFailedLoginAttempts(0);
    }

    /**
     * Test successful password change.
     */
    @Test
    void testChangePasswordSuccess() {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("NewPassword1");
        request.setConfirmPassword("NewPassword1");

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(passwordEncoder.matches("oldPass", "encodedPassword"))
                .thenReturn(true);

        when(passwordEncoder.encode("NewPassword1"))
                .thenReturn("newEncodedPassword");

        authService.changePassword("EMP001", request);

        verify(employeeRepository).save(employee);
        assertEquals("newEncodedPassword", employee.getPasswordHash());
        assertEquals('N', employee.getFirstLogin());
    }

    /**
     * Test password mismatch.
     */
    @Test
    void testChangePasswordMismatch() {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("Password1");
        request.setConfirmPassword("Password2");

        assertThrows(ValidationException.class,
                () -> authService.changePassword("EMP001", request));
    }

    /**
     * Test incorrect current password.
     */
    @Test
    void testChangePasswordIncorrectCurrentPassword() {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongPass");
        request.setNewPassword("Password1A");
        request.setConfirmPassword("Password1A");

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(AuthenticationException.class,
                () -> authService.changePassword("EMP001", request));
    }

    /**
     * Test successful login reset logic.
     */
    @Test
    void testRecordSuccessfulLogin() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        authService.recordSuccessfulLogin("EMP001");

        verify(employeeRepository).save(employee);

        assertEquals(0, employee.getFailedLoginAttempts());
        assertEquals('N', employee.getAccountLocked());
        assertNull(employee.getLockedUntil());
    }

    /**
     * Test failed login attempt increment.
     */
    @Test
    void testRecordFailedLogin() {

        when(employeeRepository.findByEmailOrEmployeeId("EMP001", "EMP001"))
                .thenReturn(Optional.of(employee));

        authService.recordFailedLogin("EMP001");

        verify(employeeRepository).save(employee);
        assertEquals(1, employee.getFailedLoginAttempts());
    }

    /**
     * Test account lock after max attempts.
     */
    @Test
    void testAccountLockAfterMaxAttempts() {

        employee.setFailedLoginAttempts(4);

        when(employeeRepository.findByEmailOrEmployeeId("EMP001", "EMP001"))
                .thenReturn(Optional.of(employee));

        authService.recordFailedLogin("EMP001");

        assertEquals('Y', employee.getAccountLocked());
        assertNotNull(employee.getLockedUntil());
    }

    /**
     * Test unlocking account when lock expired.
     */
    @Test
    void testUnlockIfExpired() {

        employee.setAccountLocked('Y');
        employee.setLockedUntil(LocalDateTime.now().minusMinutes(5));

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        boolean result = authService.unlockIfExpired("EMP001");

        assertTrue(result);
        assertEquals('N', employee.getAccountLocked());

        verify(employeeRepository).save(employee);
    }

    /**
     * Test isAccountLocked logic.
     */
    @Test
    void testIsAccountLocked() {

        employee.setAccountLocked('Y');
        employee.setLockedUntil(LocalDateTime.now().plusMinutes(10));

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        assertTrue(authService.isAccountLocked("EMP001"));
    }

    /**
     * Test remaining lock time.
     */
    @Test
    void testGetRemainingLockTime() {

        employee.setAccountLocked('Y');
        employee.setLockedUntil(LocalDateTime.now().plusMinutes(10));

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        long remaining = authService.getRemainingLockTime("EMP001");

        assertTrue(remaining > 0);
    }

    /**
     * Test first login detection.
     */
    @Test
    void testIsFirstLogin() {

        employee.setFirstLogin('Y');

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        assertTrue(authService.isFirstLogin("EMP001"));
    }
}