package com.revature.revworkforce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for Department data transfer.
 * 
 * Used for creating and updating department information.
 * 
 * @author RevWorkForce Team
 */
public class DepartmentDTO {

    private Long departmentId;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String departmentName;

    @Size(max = 20, message = "Department head name cannot exceed 20 characters")
    private String departmentHead;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Character isActive = 'Y';

    // Constructors
    public DepartmentDTO() {
    }

    public DepartmentDTO(String departmentName) {
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
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
}
