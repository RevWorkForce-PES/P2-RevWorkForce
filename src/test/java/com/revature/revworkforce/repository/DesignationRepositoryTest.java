package com.revature.revworkforce.repository;

import com.revature.revworkforce.model.Designation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") 
class DesignationRepositoryTest {

    @Autowired
    private DesignationRepository designationRepository;

    private Designation juniorDeveloper;
    private Designation seniorDeveloper;
    private Designation inactiveManager;

    @BeforeEach
    void setUp() {
        // Arrange: Create some designations
        juniorDeveloper = new Designation("Junior Developer");
        juniorDeveloper.setDesignationLevel("Junior");
        juniorDeveloper.setMinSalary(new BigDecimal("40000"));
        juniorDeveloper.setMaxSalary(new BigDecimal("60000"));
        juniorDeveloper.setIsActive('Y');
        designationRepository.save(juniorDeveloper);

        seniorDeveloper = new Designation("Senior Developer");
        seniorDeveloper.setDesignationLevel("Senior");
        seniorDeveloper.setMinSalary(new BigDecimal("80000"));
        seniorDeveloper.setMaxSalary(new BigDecimal("120000"));
        seniorDeveloper.setIsActive('Y');
        designationRepository.save(seniorDeveloper);

        inactiveManager = new Designation("Manager");
        inactiveManager.setDesignationLevel("Mid");
        inactiveManager.setMinSalary(new BigDecimal("70000"));
        inactiveManager.setMaxSalary(new BigDecimal("100000"));
        inactiveManager.setIsActive('N');
        designationRepository.save(inactiveManager);
    }

    @Test
    @DisplayName("Find designation by name")
    void findByDesignationName_ShouldReturnDesignation() {
        // Act
        Optional<Designation> result = designationRepository.findByDesignationName("Junior Developer");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getDesignationName()).isEqualTo("Junior Developer");
    }

    @Test
    @DisplayName("Find all active designations")
    void findByIsActive_ShouldReturnActiveDesignations() {
        // Act
        List<Designation> activeDesignations = designationRepository.findByIsActive('Y');

        // Assert
        assertThat(activeDesignations).isNotEmpty();
        assertThat(activeDesignations).extracting("designationName").containsExactly("Junior Developer", "Senior Developer");
    }

    @Test
    @DisplayName("Check if designation exists by name")
    void existsByDesignationName_ShouldReturnTrue() {
        // Act
        boolean exists = designationRepository.existsByDesignationName("Senior Developer");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Find all designations ordered by name")
    void findAllByOrderByDesignationNameAsc_ShouldReturnOrderedDesignations() {
        // Act
        List<Designation> designations = designationRepository.findAllByOrderByDesignationNameAsc();

        // Assert
        assertThat(designations).hasSize(3);
        assertThat(designations.get(0).getDesignationName()).isEqualTo("Junior Developer");
        assertThat(designations.get(1).getDesignationName()).isEqualTo("Manager");
        assertThat(designations.get(2).getDesignationName()).isEqualTo("Senior Developer");
    }

    @Test
    @DisplayName("Find designations by level")
    void findByDesignationLevel_ShouldReturnDesignations() {
        // Act
        List<Designation> juniorDesignations = designationRepository.findByDesignationLevel("Junior");

        // Assert
        assertThat(juniorDesignations).isNotEmpty();
        assertThat(juniorDesignations).extracting("designationName").containsExactly("Junior Developer");
    }

    @Test
    @DisplayName("Save and update designation")
    void saveAndUpdateDesignation_ShouldPersistChanges() {
        // Arrange
        Designation newDesignation = new Designation("Lead Developer");
        newDesignation.setDesignationLevel("Senior");
        newDesignation.setMinSalary(new BigDecimal("90000"));
        newDesignation.setMaxSalary(new BigDecimal("130000"));
        newDesignation.setIsActive('Y');

        // Act
        Designation saved = designationRepository.save(newDesignation);
        saved.setDesignationLevel("Lead");
        Designation updated = designationRepository.save(saved);

        // Assert
        assertThat(updated.getDesignationLevel()).isEqualTo("Lead");
    }

    @Test
    @DisplayName("Delete designation")
    void deleteDesignation_ShouldRemoveEntry() {
        // Act
        designationRepository.delete(juniorDeveloper);

        // Assert
        Optional<Designation> deleted = designationRepository.findById(juniorDeveloper.getDesignationId());
        assertThat(deleted).isNotPresent();
    }
}