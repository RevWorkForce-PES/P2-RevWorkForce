package com.revature.revworkforce.service;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.ResourceNotFoundException;
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
    }

}