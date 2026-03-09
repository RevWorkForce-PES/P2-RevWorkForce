package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.DesignationDTO;
import com.revature.revworkforce.exception.DuplicateResourceException;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.ValidationException;
import com.revature.revworkforce.model.Designation;
import com.revature.revworkforce.repository.DesignationRepository;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.service.AuditService;
import com.revature.revworkforce.service.impl.DesignationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DesignationServiceImplTest {

    @Mock
    private DesignationRepository designationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;  // Mock AuditService

    @InjectMocks
    private DesignationServiceImpl designationService;

    private DesignationDTO validDesignationDTO;
    private DesignationDTO invalidSalaryDesignationDTO;

    @BeforeEach
    void setUp() {
        validDesignationDTO = new DesignationDTO();
        validDesignationDTO.setDesignationName("Software Engineer");
        validDesignationDTO.setDesignationLevel("Mid");
        validDesignationDTO.setMinSalary(new BigDecimal("50000"));
        validDesignationDTO.setMaxSalary(new BigDecimal("100000"));
        validDesignationDTO.setDescription("Responsible for development tasks.");
        validDesignationDTO.setIsActive('Y');

        invalidSalaryDesignationDTO = new DesignationDTO();
        invalidSalaryDesignationDTO.setDesignationName("Software Engineer");
        invalidSalaryDesignationDTO.setDesignationLevel("Mid");
        invalidSalaryDesignationDTO.setMinSalary(new BigDecimal("100000"));
        invalidSalaryDesignationDTO.setMaxSalary(new BigDecimal("50000"));
        invalidSalaryDesignationDTO.setDescription("Responsible for development tasks.");
        invalidSalaryDesignationDTO.setIsActive('Y');
    }

    @Test
    void createDesignation_ShouldCreateDesignation() {
        // Arrange: Create a mock designation
        Designation newDesignation = new Designation(validDesignationDTO.getDesignationName());
        newDesignation.setDesignationLevel(validDesignationDTO.getDesignationLevel());
        newDesignation.setMinSalary(validDesignationDTO.getMinSalary());
        newDesignation.setMaxSalary(validDesignationDTO.getMaxSalary());
        newDesignation.setDescription(validDesignationDTO.getDescription());
        newDesignation.setIsActive(validDesignationDTO.getIsActive());
        newDesignation.setDesignationId(1L);  // Explicitly set the ID to avoid NullPointerException

        when(designationRepository.existsByDesignationName(validDesignationDTO.getDesignationName())).thenReturn(false);
        when(designationRepository.save(any(Designation.class))).thenReturn(newDesignation);

        // Act
        DesignationDTO created = designationService.createDesignation(validDesignationDTO);

        // Assert
        assertThat(created.getDesignationName()).isEqualTo("Software Engineer");
        verify(designationRepository).save(any(Designation.class));  // Ensure save is called
    }

    @Test
    void createDesignation_ShouldThrowDuplicateResourceException_WhenDesignationAlreadyExists() {
        // Arrange
        when(designationRepository.existsByDesignationName(validDesignationDTO.getDesignationName())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> designationService.createDesignation(validDesignationDTO));
    }

    @Test
    void updateDesignation_ShouldUpdateDesignation() {
       
        // Arrange: Create an existing designation
        Designation existingDesignation = new Designation(validDesignationDTO.getDesignationName());
        existingDesignation.setDesignationId(1L);  // Ensure designationId is set
        existingDesignation.setDesignationLevel(validDesignationDTO.getDesignationLevel());
        existingDesignation.setMinSalary(validDesignationDTO.getMinSalary());
        existingDesignation.setMaxSalary(validDesignationDTO.getMaxSalary());
        existingDesignation.setDescription(validDesignationDTO.getDescription());
        existingDesignation.setIsActive(validDesignationDTO.getIsActive());

        when(designationRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));
        when(designationRepository.save(any(Designation.class))).thenReturn(existingDesignation);

        // Act
        DesignationDTO updatedDesignation = designationService.updateDesignation(1L, validDesignationDTO);

        // Assert
        assertThat(updatedDesignation.getDesignationName()).isEqualTo(validDesignationDTO.getDesignationName());
        verify(designationRepository).save(any(Designation.class));  // Ensure save is called
    }

    @Test
    void updateDesignation_ShouldThrowResourceNotFoundException_WhenDesignationDoesNotExist() {
        // Arrange
        when(designationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> designationService.updateDesignation(1L, validDesignationDTO));
    }

    @Test
    void updateDesignation_ShouldThrowValidationException_WhenMinSalaryGreaterThanMaxSalary() {
        // Arrange: Create an existing Designation entity with valid data
        Designation existingDesignation = new Designation("Software Engineer");
        existingDesignation.setDesignationId(1L);  // Ensure designationId is set
        existingDesignation.setMinSalary(new BigDecimal("100000"));
        existingDesignation.setMaxSalary(new BigDecimal("50000"));  // Min > Max salary to trigger validation
        when(designationRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));

        // Act & Assert: Expect a ValidationException to be thrown
        assertThrows(ValidationException.class, () -> designationService.updateDesignation(1L, invalidSalaryDesignationDTO));
    }

    @Test
    void getDesignationById_ShouldReturnDesignation() {
        // Arrange
        Designation designation = new Designation(validDesignationDTO.getDesignationName());
        when(designationRepository.findById(1L)).thenReturn(Optional.of(designation));

        // Act
        DesignationDTO result = designationService.getDesignationById(1L);

        // Assert
        assertThat(result.getDesignationName()).isEqualTo(validDesignationDTO.getDesignationName());
    }

    @Test
    void getDesignationById_ShouldThrowResourceNotFoundException_WhenDesignationDoesNotExist() {
        // Arrange
        when(designationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> designationService.getDesignationById(1L));
    }

    @Test
    void deleteDesignation_ShouldSoftDeleteDesignation() {
        // Arrange: Mock the AuditService to avoid NullPointerException
       

        // Arrange: Create an existing designation
        Designation existingDesignation = new Designation(validDesignationDTO.getDesignationName());
        existingDesignation.setDesignationId(1L);  // Ensure designationId is set
        when(designationRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));
        when(designationRepository.save(any(Designation.class))).thenReturn(existingDesignation);

        // Act
        designationService.deleteDesignation(1L);

        // Assert
        assertThat(existingDesignation.getIsActive()).isEqualTo('N');
        verify(designationRepository).save(any(Designation.class));  // Ensure save is called
    }

    @Test
    void isSalaryInRange_ShouldReturnTrue_WhenSalaryIsInRange() {
        // Arrange
        Designation existingDesignation = new Designation(validDesignationDTO.getDesignationName());
        existingDesignation.setMinSalary(new BigDecimal("50000"));
        existingDesignation.setMaxSalary(new BigDecimal("100000"));
        when(designationRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));

        // Act
        boolean result = designationService.isSalaryInRange(1L, new BigDecimal("70000"));

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void isSalaryInRange_ShouldReturnFalse_WhenSalaryIsOutOfRange() {
        // Arrange
        Designation existingDesignation = new Designation(validDesignationDTO.getDesignationName());
        existingDesignation.setMinSalary(new BigDecimal("50000"));
        existingDesignation.setMaxSalary(new BigDecimal("100000"));
        when(designationRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));

        // Act
        boolean result = designationService.isSalaryInRange(1L, new BigDecimal("120000"));

        // Assert
        assertThat(result).isFalse();
    }
}