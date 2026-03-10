package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.Gender;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.EmployeeRole;
import com.revature.revworkforce.model.Role;
import com.revature.revworkforce.repository.*;
import com.revature.revworkforce.service.impl.EmployeeServiceImpl;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.security.SecurityUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        employee.setStatus(EmployeeStatus.ACTIVE);
    }

    // ==========================================
    // CREATE EMPLOYEE
    // ==========================================

    @Test
    void createEmployee_Success_WithDefaultRole() {
        employeeDTO.setRoleIds(null); // Force default role
        Role defaultRole = new Role();
        defaultRole.setRoleId(1L);
        defaultRole.setRoleName("EMPLOYEE");

        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsById("EMP001")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(roleRepository.findByRoleName("EMPLOYEE")).thenReturn(Optional.of(defaultRole));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(defaultRole));
        when(employeeRoleRepository.existsByEmployeeIdAndRoleId(anyString(), anyLong())).thenReturn(false);

        Employee result = employeeService.createEmployee(employeeDTO);

        assertThat(result).isNotNull();
        verify(employeeRepository).save(any(Employee.class));
        verify(auditService, times(2)).createAuditLog(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void createEmployee_Success_WithCustomRoles() {
        employeeDTO.setRoleIds(Set.of(2L));
        Role customRole = new Role();
        customRole.setRoleId(2L);
        customRole.setRoleName("MANAGER");

        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsById("EMP001")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(customRole));

        Employee result = employeeService.createEmployee(employeeDTO);
        assertThat(result).isNotNull();
    }

    @Test
    void createEmployee_AutoGenerateId() {
        employeeDTO.setEmployeeId(null);
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList()); // Will generate EMP001
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsById("EMP001")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        Role defaultRole = new Role();
        defaultRole.setRoleId(1L);
        when(roleRepository.findByRoleName("EMPLOYEE")).thenReturn(Optional.of(defaultRole));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(defaultRole));

        Employee result = employeeService.createEmployee(employeeDTO);
        assertThat(result).isNotNull();
    }

    @Test
    void createEmployee_DuplicateEmail_ShouldThrowException() {
        when(employeeRepository.existsByEmail(employeeDTO.getEmail())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(employeeDTO));
    }

    @Test
    void createEmployee_DuplicateId_ShouldThrowException() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsById("EMP001")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(employeeDTO));
    }

    @Test
    void createEmployee_InvalidAge_ShouldThrowException() {
        employeeDTO.setDateOfBirth(LocalDate.now().minusYears(17)); // Under 18
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        assertThrows(ValidationException.class, () -> employeeService.createEmployee(employeeDTO));
    }

    // ==========================================
    // UPDATE EMPLOYEE
    // ==========================================

    @Test
    void updateEmployee_Success_AllFields() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeDTO.setFirstName("Updated");
        employeeDTO.setLastName("Last");
        employeeDTO.setPhone("12345");
        employeeDTO.setGender(Gender.M);
        employeeDTO.setAddress("Addr");
        employeeDTO.setCity("City");
        employeeDTO.setState("State");
        employeeDTO.setPostalCode("123");
        employeeDTO.setCountry("USA");
        employeeDTO.setEmergencyContactName("EContact");
        employeeDTO.setEmergencyContactPhone("987");
        employeeDTO.setJoiningDate(LocalDate.now());
        employeeDTO.setLeavingDate(LocalDate.now());
        employeeDTO.setSalary(BigDecimal.TEN);
        employeeDTO.setStatus(EmployeeStatus.ACTIVE);

        employeeDTO.setDepartmentId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(new Department()));

        employeeDTO.setDesignationId(2L);
        when(designationRepository.findById(2L)).thenReturn(Optional.of(new Designation()));

        employeeDTO.setManagerId("MGR");
        when(employeeRepository.findById("MGR")).thenReturn(Optional.of(new Employee()));

        employeeDTO.setRoleIds(Set.of(3L));
        Role role = new Role();
        role.setRoleId(3L);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));

        Employee result = employeeService.updateEmployee("EMP001", employeeDTO);

        assertThat(result).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Updated");
        verify(employeeRoleRepository).deleteByEmployeeId("EMP001");
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_DuplicateEmail() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        employeeDTO.setEmail("new@example.com");
        when(employeeRepository.existsByEmail("new@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.updateEmployee("EMP001", employeeDTO));
    }

    // ==========================================
    // GETTERS & DELETIONS
    // ==========================================

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        Employee result = employeeService.getEmployeeById("EMP001");
        assertThat(result).isNotNull();
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById("EMP001"));
    }

    @Test
    void getEmployeeDTOById_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        EmployeeDTO result = employeeService.getEmployeeDTOById("EMP001");
        assertThat(result).isNotNull();
    }

    @Test
    void getAllEmployees_Success() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        assertThat(employeeService.getAllEmployees()).hasSize(1);
    }

    @Test
    void getActiveEmployees_Success() {
        when(employeeRepository.findByStatus(EmployeeStatus.ACTIVE)).thenReturn(List.of(employee));
        assertThat(employeeService.getActiveEmployees()).hasSize(1);
        assertThat(employeeService.getActiveEmployeesAsDTO()).hasSize(1);
    }

    @Test
    void deactivateEmployee_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        employeeService.deactivateEmployee("EMP001");
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.INACTIVE);
        verify(employeeRepository).save(employee);
    }

    @Test
    void reactivateEmployee_Success() {
        employee.setStatus(EmployeeStatus.INACTIVE);
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        employeeService.reactivateEmployee("EMP001");
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.ACTIVE);
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee("EMP001");
        assertThat(employee.getStatus()).isEqualTo(EmployeeStatus.TERMINATED);
    }

    // ==========================================
    // SEARCH & FILTERING
    // ==========================================

    @Test
    void searchEmployees_Combinations() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setKeyword("John");
        criteria.setDepartmentId(1L);
        criteria.setDesignationId(2L);
        criteria.setStatus(EmployeeStatus.ACTIVE);
        criteria.setManagerId("MGR");

        Department dept = new Department();
        dept.setDepartmentId(1L);
        employee.setDepartment(dept);

        Designation desig = new Designation();
        desig.setDesignationId(2L);
        employee.setDesignation(desig);

        Employee manager = new Employee();
        manager.setEmployeeId("MGR");
        employee.setManager(manager);

        when(employeeRepository.searchByKeyword("John")).thenReturn(List.of(employee));

        List<Employee> results = employeeService.searchEmployees(criteria);
        assertThat(results).hasSize(1);

        List<EmployeeDTO> dtoResults = employeeService.searchEmployeesAsDTO(criteria);
        assertThat(dtoResults).hasSize(1);
    }

    @Test
    void searchEmployees_NoKeyword_JustManagerId() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setManagerId("MGR");

        Employee manager = new Employee();
        manager.setEmployeeId("MGR");
        employee.setManager(manager);

        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> results = employeeService.searchEmployees(criteria);
        assertThat(results).hasSize(1);
    }

    @Test
    void getEmployeesByDepartment_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(new Department()));
        when(employeeRepository.findByDepartment(any())).thenReturn(List.of(employee));
        assertThat(employeeService.getEmployeesByDepartment(1L)).hasSize(1);
    }

    @Test
    void getEmployeesByDesignation_Success() {
        when(designationRepository.findById(1L)).thenReturn(Optional.of(new Designation()));
        when(employeeRepository.findByDesignation(any())).thenReturn(List.of(employee));
        assertThat(employeeService.getEmployeesByDesignation(1L)).hasSize(1);
    }

    @Test
    void getTeamMembers_Success() {
        when(employeeRepository.findByManager_EmployeeId("MGR")).thenReturn(List.of(employee));
        assertThat(employeeService.getTeamMembers("MGR")).hasSize(1);
    }

    // ==========================================
    // ROLES
    // ==========================================

    @Test
    void assignRole_Success() {
        Role role = new Role();
        role.setRoleId(2L);
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(employeeRoleRepository.existsByEmployeeIdAndRoleId("EMP001", 2L)).thenReturn(false);

        employeeService.assignRole("EMP001", 2L);
        verify(employeeRoleRepository).save(any());
    }

    @Test
    void assignRole_RoleNotFound() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.assignRole("EMP001", 2L));
    }

    @Test
    void removeRole_Success() {
        employeeService.removeRole("EMP001", 2L);
        verify(employeeRoleRepository).deleteByEmployeeIdAndRoleId("EMP001", 2L);
    }

    @Test
    void getEmployeeRoles_Success() {
        Role role = new Role();
        role.setRoleName("EMPLOYEE");
        employee.setRoles(Set.of(role));
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

        Set<Role> roles = employeeService.getEmployeeRoles("EMP001");
        assertThat(roles).hasSize(1);
    }

    // ==========================================
    // UTILITIES
    // ==========================================

    @Test
    void generateEmployeeId_Complex() {
        Employee e1 = new Employee();
        e1.setEmployeeId("EMP001");
        Employee e2 = new Employee();
        e2.setEmployeeId("EMP005");
        Employee e3 = new Employee();
        e3.setEmployeeId("BADFORMAT"); // coverage for catching NumberFormatException

        when(employeeRepository.findAll()).thenReturn(List.of(e1, e2, e3));

        String id = employeeService.generateEmployeeId("EMP");
        assertThat(id).isEqualTo("EMP006");
    }

    @Test
    void convertToDTO_Success() {
        Department dept = new Department();
        dept.setDepartmentId(1L);
        dept.setDepartmentName("Engineering");
        employee.setDepartment(dept);

        Designation desig = new Designation();
        desig.setDesignationId(1L);
        desig.setDesignationName("Developer");
        employee.setDesignation(desig);

        Employee manager = new Employee();
        manager.setEmployeeId("MGR");
        manager.setFirstName("Boss");
        manager.setLastName("Man");
        employee.setManager(manager);

        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE");
        employee.setRoles(Set.of(role));

        EmployeeDTO dto = employeeService.convertToDTO(employee);

        assertThat(dto.getDepartmentName()).isEqualTo("Engineering");
        assertThat(dto.getDesignationName()).isEqualTo("Developer");
        assertThat(dto.getManagerName()).isEqualTo("Boss Man");
        assertThat(dto.getRoleNames()).contains("ROLE");
    }
}