package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.EmployeeStatus;

/**
 * DTO for Employee search and filter criteria.
 * 
 * @author RevWorkForce Team
 */
public class EmployeeSearchCriteria {
    
    private String keyword;           // Search in name, email, employee ID
    private Long departmentId;        // Filter by department
    private Long designationId;       // Filter by designation
    private EmployeeStatus status;    // Filter by status
    private String managerId;         // Filter by manager
    
    // Constructors
    public EmployeeSearchCriteria() {
    }
    
    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
    public Long getDesignationId() {
        return designationId;
    }
    
    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }
    
    public EmployeeStatus getStatus() {
        return status;
    }
    
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    /**
     * Check if any filter is applied.
     * 
     * @return true if any filter is set
     */
    public boolean hasFilters() {
        return (keyword != null && !keyword.trim().isEmpty()) ||
               departmentId != null ||
               designationId != null ||
               status != null ||
               (managerId != null && !managerId.trim().isEmpty());
    }
}