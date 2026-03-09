package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.EmployeeDTO;
import com.revature.revworkforce.dto.EmployeeSearchCriteria;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.RoleRepository;
import com.revature.revworkforce.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private DesignationRepository designationRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private RoleRepository roleRepository;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));

        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId("EMP001");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john@example.com");
        employeeDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }

    // ===========================
    // ADD EMPLOYEE
    // ===========================

    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployee_Success() throws Exception {

        mockMvc.perform(post("/admin/employees/add")
                .with(csrf())
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@example.com")
                .param("dateOfBirth", "1990-01-01"))
                .andExpect(status().isOk());
    }

    // ===========================
    // DELETE EMPLOYEE
    // ===========================

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_RedirectsToList() throws Exception {

        when(employeeService.getEmployeeById("EMP001")).thenReturn(employee);
        doNothing().when(employeeService).deleteEmployee("EMP001");

        mockMvc.perform(post("/admin/employees/delete/EMP001")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).deleteEmployee("EMP001");
    }

    // ===========================
    // VIEW EMPLOYEE DETAILS
    // ===========================

    @Test
    @WithMockUser(roles = "ADMIN")
    void viewEmployee_Success() throws Exception {

        when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(employeeDTO);
        when(employeeService.getTeamMembers("EMP001")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/employees/view/EMP001"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(view().name("pages/admin/employee-view"));
    }

    // ===========================
    // EDIT EMPLOYEE FORM
    // ===========================

    @Test
    @WithMockUser(roles = "ADMIN")
    void showEditEmployeeForm_Success() throws Exception {

        when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(employeeDTO);

        mockMvc.perform(get("/admin/employees/edit/EMP001"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employeeDTO"))
                .andExpect(view().name("pages/admin/employee-form"));
    }

    // ===========================
    // LIST EMPLOYEES
    // ===========================

    @Test
    @WithMockUser(roles = "ADMIN")
    void listEmployees_ReturnsPage() throws Exception {

        when(employeeService.searchEmployees(any(EmployeeSearchCriteria.class)))
                .thenReturn(List.of(employee));

        when(employeeService.convertToDTO(any(Employee.class)))
                .thenReturn(employeeDTO);

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employees"))
                .andExpect(view().name("pages/admin/employee-management"));
    }

    // ===========================
    // EMPLOYEE PROFILE VIEW
    // ===========================

    @Test
    @WithMockUser(username = "EMP001", roles = "EMPLOYEE")
    void viewProfile_Success() throws Exception {

        when(employeeService.getEmployeeDTOById("EMP001")).thenReturn(employeeDTO);
        when(employeeService.getActiveEmployeesAsDTO()).thenReturn(List.of(employeeDTO));

        mockMvc.perform(get("/employee/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(view().name("pages/employee/profile-directory"));
    }

    // ===========================
    // EDIT PROFILE FORM
    // ===========================

    @Test
    @WithMockUser(username = "EMP001", roles = "EMPLOYEE")
    void showEditProfileForm_Success() throws Exception {

        when(employeeService.getEmployeeById("EMP001")).thenReturn(employee);
        when(employeeService.convertToDTO(employee)).thenReturn(employeeDTO);

        mockMvc.perform(get("/employee/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employeeDTO"))
                .andExpect(view().name("pages/employee/profile-edit"));
    }

    // ===========================
    // UPDATE PROFILE
    // ===========================

    @Test
    @WithMockUser(username = "EMP001", roles = "EMPLOYEE")
    void updateProfile_Success() throws Exception {

        when(employeeService.getEmployeeById("EMP001")).thenReturn(employee);
        when(employeeService.convertToDTO(employee)).thenReturn(employeeDTO);
        when(employeeService.updateEmployee(eq("EMP001"), any(EmployeeDTO.class))).thenReturn(employee);

        mockMvc.perform(post("/employee/profile/update")
                .with(csrf())
                .param("phone", "9876543210")
                .param("address", "123 Street"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/profile"));
    }

    // ===========================
    // EMPLOYEE DIRECTORY
    // ===========================

    @Test
    @WithMockUser(username = "EMP001", roles = "EMPLOYEE")
    void viewDirectory_Success() throws Exception {

        when(employeeService.getActiveEmployeesAsDTO()).thenReturn(List.of(employeeDTO));
        when(employeeService.getEmployeeById("EMP001")).thenReturn(employee);

        mockMvc.perform(get("/employee/directory"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employees"))
                .andExpect(view().name("pages/employee/profile-directory"));
    }
}