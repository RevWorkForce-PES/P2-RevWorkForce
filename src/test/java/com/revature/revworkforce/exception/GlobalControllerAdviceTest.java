package com.revature.revworkforce.exception;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class GlobalControllerAdviceTest {

    @InjectMocks
    private GlobalControllerAdvice controllerAdvice;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUserAttributes_AuthenticatedEmployee_AddsAttributes() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");

        Authentication auth = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")))
                .when(auth).getAuthorities();

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::isAuthenticated).thenReturn(true);
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            security.when(SecurityUtils::getCurrentAuthentication).thenReturn(auth);

            when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

            controllerAdvice.addUserAttributes(model);

            verify(model).addAttribute("fullName", "John Doe");
            verify(model).addAttribute("userRole", "Employee");
        }
    }

    @Test
    void addUserAttributes_AuthenticatedAdmin_AddsAdminFallback() {
        Authentication auth = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(auth).getAuthorities();

        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::isAuthenticated).thenReturn(true);
            security.when(SecurityUtils::getCurrentUsername).thenReturn("ADMIN");
            security.when(SecurityUtils::getCurrentAuthentication).thenReturn(auth);

            when(employeeRepository.findById("ADMIN")).thenReturn(Optional.empty());

            controllerAdvice.addUserAttributes(model);

            verify(model).addAttribute("fullName", "System Administrator");
            verify(model).addAttribute("userRole", "Admin");
        }
    }

    @Test
    void addUserAttributes_NotAuthenticated_DoesNothing() {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::isAuthenticated).thenReturn(false);

            controllerAdvice.addUserAttributes(model);

            verifyNoInteractions(model, employeeRepository);
        }
    }
}
