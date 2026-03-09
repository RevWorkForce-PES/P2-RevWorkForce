package com.revature.revworkforce.security;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;
import com.revature.revworkforce.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CustomUserDetailsService}.
 *
 * This class verifies the authentication logic used by Spring Security
 * when loading user details from the database.
 *
 * Test scenarios include:
 * <ul>
 *     <li>Successful user authentication</li>
 *     <li>User not found in database</li>
 *     <li>Account locked</li>
 *     <li>Inactive account</li>
 * </ul>
 *
 * The tests follow the AAA (Arrange–Act–Assert) testing pattern.
 *
 * @author RevWorkForce Team
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    /**
     * Mock repository used to simulate database interactions.
     */
    @Mock
    private EmployeeRepository employeeRepository;

    /**
     * Service under test.
     * Mockito will inject mocked dependencies automatically.
     */
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Test employee object used across test cases.
     */
    private Employee testEmployee;

    /**
     * Initialize test data before each test.
     */
    @BeforeEach
    void setUp() {

        Role role = new Role();
        role.setRoleName("ADMIN");

        testEmployee = new Employee();
        testEmployee.setEmployeeId("EMP001");
        testEmployee.setEmail("test@revature.com");
        testEmployee.setPasswordHash("$2a$10$hashedpassword");
        testEmployee.setAccountLocked('N');
        testEmployee.setRoles(Set.of(role));
    }

    /**
     * Test successful loading of user details when employee exists
     * and account is active.
     */
    @Test
    void loadUserByUsername_Success() {

        // Arrange
        when(employeeRepository.findByEmailOrEmployeeId("EMP001", "EMP001"))
                .thenReturn(Optional.of(testEmployee));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("EMP001");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("EMP001");
        assertThat(result.getAuthorities()).isNotEmpty();
        assertThat(result.isAccountNonLocked()).isTrue();
    }

    /**
     * Test scenario where user is not found in the database.
     * Expected result: UsernameNotFoundException.
     */
    @Test
    void loadUserByUsername_UserNotFound() {

        // Arrange
        when(employeeRepository.findByEmailOrEmployeeId("EMP999", "EMP999"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() ->
                customUserDetailsService.loadUserByUsername("EMP999"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    /**
     * Test scenario where the user account is locked.
     * Expected result: UsernameNotFoundException.
     */
    @Test
    void loadUserByUsername_AccountLocked() {

        // Arrange
        testEmployee.setAccountLocked('Y');

        when(employeeRepository.findByEmailOrEmployeeId("EMP001", "EMP001"))
                .thenReturn(Optional.of(testEmployee));

        // Act & Assert
        assertThatThrownBy(() ->
                customUserDetailsService.loadUserByUsername("EMP001"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account is locked");
    }

    /**
     * Test scenario where the user account is inactive.
     * Expected result: UsernameNotFoundException.
     */
    @Test
    void loadUserByUsername_InactiveAccount() {

        // Arrange
        testEmployee.setStatus(null);

        when(employeeRepository.findByEmailOrEmployeeId("EMP001", "EMP001"))
                .thenReturn(Optional.of(testEmployee));

        // Act & Assert
        assertThatThrownBy(() ->
                customUserDetailsService.loadUserByUsername("EMP001"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account is not active");
    }
}