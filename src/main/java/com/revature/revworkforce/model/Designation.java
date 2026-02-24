package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a Designation (Job Title) in the organization.
 * 
 * Maps to database table: DESIGNATIONS
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "DESIGNATIONS")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "desig_seq")
    @SequenceGenerator(name = "desig_seq", sequenceName = "desig_seq", allocationSize = 1)
    @Column(name = "designation_id")
    private Long designationId;

    @Column(name = "designation_name", nullable = false, unique = true, length = 100)
    private String designationName;

    @Column(name = "designation_level", length = 20)
    private String designationLevel;

    @Column(name = "min_salary", precision = 10, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 10, scale = 2)
    private BigDecimal maxSalary;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active", length = 1)
    private Character isActive = 'Y';

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Designation() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Designation(String designationName) {
        this();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Designation{" +
                "designationId=" + designationId +
                ", designationName='" + designationName + '\'' +
                ", designationLevel='" + designationLevel + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}