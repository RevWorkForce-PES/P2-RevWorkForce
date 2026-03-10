package com.revature.revworkforce.controller;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.AuthService;
import com.revature.revworkforce.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

/**
 * Unit tests for AuthController.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AuthController authController;

    private Employee employee;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmail("test@test.com");
        employee.setFirstName("Test");
        employee.setLastName("User");
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("pages/auth/splash"));
    }

    /**
     * Test logout redirection.
     */
    @Test
    void testLogout() throws Exception {

        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }

    /**
     * Test change password page.
     */
    @Test
    void testChangePasswordPage() throws Exception {

        mockMvc.perform(get("/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/reset-password"));
    }

    /**
     * Test forgot password page.
     */
    @Test
    void testForgotPasswordPage() throws Exception {

        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/forgot-password"));
    }

    /**
     * Test verify email success.
     */
    @Test
    void testVerifyEmailSuccess() throws Exception {

        employee.setSecurityQuestion1("Pet name?");
        employee.setSecurityQuestion2("Birth city?");

        when(employeeRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(post("/forgot-password/verify-email")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/security-questions"));
    }

    /**
     * Test verify email not found.
     */
    @Test
    void testVerifyEmailNotFound() throws Exception {

        when(employeeRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/forgot-password/verify-email")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/forgot-password"));
    }

    /**
     * Test password reset success.
     */
    @Test
    void testResetPasswordSuccess() throws Exception {

        when(employeeRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(employee));

        when(passwordEncoder.encode("NewPassword1"))
                .thenReturn("encodedPassword");

        mockMvc.perform(post("/forgot-password/reset")
                .param("email", "test@test.com")
                .param("newPassword", "NewPassword1")
                .param("confirmPassword", "NewPassword1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(employeeRepository).save(employee);
        verify(auditService).logAction(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }

    /**
     * Test reset password mismatch.
     */
    @Test
    void testResetPasswordMismatch() throws Exception {

        mockMvc.perform(post("/forgot-password/reset")
                .param("email", "test@test.com")
                .param("newPassword", "Password1")
                .param("confirmPassword", "Password2"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/auth/reset-password"));
    }

    /**
     * Test 404 error page.
     */
    @Test
    void test404Page() throws Exception {

        mockMvc.perform(get("/error/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }

    /**
     * Test 500 error page.
     */
    @Test
    void test500Page() throws Exception {

        mockMvc.perform(get("/error/500"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"));
    }
}