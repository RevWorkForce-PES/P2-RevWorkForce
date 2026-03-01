package com.revature.revworkforce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for Designation data transfer.
 * 
 * Used for creating and updating designation information.
 * 
 * @author RevWorkForce Team
 */
public class DesignationDTO {

    private Long designationId;

    @NotBlank(message = "Designation name is required")
    @Size(min = 2, max = 100, message = "Designation name must be between 2 and 100 characters")
    private String designationName;

    @Size(max = 20, message = "Designation level cannot exceed 20 characters")
    private String designationLevel;

    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary cannot be negative")
    private BigDecimal minSalary;

    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum salary cannot be negative")
    private BigDecimal maxSalary;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Character isActive = 'Y';

    private Long employeeCount = 0L;

    // Constructors
    public DesignationDTO() {
    }

    public DesignationDTO(String designationName) {
        this.designationName = designationName;
    }

    // Getters and Setters
    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDesignationLevel() {
        return designationLevel;
    }

    public void setDesignationLevel(String designationLevel) {
        this.designationLevel = designationLevel;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Long employeeCount) {
        this.employeeCount = employeeCount;
    }
}
