package com.revature.revworkforce.service;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setStatus(EmployeeStatus.ACTIVE);
    }

    // -----------------------------
    // resetPassword
    // -----------------------------
    @Test
    void resetPassword_ShouldUpdatePassword() {
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(passwordEncoder.encode("newpassword"))
                .thenReturn("encodedPassword");

        adminService.resetPassword("EMP001", "newpassword");

        verify(employeeRepository).save(employee);
        assertEquals('Y', employee.getFirstLogin());
        assertNotNull(employee.getUpdatedAt());
        assertEquals("encodedPassword", employee.getPasswordHash());
    }

    @Test
    void resetPassword_ShortPassword_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> adminService.resetPassword("EMP001", "short"));
    }

    @Test
    void resetPassword_NullPassword_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> adminService.resetPassword("EMP001", null));
    }

    @Test
    void resetPassword_EmployeeNotFound_ThrowsException() {
        when(employeeRepository.findById("EMP002")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.resetPassword("EMP002", "validpassword"));
    }

    // -----------------------------
    // unlockAccount
    // -----------------------------
    @Test
    void unlockAccount_ShouldUnlockUser() {
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        adminService.unlockAccount("EMP001");

        verify(employeeRepository).save(employee);
        assertEquals('N', employee.getAccountLocked());
        assertEquals(0, employee.getFailedLoginAttempts());
        assertNull(employee.getLockedUntil());
        assertNotNull(employee.getUpdatedAt());
    }

    @Test
    void unlockAccount_EmployeeNotFound_ThrowsException() {
        when(employeeRepository.findById("EMP002")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.unlockAccount("EMP002"));
    }

    // -----------------------------
    // lockAccount
    // -----------------------------
    @Test
    void lockAccount_ShouldLockUser() {
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        adminService.lockAccount("EMP001");

        verify(employeeRepository).save(employee);
        assertEquals('Y', employee.getAccountLocked());
        assertNotNull(employee.getLockedUntil());
        assertNotNull(employee.getUpdatedAt());
    }

    @Test
    void lockAccount_EmployeeNotFound_ThrowsException() {
        when(employeeRepository.findById("EMP002")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.lockAccount("EMP002"));
    }

    // -----------------------------
    // getSystemStatistics
    // -----------------------------
    @Test
    void getSystemStatistics_ShouldReturnStats() {
        when(employeeRepository.count()).thenReturn(10L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(7L);
        when(employeeRepository.countByStatus(EmployeeStatus.INACTIVE)).thenReturn(3L);
        when(roleRepository.count()).thenReturn(5L);
        when(employeeRepository.findByAccountLocked('Y')).thenReturn(List.of(employee));

        Map<String, Object> stats = adminService.getSystemStatistics();

        assertEquals(10L, stats.get("totalEmployees"));
        assertEquals(7L, stats.get("activeEmployees"));
        assertEquals(3L, stats.get("inactiveEmployees"));
        assertEquals(5L, stats.get("totalRoles"));
        assertEquals(1, stats.get("lockedAccounts"));
    }

    // -----------------------------
    // Queries
    // -----------------------------
    @Test
    void getLockedAccounts_ReturnsList() {
        when(employeeRepository.findByAccountLocked('Y')).thenReturn(List.of(employee));
        List<Employee> result = adminService.getLockedAccounts();
        assertEquals(1, result.size());
    }

    @Test
    void getFirstLoginEmployees_ReturnsList() {
        when(employeeRepository.findByFirstLogin('Y')).thenReturn(List.of(employee));
        List<Employee> result = adminService.getFirstLoginEmployees();
        assertEquals(1, result.size());
    }

    // -----------------------------
    // Bulk Operations
    // -----------------------------
    @Test
    void bulkActivateEmployees_ValidAndInvalidIds() {
        Employee e2 = new Employee();
        e2.setEmployeeId("EMP002");

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(employeeRepository.findById("EMP002")).thenReturn(Optional.of(e2));
        when(employeeRepository.findById("EMP003")).thenReturn(Optional.empty()); // Does not exist

        int count = adminService.bulkActivateEmployees(List.of("EMP001", "EMP002", "EMP003"));

        assertEquals(2, count);
        assertEquals(EmployeeStatus.ACTIVE, employee.getStatus());
        assertEquals(EmployeeStatus.ACTIVE, e2.getStatus());
        verify(employeeRepository, times(2)).save(any(Employee.class));
    }

    @Test
    void bulkDeactivateEmployees_ValidAndInvalidIds() {
        Employee e2 = new Employee();
        e2.setEmployeeId("EMP002");

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(employeeRepository.findById("EMP002")).thenReturn(Optional.of(e2));
        when(employeeRepository.findById("EMP003")).thenReturn(Optional.empty()); // Does not exist

        int count = adminService.bulkDeactivateEmployees(List.of("EMP001", "EMP002", "EMP003"));

        assertEquals(2, count);
        assertEquals(EmployeeStatus.INACTIVE, employee.getStatus());
        assertEquals(EmployeeStatus.INACTIVE, e2.getStatus());
        verify(employeeRepository, times(2)).save(any(Employee.class));
    }

    // -----------------------------
    // getDatabaseHealth
    // -----------------------------
    @Test
    void getDatabaseHealth_ShouldReturnUP() {
        when(employeeRepository.count()).thenReturn(10L);

        Map<String, Object> health = adminService.getDatabaseHealth();

        assertEquals("UP", health.get("status"));
        assertEquals(10L, health.get("employeeCount"));
        assertNotNull(health.get("timestamp"));
    }

    @Test
    void getDatabaseHealth_ExceptionThrown_ShouldReturnDOWN() {
        when(employeeRepository.count()).thenThrow(new RuntimeException("DB Connection Failed"));

        Map<String, Object> health = adminService.getDatabaseHealth();

        assertEquals("DOWN", health.get("status"));
        assertEquals("DB Connection Failed", health.get("error"));
        assertNotNull(health.get("timestamp"));
    }
}