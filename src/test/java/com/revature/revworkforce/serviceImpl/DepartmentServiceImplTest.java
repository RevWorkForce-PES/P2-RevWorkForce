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
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.impl.DepartmentServiceImpl;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService; // Mocking AuditService

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up a mock departmentDTO
        departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(1L);
        departmentDTO.setDepartmentName("IT");
        departmentDTO.setDepartmentHead("Alice");
        departmentDTO.setDescription("IT Department");
        departmentDTO.setIsActive('Y');
    }

    @Test
    void createDepartment_ShouldReturnDepartmentDTO() {
        // Arrange: Create a mock department
        Department department = new Department("IT");
        department.setDepartmentId(1L);
        department.setDepartmentHead("Alice");
        department.setDescription("IT Department");
        department.setIsActive('Y');
        department.setDepartmentId(1L); // Ensure department ID is set

        // Mock repository behaviors
        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(false); // No
                                                                                                                // duplicate
                                                                                                                // department
                                                                                                                // name
        when(departmentRepository.save(any(Department.class))).thenReturn(department); // Simulate saving the department
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn((long) 10); // Assume 10 employees
                                                                                                 // in the department

        // Act: Call the service method
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);

        // Assert: Verify the returned DepartmentDTO
        assertThat(createdDepartment).isNotNull();
        assertThat(createdDepartment.getDepartmentName()).isEqualTo("IT");
        assertThat(createdDepartment.getEmployeeCount()).isEqualTo(10);
    }

    @Test
    void createDepartment_ShouldThrowDuplicateResourceException() {
        // Arrange: Simulate a department with the same name already existing
        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(true);

        // Act & Assert: Assert that the exception is thrown
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartmentDTO() {
        // Arrange: Create an existing department to simulate the database record
        Department existingDepartment = new Department("IT");
        existingDepartment.setDepartmentId(1L);
        existingDepartment.setDepartmentHead("Alice");
        existingDepartment.setDescription("IT Department");
        existingDepartment.setIsActive('Y');

        // Mock repository interactions
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment)); // Finding the existing
                                                                                             // department by ID
        when(departmentRepository.existsByDepartmentName(departmentDTO.getDepartmentName())).thenReturn(false); // No
                                                                                                                // duplicate
                                                                                                                // department
                                                                                                                // name
        when(departmentRepository.save(any(Department.class))).thenReturn(existingDepartment); // Simulating saving the
                                                                                               // updated department
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn((long) 10); // Assume 10 employees
                                                                                                 // in the department

        // Act: Call the update method
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(1L, departmentDTO);

        // Assert: Verify the updated department
        assertThat(updatedDepartment).isNotNull();
        assertThat(updatedDepartment.getDepartmentName()).isEqualTo("IT");
        assertThat(updatedDepartment.getEmployeeCount()).isEqualTo(10);
    }

    @Test
    void updateDepartment_ShouldThrowResourceNotFoundException() {
        // Arrange: Simulate department not found by ID
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Assert that the exception is thrown
        assertThatThrownBy(() -> departmentService.updateDepartment(1L, departmentDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    @Test
    void getDepartmentById_ShouldReturnDepartmentDTO() {
        // Arrange: Create a mock department
        Department existingDepartment = new Department("IT");
        existingDepartment.setDepartmentId(1L);
        existingDepartment.setDepartmentHead("Alice");
        existingDepartment.setDescription("IT Department");
        existingDepartment.setIsActive('Y');

        // Mock repository behavior
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDepartment));

        // Act: Call the service method to get the department by ID
        DepartmentDTO department = departmentService.getDepartmentById(1L);

        // Assert: Verify the returned department
        assertThat(department).isNotNull();
        assertThat(department.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    void getDepartmentById_ShouldThrowResourceNotFoundException() {
        // Arrange: Simulate department not found by ID
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Assert that the exception is thrown
        assertThatThrownBy(() -> departmentService.getDepartmentById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    @Test
    void getAllDepartments_ShouldReturnListOfDepartmentDTO() {
        // Arrange: Create mock departments
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

        // Act: Call the service method to get all departments
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        // Assert: Verify the returned departments are ordered correctly
        assertThat(departments).hasSize(2);
        assertThat(departments.get(0).getDepartmentName()).isEqualTo("HR"); // HR should be first
        assertThat(departments.get(1).getDepartmentName()).isEqualTo("IT"); // IT should be second
    }

    @Test
    void deleteDepartment_ShouldMarkAsInactive() {
        // Arrange: Create a mock department
        Department department = new Department("IT");
        department.setDepartmentId(1L);
        department.setIsActive('Y');

        // Mock repository interactions
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        // Act: Call the service method to delete the department
        departmentService.deleteDepartment(1L);

        // Assert: Verify the department is marked as inactive
        assertThat(department.getIsActive()).isEqualTo('N');
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void deleteDepartment_ShouldThrowResourceNotFoundException() {
        // Arrange: Simulate department not found by ID
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Assert that the exception is thrown
        assertThatThrownBy(() -> departmentService.deleteDepartment(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }
}