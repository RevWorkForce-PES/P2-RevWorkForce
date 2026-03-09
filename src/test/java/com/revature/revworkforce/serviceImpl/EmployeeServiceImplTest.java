package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Designation;
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
     * Test create employee success.
     */
    @Test
    void createEmployee_Success() {
        // Arrange
        employeeDTO.setRoleIds(null); // Force default role
        Role defaultRole = new Role();
        defaultRole.setRoleId(1L);
        defaultRole.setRoleName("EMPLOYEE");

        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsById(anyString())).thenReturn(false);
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(roleRepository.findByRoleName("EMPLOYEE")).thenReturn(Optional.of(defaultRole));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(defaultRole));
        when(employeeRoleRepository.existsByEmployeeIdAndRoleId(anyString(), anyLong())).thenReturn(false);

        // Act
        Employee result = employeeService.createEmployee(employeeDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(employeeRepository).save(any(Employee.class));
        verify(auditService, times(2)).createAuditLog(anyString(), anyString(), anyString(), anyString(), any(),
                anyString(),
                any(), any());
    }

    /**
     * Test create employee with invalid age.
     */
    @Test
    void createEmployee_InvalidAge_ShouldThrowException() {
        // Arrange
        employeeDTO.setDateOfBirth(LocalDate.now().minusYears(17)); // Under 18

        // Act + Assert
        assertThatThrownBy(() -> employeeService.createEmployee(employeeDTO))
                .isInstanceOf(com.revature.revworkforce.exception.ValidationException.class);
    }

    /**
     * Test update employee success.
     */
    @Test
    void updateEmployee_Success() {
        // Arrange
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeDTO.setFirstName("Updated");

        // Act
        Employee result = employeeService.updateEmployee("EMP001", employeeDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Updated");
        verify(employeeRepository).save(employee);
    }

    /**
     * Test search employees by keyword.
     */
    @Test
    void searchEmployees_WithKeyword() {
        // Arrange
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setKeyword("John");
        when(employeeRepository.searchByKeyword("John")).thenReturn(List.of(employee));

        // Act
        List<Employee> results = employeeService.searchEmployees(criteria);

        // Assert
        assertThat(results).hasSize(1);
        verify(employeeRepository).searchByKeyword("John");
    }

    /**
     * Test search employees with filters.
     */
    @Test
    void searchEmployees_WithFilters() {
        // Arrange
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setDepartmentId(1L);
        criteria.setStatus(EmployeeStatus.ACTIVE);

        Department dept = new Department();
        dept.setDepartmentId(1L);
        employee.setDepartment(dept);

        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        // Act
        List<Employee> results = employeeService.searchEmployees(criteria);

        // Assert
        assertThat(results).hasSize(1);
    }

    /**
     * Test reactivate employee.
     */
    @Test
    void reactivateEmployee_Success() {
        // Arrange
        employee.setStatus(EmployeeStatus.INACTIVE);
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

        // Act
        employeeService.reactivateEmployee("EMP001");

        // Assert
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.ACTIVE);
        verify(employeeRepository).save(employee);
    }

    /**
     * Test assign role.
     */
    @Test
    void assignRole_Success() {
        // Arrange
        Role role = new Role();
        role.setRoleId(2L);
        role.setRoleName("MANAGER");

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(employeeRoleRepository.existsByEmployeeIdAndRoleId("EMP001", 2L)).thenReturn(false);

        // Act
        employeeService.assignRole("EMP001", 2L);

        // Assert
        verify(employeeRoleRepository).save(any());
    }

    /**
     * Test remove role.
     */
    @Test
    void removeRole_Success() {
        // Act
        employeeService.removeRole("EMP001", 2L);

        // Assert
        verify(employeeRoleRepository).deleteByEmployeeIdAndRoleId("EMP001", 2L);
    }

    /**
     * Test get employee roles.
     */
    @Test
    void getEmployeeRoles_Success() {
        // Arrange
        Role role = new Role();
        role.setRoleName("EMPLOYEE");
        employee.setRoles(Set.of(role));
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

        // Act
        Set<Role> roles = employeeService.getEmployeeRoles("EMP001");

        // Assert
        assertThat(roles).hasSize(1);
        assertThat(roles.iterator().next().getRoleName()).isEqualTo("EMPLOYEE");
    }

    /**
     * Test convertToDTO.
     */
    @Test
    void convertToDTO_Success() {
        // Arrange
        Department dept = new Department();
        dept.setDepartmentName("Engineering");
        employee.setDepartment(dept);

        Designation desig = new Designation();
        desig.setDesignationName("Developer");
        employee.setDesignation(desig);

        // Act
        EmployeeDTO dto = employeeService.convertToDTO(employee);

        // Assert
        assertThat(dto.getDepartmentName()).isEqualTo("Engineering");
        assertThat(dto.getDesignationName()).isEqualTo("Developer");
    }
}