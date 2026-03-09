package com.revature.revworkforce.repository;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.model.Employee;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    private Department department;
    private Designation designation;
    private Employee employee;

    @BeforeEach
    void setUp() {

        // Arrange Department
        department = new Department();
        department.setDepartmentName("IT");
        entityManager.persist(department);

        // Arrange Designation
        designation = new Designation();
        designation.setDesignationName("Developer");
        entityManager.persist(designation);

        // Arrange Employee
        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@test.com");
        employee.setPasswordHash("password123"); // REQUIRED
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setJoiningDate(LocalDate.now());
        employee.setDepartment(department);
        employee.setDesignation(designation);

        entityManager.persist(employee);
        entityManager.flush();
    }

    @Test
    @DisplayName("Save Employee Successfully")
    void saveEmployee_Success() {

        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId("EMP002");
        newEmployee.setFirstName("Alice");
        newEmployee.setLastName("Smith");
        newEmployee.setEmail("alice@test.com");
        newEmployee.setPasswordHash("password123");
        newEmployee.setStatus(EmployeeStatus.ACTIVE);
        newEmployee.setJoiningDate(LocalDate.now());
        newEmployee.setDepartment(department);
        newEmployee.setDesignation(designation);

        Employee savedEmployee = employeeRepository.save(newEmployee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmployeeId()).isEqualTo("EMP002");
    }

    @Test
    void findByEmail_ReturnEmployee() {

        Optional<Employee> result = employeeRepository.findByEmail("john@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void findByEmailOrEmployeeId_ReturnEmployee() {

        Optional<Employee> result =
                employeeRepository.findByEmailOrEmployeeId("john@test.com", "EMP001");

        assertThat(result).isPresent();
    }

    @Test
    void findByStatus_ReturnEmployeeList() {

        List<Employee> employees =
                employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        assertThat(employees).isNotEmpty();
    }

    @Test
    void findByDepartment_ReturnEmployees() {

        List<Employee> employees =
                employeeRepository.findByDepartment(department);

        assertThat(employees).isNotEmpty();
    }

    @Test
    void findByDesignation_ReturnEmployees() {

        List<Employee> employees =
                employeeRepository.findByDesignation(designation);

        assertThat(employees).isNotEmpty();
    }

    @Test
    void searchByKeyword_ReturnMatchingEmployees() {

        List<Employee> employees =
                employeeRepository.searchByKeyword("john");

        assertThat(employees).isNotEmpty();
    }

    @Test
    void existsByEmail_ReturnTrue() {

        boolean exists =
                employeeRepository.existsByEmail("john@test.com");

        assertThat(exists).isTrue();
    }

    @Test
    void countByStatus_ReturnCount() {

        long count =
                employeeRepository.countByStatus(EmployeeStatus.ACTIVE);

        assertThat(count).isGreaterThan(0);
    }

    @Test
    void findByStatusOrderByFirstNameAsc_ReturnEmployees() {

        List<Employee> employees =
                employeeRepository.findByStatusOrderByFirstNameAsc(EmployeeStatus.ACTIVE);

        assertThat(employees).isNotEmpty();
    }

}