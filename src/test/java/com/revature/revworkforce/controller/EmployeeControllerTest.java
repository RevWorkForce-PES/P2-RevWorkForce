package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import com.revature.revworkforce.security.SecurityUtils;
import com.revature.revworkforce.service.EmployeeService;
import com.revature.revworkforce.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DesignationRepository designationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private EmployeeDTO createValidEmployeeDTO() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId("EMP001");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setAddress("123 Street");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setDepartmentId(1L);
        dto.setDesignationId(1L);
        dto.setJoiningDate(LocalDate.now());
        dto.setSalary(BigDecimal.valueOf(50000));
        return dto;
    }

    // ============================================
    // ADMIN API ENDPOINTS
    // ============================================

    @Test
    void getNextEmployeeId_Success() throws Exception {
        when(employeeService.generateEmployeeId("EMP")).thenReturn("EMP002");
        mockMvc.perform(get("/admin/api/next-employee-id").param("prefix", "EMP"))
                .andExpect(status().isOk())
                .andExpect(content().string("EMP002"));
    }

    @Test
    void getNextEmployeeId_Exception() throws Exception {
        when(employeeService.generateEmployeeId("EMP")).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/admin/api/next-employee-id").param("prefix", "EMP"))
                .andExpect(status().isOk())
                .andExpect(content().string("EMP001"));
    }

    // ============================================
    // ADMIN ENDPOINTS - Employee Management
    // ============================================

    @Test
    void listEmployees_Success() throws Exception {
        when(employeeService.searchEmployeesAsDTO(any())).thenReturn(Collections.emptyList());
        when(departmentRepository.findAllByOrderByDepartmentNameAsc()).thenReturn(Collections.emptyList());
        when(designationRepository.findAllByOrderByDesignationNameAsc()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/employees")
                .param("keyword", "test")
                .param("departmentId", "1")
                .param("designationId", "1")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-management"))
                .andExpect(model().attributeExists("employees"));
    }

    @Test
    void showAddEmployeeForm_Success() throws Exception {
        mockMvc.perform(get("/admin/employees/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-form"))
                .andExpect(model().attributeExists("employeeDTO"));
    }

    @Test
    void addEmployee_ValidationError() throws Exception {
        mockMvc.perform(post("/admin/employees/add")
                .flashAttr("employeeDTO", new EmployeeDTO()))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-form"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void addEmployee_Success() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setEmployeeId("EMP001");

        when(employeeService.createEmployee(any())).thenReturn(emp);

        mockMvc.perform(post("/admin/employees/add")
                .flashAttr("employeeDTO", createValidEmployeeDTO()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    void addEmployee_ServiceException() throws Exception {
        when(employeeService.createEmployee(any())).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(post("/admin/employees/add")
                .flashAttr("employeeDTO", createValidEmployeeDTO()))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-form"))
                .andExpect(model().attribute("error", "fail"));
    }

    @Test
    void showEditEmployeeForm_Success() throws Exception {
        when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(new EmployeeDTO());

        mockMvc.perform(get("/admin/employees/edit/EMP001"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-form"))
                .andExpect(model().attribute("isEdit", true));
    }

    @Test
    void editEmployee_Success() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");

        when(employeeService.updateEmployee(eq("EMP001"), any())).thenReturn(emp);

        mockMvc.perform(post("/admin/employees/edit/EMP001")
                .flashAttr("employeeDTO", createValidEmployeeDTO()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    void editEmployee_ValidationError() throws Exception {
        mockMvc.perform(post("/admin/employees/edit/EMP001")
                .flashAttr("employeeDTO", new EmployeeDTO()))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-form"));
    }

    @Test
    void viewEmployee_Success() throws Exception {
        when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(new EmployeeDTO());
        when(employeeService.getTeamMembers("EMP001")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/employees/view/EMP001"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-view"));
    }

    @Test
    void deactivateEmployee_Success() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

        mockMvc.perform(post("/admin/employees/deactivate/EMP001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).deactivateEmployee("EMP001");
    }

    @Test
    void deactivateEmployee_Exception() throws Exception {
        doThrow(new RuntimeException("fail")).when(employeeService).deactivateEmployee("EMP001");

        mockMvc.perform(post("/admin/employees/deactivate/EMP001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "fail"));
    }

    @Test
    void reactivateEmployee_Success() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

        mockMvc.perform(post("/admin/employees/reactivate/EMP001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).reactivateEmployee("EMP001");
    }

    @Test
    void deleteEmployee_Success() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);

        mockMvc.perform(post("/admin/employees/delete/EMP001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).deleteEmployee("EMP001");
    }

    // ============================================
    // EMPLOYEE ENDPOINTS - Profile Management
    // ============================================

    @Test
    void viewProfile_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(new EmployeeDTO());

            mockMvc.perform(get("/employee/profile"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/profile-directory"));
        }
    }

    @Test
    void showEditProfileForm_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            Employee emp = new Employee();
            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);
            when(employeeService.convertToDTO(emp)).thenReturn(new EmployeeDTO());

            mockMvc.perform(get("/employee/profile/edit"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/profile-edit"));
        }
    }

    @Test
    void updateProfile_ValidationErrors() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            mockMvc.perform(post("/employee/profile/update")
                    .param("phone", "123") // Invalid
                    .param("postalCode", "abc") // Invalid
                    .param("emergencyContactPhone", "456") // Invalid
                    .param("address", "a".repeat(501))) // Long
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/profile-edit"))
                    .andExpect(model().attributeExists("error"));
        }
    }

    @Test
    void updateProfile_Success() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");
            Employee emp = new Employee();
            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);
            when(employeeService.convertToDTO(emp)).thenReturn(new EmployeeDTO());

            mockMvc.perform(post("/employee/profile/update")
                    .param("phone", "9876543210")
                    .param("postalCode", "123456")
                    .param("emergencyContactPhone", "8876543210")
                    .param("address", "Valid Address"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employee/profile"));
        }
    }

    @Test
    void viewDirectory_WithSearch() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("EMP001");

            Employee emp = new Employee();
            when(employeeService.getEmployeeById("EMP001")).thenReturn(emp);
            when(employeeService.convertToDTO(emp)).thenReturn(new EmployeeDTO());

            mockMvc.perform(get("/employee/directory")
                    .param("search", "John")
                    .param("departmentId", "1")
                    .param("designationId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/employee/profile-directory"))
                    .andExpect(model().attribute("search", "John"));

            verify(employeeService).searchEmployeesAsDTO(any());
        }
    }

    @Test
    void viewDirectory_NoEmployeeRecord() throws Exception {
        try (MockedStatic<SecurityUtils> security = mockStatic(SecurityUtils.class)) {
            security.when(SecurityUtils::getCurrentUsername).thenReturn("ADMIN");
            when(employeeService.getEmployeeById("ADMIN")).thenThrow(new ResourceNotFoundException("Not found"));

            mockMvc.perform(get("/employee/directory"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeDoesNotExist("currentUser"));
        }
    }

    @Test
    void searchEmployeesAlias_Success() throws Exception {
        mockMvc.perform(get("/admin/employees/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-management"));
    }

    @Test
    void filterByDepartment_Success() throws Exception {
        mockMvc.perform(get("/admin/employees/department/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/admin/employee-management"));
    }
}