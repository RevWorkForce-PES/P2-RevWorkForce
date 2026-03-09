package com.revature.revworkforce.controller;

import com.revature.revworkforce.dto.DepartmentDTO;
import com.revature.revworkforce.service.DepartmentService;
import com.revature.revworkforce.service.DesignationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private DesignationService designationService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    // ============================
    // List Departments
    // ============================

    @Test
    void listDepartments_ShouldReturnDepartmentListPage() throws Exception {

        DepartmentDTO dept = new DepartmentDTO();
        dept.setDepartmentId(1L);
        dept.setDepartmentName("IT");

        when(departmentService.getAllDepartments()).thenReturn(List.of(dept));

        mockMvc.perform(get("/admin/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/departments/list"))
                .andExpect(model().attributeExists("departments"));
    }

    // ============================
    // Show Add Form
    // ============================

    @Test
    void showAddDepartmentForm_ShouldReturnForm() throws Exception {

        mockMvc.perform(get("/admin/departments/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/departments/form"))
                .andExpect(model().attributeExists("departmentDTO"));
    }

    // ============================
    // Add Department
    // ============================

    @Test
    void addDepartment_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/departments/add")
                .param("departmentName", "HR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/departments"));

        verify(departmentService).createDepartment(any(DepartmentDTO.class));
    }

    // ============================
    // Show Edit Form
    // ============================

    @Test
    void showEditDepartmentForm_ShouldReturnForm() throws Exception {

        DepartmentDTO dept = new DepartmentDTO();
        dept.setDepartmentId(1L);
        dept.setDepartmentName("Finance");

        when(departmentService.getDepartmentById(1L)).thenReturn(dept);

        mockMvc.perform(get("/admin/departments/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/departments/form"))
                .andExpect(model().attributeExists("departmentDTO"));
    }

    // ============================
    // Update Department
    // ============================

    @Test
    void editDepartment_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/departments/edit/1")
                .param("departmentName", "Finance"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/departments"));

        verify(departmentService).updateDepartment(eq(1L), any(DepartmentDTO.class));
    }

    // ============================
    // Delete Department
    // ============================

    @Test
    void deleteDepartment_ShouldRedirect() throws Exception {

        DepartmentDTO dept = new DepartmentDTO();
        dept.setDepartmentId(1L);
        dept.setDepartmentName("IT");

        when(departmentService.getDepartmentById(1L)).thenReturn(dept);

        mockMvc.perform(post("/admin/departments/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/departments"));

        verify(departmentService).deleteDepartment(1L);
    }
}