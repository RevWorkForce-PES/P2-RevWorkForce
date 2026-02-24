package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing different types of leave.
 */
@Entity
@Table(name = "LEAVE_TYPES")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_type_seq_gen")
    @SequenceGenerator(name = "leave_type_seq_gen", sequenceName = "leave_type_seq", allocationSize = 1)
    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "leave_code", nullable = false, unique = true)
    private String leaveCode;

    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    @Column(name = "default_days")
    private Integer defaultDays;

    @Column(name = "max_carry_forward")
    private Integer maxCarryForward;

    @Column(name = "description")
    private String description;

    @Column(name = "requires_approval", length = 1)
    private String requiresApproval;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LeaveType() {}

    // Getters and Setters

    public Long getLeaveTypeId() { return leaveTypeId; }

    public void setLeaveTypeId(Long leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public String getLeaveCode() { return leaveCode; }

    public void setLeaveCode(String leaveCode) { this.leaveCode = leaveCode; }

    public String getLeaveName() { return leaveName; }

    public void setLeaveName(String leaveName) { this.leaveName = leaveName; }

    public Integer getDefaultDays() { return defaultDays; }

    public void setDefaultDays(Integer defaultDays) { this.defaultDays = defaultDays; }

    public Integer getMaxCarryForward() { return maxCarryForward; }

    public void setMaxCarryForward(Integer maxCarryForward) { this.maxCarryForward = maxCarryForward; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getRequiresApproval() { return requiresApproval; }

    public void setRequiresApproval(String requiresApproval) { this.requiresApproval = requiresApproval; }

    public String getIsActive() { return isActive; }

    public void setIsActive(String isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "LeaveType{" +
                "leaveTypeId=" + leaveTypeId +
                ", leaveCode='" + leaveCode + '\'' +
                ", leaveName='" + leaveName + '\'' +
                '}';
    }
}
