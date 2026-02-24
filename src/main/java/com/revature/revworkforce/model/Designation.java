package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing job designations.
 * Independent table without foreign keys.
 */
@Entity
@Table(name = "DESIGNATIONS")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "desig_seq_gen")
    @SequenceGenerator(name = "desig_seq_gen", sequenceName = "desig_seq", allocationSize = 1)
    @Column(name = "designation_id")
    private Long designationId;

    @Column(name = "designation_name", nullable = false, unique = true)
    private String designationName;

    @Column(name = "designation_level")
    private String designationLevel;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Designation() {}

    // Getters and Setters

    public Long getDesignationId() { return designationId; }

    public void setDesignationId(Long designationId) { this.designationId = designationId; }

    public String getDesignationName() { return designationName; }

    public void setDesignationName(String designationName) { this.designationName = designationName; }

    public String getDesignationLevel() { return designationLevel; }

    public void setDesignationLevel(String designationLevel) { this.designationLevel = designationLevel; }

    public Double getMinSalary() { return minSalary; }

    public void setMinSalary(Double minSalary) { this.minSalary = minSalary; }

    public Double getMaxSalary() { return maxSalary; }

    public void setMaxSalary(Double maxSalary) { this.maxSalary = maxSalary; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIsActive() { return isActive; }

    public void setIsActive(String isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Designation{" +
                "designationId=" + designationId +
                ", designationName='" + designationName + '\'' +
                ", designationLevel='" + designationLevel + '\'' +
                '}';
    }
}
