package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.*;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRoleRepositoryTest {

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private EntityManager entityManager;

    private Employee employee;
    private Role role;

    @BeforeEach
    void setUp() {

        // Arrange Department
        Department department = new Department();
        department.setDepartmentName("IT");
        entityManager.persist(department);

        // Arrange Designation
        Designation designation = new Designation();
        designation.setDesignationName("Developer");
        entityManager.persist(designation);

        // Arrange Employee
        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@test.com");
        employee.setPasswordHash("password123");
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setJoiningDate(LocalDate.now());
        employee.setDepartment(department);
        employee.setDesignation(designation);

        entityManager.persist(employee);

        // Arrange Role
        role = new Role();
        role.setRoleName("ADMIN");
        entityManager.persist(role);

        // Arrange EmployeeRole
        EmployeeRole employeeRole = new EmployeeRole(employee.getEmployeeId(), role.getRoleId());
        employeeRole.setAssignedBy("SYSTEM");

        entityManager.persist(employeeRole);
        entityManager.flush();
    }

    @Test
    @DisplayName("Find roles by employeeId")
    void findByEmployeeId_ReturnRoles() {

        // Act
        List<EmployeeRole> roles = employeeRoleRepository.findByEmployeeId("EMP001");

        // Assert
        assertThat(roles).isNotEmpty();
        assertThat(roles.get(0).getEmployeeId()).isEqualTo("EMP001");
    }

    @Test
    @DisplayName("Find employees by roleId")
    void findByRoleId_ReturnEmployees() {

        // Act
        List<EmployeeRole> result = employeeRoleRepository.findByRoleId(role.getRoleId());

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getRoleId()).isEqualTo(role.getRoleId());
    }

    @Test
    @DisplayName("Check if employee has role")
    void existsByEmployeeIdAndRoleId_ReturnTrue() {

        // Act
        boolean exists = employeeRoleRepository
                .existsByEmployeeIdAndRoleId("EMP001", role.getRoleId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Delete role assignment for employee")
    void deleteByEmployeeIdAndRoleId_Success() {

        // Act
        employeeRoleRepository.deleteByEmployeeIdAndRoleId("EMP001", role.getRoleId());

        // Assert
        boolean exists = employeeRoleRepository
                .existsByEmployeeIdAndRoleId("EMP001", role.getRoleId());

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Delete all roles for employee")
    void deleteByEmployeeId_Success() {

        // Act
        employeeRoleRepository.deleteByEmployeeId("EMP001");

        // Assert
        List<EmployeeRole> roles = employeeRoleRepository.findByEmployeeId("EMP001");

        assertThat(roles).isEmpty();
    }

    @Test
    @DisplayName("Find role names by employeeId")
    void findRoleNamesByEmployeeId_ReturnRoleNames() {

        // Act
        List<String> roleNames = employeeRoleRepository
                .findRoleNamesByEmployeeId("EMP001");

        // Assert
        assertThat(roleNames).isNotEmpty();
        assertThat(roleNames.get(0)).isEqualTo("ADMIN");
    }

}