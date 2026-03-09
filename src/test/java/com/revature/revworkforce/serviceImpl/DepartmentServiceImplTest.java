package com.revature.revworkforce.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.revworkforce.dto.DepartmentDTO;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.model.Department;
import com.revature.revworkforce.repository.DepartmentRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.impl.DepartmentServiceImpl;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private com.revature.revworkforce.service.AuditService auditService;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(1L);
        departmentDTO.setDepartmentName("IT");
        departmentDTO.setDepartmentHead("Alice");
        departmentDTO.setDescription("IT Department");
        departmentDTO.setIsActive('Y');
    }

    @Test
    void createDepartment_ShouldReturnDepartmentDTO() {
        // Arrange
        Department department = new Department("IT");
        department.setDepartmentId(1L);
        department.setDepartmentHead("Alice");
        department.setDescription("IT Department");
        department.setIsActive('Y');

        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn((long) 10);

        // Act
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

        // Assert
        assertThat(createdDepartment).isNotNull();
        assertThat(createdDepartment.getDepartmentName()).isEqualTo("IT");
        assertThat(createdDepartment.getEmployeeCount()).isEqualTo(10);
    }

    @Test
    void createDepartment_ShouldThrowDuplicateResourceException() {
        // Arrange
        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartmentDTO() {
        // Arrange
        Department existingDepartment = new Department("IT");
        existingDepartment.setDepartmentId(1L);
        existingDepartment.setDepartmentHead("Alice");
        existingDepartment.setDescription("IT Department");
        existingDepartment.setIsActive('Y');

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(existingDepartment);
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn((long) 10);

        // Act
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(1L, departmentDTO);

        // Assert
        assertThat(updatedDepartment).isNotNull();
        assertThat(updatedDepartment.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    void updateDepartment_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> departmentService.updateDepartment(1L, departmentDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    @Test
    void getDepartmentById_ShouldReturnDepartmentDTO() {
        // Arrange
        Department existingDepartment = new Department("IT");
        existingDepartment.setDepartmentId(1L);
        existingDepartment.setDepartmentHead("Alice");
        existingDepartment.setDescription("IT Department");
        existingDepartment.setIsActive('Y');

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));

        // Act
        DepartmentDTO department = departmentService.getDepartmentById(1L);

        // Assert
        assertThat(department).isNotNull();
        assertThat(department.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    void getDepartmentById_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> departmentService.getDepartmentById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    @Test
    void getAllDepartments_ShouldReturnListOfDepartmentDTO() {
        // Arrange: Create departments
        Department department1 = new Department("IT");
        department1.setDepartmentHead("Alice");

        Department department2 = new Department("HR");
        department2.setDepartmentHead("Bob");

        // Mock the repository to return departments in sorted order
        when(departmentRepository.findAllByOrderByDepartmentNameAsc()).thenReturn(List.of(department2, department1)); // HR
                                                                                                                      // first,
                                                                                                                      // IT
                                                                                                                      // second
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn((long) 10);

        // Act: Call the service method
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        // Assert: Verify the returned departments are ordered correctly
        assertThat(departments).hasSize(2);
        assertThat(departments.get(0).getDepartmentName()).isEqualTo("HR"); // HR should be first
        assertThat(departments.get(1).getDepartmentName()).isEqualTo("IT"); // IT should be second
    }

    @Test
    void deleteDepartment_ShouldMarkAsInactive() {
        // Arrange
        Department department = new Department("IT");
        department.setDepartmentId(1L);
        department.setIsActive('Y');

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        // Act
        departmentService.deleteDepartment(1L);

        // Assert
        assertThat(department.getIsActive()).isEqualTo('N');
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void deleteDepartment_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> departmentService.deleteDepartment(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }
}