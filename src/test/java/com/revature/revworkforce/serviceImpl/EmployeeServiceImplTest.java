package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Role;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.service.impl.EmployeeServiceImpl;
import com.revature.revworkforce.service.AuditService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DesignationRepository designationRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {

        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId("EMP001");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");
        employeeDTO.setDateOfBirth(LocalDate.of(1995, 1, 1));

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmail("john@example.com");
        employee.setStatus(EmployeeStatus.ACTIVE);
    }

    /**
     * Test duplicate email validation.
     */
    @Test
    void createEmployee_DuplicateEmail_ShouldThrowException() {

        // Arrange
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> employeeService.createEmployee(employeeDTO))
                .isInstanceOf(DuplicateResourceException.class);
    }

    /**
     * Test get employee by ID success.
     */
    @Test
    void getEmployeeById_Success() {

        // Arrange
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById("EMP001");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo("EMP001");
    }

    /**
     * Test get employee by ID not found.
     */
    @Test
    void getEmployeeById_NotFound_ShouldThrowException() {

        // Arrange
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> employeeService.getEmployeeById("EMP001"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    /**
     * Test deactivate employee.
     */
    @Test
    void deactivateEmployee_Success() {

        // Arrange
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        // Act
        employeeService.deactivateEmployee("EMP001");

        // Assert
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.INACTIVE);
        verify(employeeRepository).save(employee);
    }

    /**
     * Test delete employee (soft delete).
     */
    @Test
    void deleteEmployee_Success() {

        // Arrange
        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        // Act
        employeeService.deleteEmployee("EMP001");

        // Assert
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.TERMINATED);
        verify(employeeRepository).save(employee);
    }

    /**
     * Test generate employee ID.
     */
    @Test
    void generateEmployeeId_ShouldGenerateNextId() {

        // Arrange
        Employee e1 = new Employee();
        e1.setEmployeeId("EMP001");

        Employee e2 = new Employee();
        e2.setEmployeeId("EMP002");

        when(employeeRepository.findAll()).thenReturn(List.of(e1, e2));

        // Act
        String id = employeeService.generateEmployeeId("EMP");

        // Assert
        assertThat(id).isEqualTo("EMP003");
    }

    /**
     * Test get all employees.
     */
    @Test
    void getAllEmployees_ReturnsList() {

        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

}