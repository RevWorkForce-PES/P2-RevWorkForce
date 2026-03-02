package com.revature.revworkforce.service;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for complete admin operations and system monitoring logic.
 */
@Service
@Transactional
public class AdminService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String employeeId, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        employee.setPasswordHash(passwordEncoder.encode(newPassword));
        employee.setFirstLogin('Y');
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }

    public void unlockAccount(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        employee.setAccountLocked('N');
        employee.setFailedLoginAttempts(0);
        employee.setLockedUntil(null);
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }

    public void lockAccount(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));

        employee.setAccountLocked('Y');
        employee.setLockedUntil(LocalDateTime.now().plusHours(24)); // Lock for 24 hours as an example
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }

    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long inactiveEmployees = employeeRepository.countByStatus(EmployeeStatus.INACTIVE);
        List<Employee> lockedAccounts = getLockedAccounts();
        long totalRoles = roleRepository.count();

        stats.put("totalEmployees", totalEmployees);
        stats.put("activeEmployees", activeEmployees);
        stats.put("inactiveEmployees", inactiveEmployees);
        stats.put("lockedAccounts", lockedAccounts.size());
        stats.put("totalRoles", totalRoles);

        return stats;
    }

    public List<Employee> getLockedAccounts() {
        return employeeRepository.findByAccountLocked('Y');
    }

    public List<Employee> getFirstLoginEmployees() {
        return employeeRepository.findByFirstLogin('Y');
    }

    public int bulkActivateEmployees(List<String> employeeIds) {
        int count = 0;
        for (String id : employeeIds) {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee != null) {
                employee.setStatus(EmployeeStatus.ACTIVE);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
                count++;
            }
        }
        return count;
    }

    public int bulkDeactivateEmployees(List<String> employeeIds) {
        int count = 0;
        for (String id : employeeIds) {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee != null) {
                employee.setStatus(EmployeeStatus.INACTIVE);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
                count++;
            }
        }
        return count;
    }

    public Map<String, Object> getDatabaseHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("timestamp", LocalDateTime.now());
        try {
            long employeeCount = employeeRepository.count();
            health.put("status", "UP");
            health.put("employeeCount", employeeCount);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        return health;
    }
}
