package com.revature.revworkforce.config;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import com.revature.revworkforce.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RoleRestorerConfig
 * 
 * A simple startup script that checks for any employees whose roles were
 * accidentally
 * wiped due to a previous bug, and restores the base EMPLOYEE role.
 */
@Configuration
public class RoleRestorerConfig {

    @Bean
    public CommandLineRunner roleRestorer(EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            EmployeeService employeeService) {
        return args -> {
            System.out.println("Running Role Restorer to fix any users with missing roles...");
            List<Employee> allEmployees = employeeRepository.findAll();
            Role defaultRole = roleRepository.findByRoleName("EMPLOYEE").orElse(null);

            if (defaultRole != null) {
                int restoredCount = 0;
                for (Employee emp : allEmployees) {
                    if (emp.getRoles() == null || emp.getRoles().isEmpty()) {
                        employeeService.assignRole(emp.getEmployeeId(), defaultRole.getRoleId());
                        System.out.println("Restored EMPLOYEE role for user: " + emp.getEmployeeId() + " ("
                                + emp.getFullName() + ")");
                        restoredCount++;
                    }
                }
                if (restoredCount > 0) {
                    System.out.println("Successfully restored roles for " + restoredCount + " users.");
                } else {
                    System.out.println("All users have roles. No restoration needed.");
                }
            } else {
                System.out.println("Default EMPLOYEE role not found in database.");
            }
        };
    }
}
