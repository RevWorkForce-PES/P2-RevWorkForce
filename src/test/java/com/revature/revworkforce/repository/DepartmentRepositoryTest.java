package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Use the 'test' profile to load the correct properties file
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department itDepartment;
    private Department hrDepartment;

    @BeforeEach
    void setUp() {
        // Arrange: create departments
        itDepartment = new Department("IT");
        itDepartment.setIsActive('Y');
        departmentRepository.save(itDepartment);

        hrDepartment = new Department("HR");
        hrDepartment.setIsActive('N');
        departmentRepository.save(hrDepartment);
    }

    @Test
    @DisplayName("Find department by name")
    void findByDepartmentName_ShouldReturnDepartment() {
        // Act
        Optional<Department> result = departmentRepository.findByDepartmentName("IT");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getDepartmentName()).isEqualTo("IT");
    }

    @Test
    @DisplayName("Find all active departments")
    void findByIsActive_ShouldReturnActiveDepartments() {
        // Act
        List<Department> activeDepartments = departmentRepository.findByIsActive('Y');

        // Assert
        assertThat(activeDepartments).isNotEmpty();
        assertThat(activeDepartments).extracting("departmentName").containsExactly("IT");
    }

    @Test
    @DisplayName("Check if department exists by name")
    void existsByDepartmentName_ShouldReturnTrue() {
        // Act
        boolean exists = departmentRepository.existsByDepartmentName("IT");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Find all departments ordered by name")
    void findAllByOrderByDepartmentNameAsc_ShouldReturnOrderedDepartments() {
        // Act
        List<Department> departments = departmentRepository.findAllByOrderByDepartmentNameAsc();

        // Assert
        assertThat(departments).hasSize(2);
        assertThat(departments.get(0).getDepartmentName()).isEqualTo("HR");
        assertThat(departments.get(1).getDepartmentName()).isEqualTo("IT");
    }

    @Test
    @DisplayName("Save and update department")
    void saveAndUpdateDepartment_ShouldPersistChanges() {
        // Arrange
        Department financeDept = new Department("Finance");
        financeDept.setIsActive('Y');

        // Act
        Department saved = departmentRepository.save(financeDept);
        saved.setDepartmentHead("Alice");
        Department updated = departmentRepository.save(saved);

        // Assert
        assertThat(updated.getDepartmentHead()).isEqualTo("Alice");
        assertThat(updated.getIsActive()).isEqualTo('Y');
    }

    @Test
    @DisplayName("Delete department")
    void deleteDepartment_ShouldRemoveEntry() {
        // Act
        departmentRepository.delete(itDepartment);

        // Assert
        Optional<Department> deleted = departmentRepository.findById(itDepartment.getDepartmentId());
        assertThat(deleted).isNotPresent();
    }
}