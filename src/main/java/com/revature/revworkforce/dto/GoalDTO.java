package com.revature.revworkforce.dto;

import com.revature.revworkforce.enums.Priority;
import com.revature.revworkforce.enums.GoalStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* DTO for Goal - P2 VERSION
* Works with existing Goal entity
* 
* @author RevWorkForce Team
*/
public class GoalDTO {

    private Long goalId;

    private String employeeId;

    private String employeeName;

    private String departmentName;

    private String designationName;

    @NotBlank(message = "Goal title is required")
    @Size(max = 200, message = "Goal title cannot exceed 200 characters")
    private String goalTitle;

    @NotBlank(message = "Goal description is required")
    @Size(max = 1000, message = "Goal description cannot exceed 1000 characters")
    private String goalDescription;

    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progress;

    private GoalStatus status;

    @Size(max = 500, message = "Manager comments cannot exceed 500 characters")
    private String managerComments;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Manager Info
    private String managerId;

    private String managerName;

    // Constructors
    public GoalDTO() {
        this.progress = 0;
        this.status = GoalStatus.NOT_STARTED;
    }

    // Getters and Setters
    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public String getManagerComments() {
        return managerComments;
    }

    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * Check if goal is editable by employee.
     * 
     * @return true if NOT_STARTED or IN_PROGRESS
     */
    public boolean isEditable() {
        return status == GoalStatus.NOT_STARTED || status == GoalStatus.IN_PROGRESS;
    }

    /**
     * Check if goal is overdue.
     * 
     * @return true if deadline passed and not completed
     */
    public boolean isOverdue() {
        if (deadline == null) {
            return false;
        }
        return deadline.isBefore(LocalDate.now()) && 
               status != GoalStatus.COMPLETED && 
               status != GoalStatus.CANCELLED;
    }

    /**
     * Get progress label.
     * 
     * @return progress label
     */
    public String getProgressLabel() {
        if (progress == null || progress == 0) {
            return "Not Started";
        } else if (progress == 100) {
            return "Completed";
        } else if (progress < 25) {
            return "Just Started";
        } else if (progress < 50) {
            return "In Progress";
        } else if (progress < 75) {
            return "More Than Half";
        } else {
            return "Almost Done";
        }
    }

    /**
     * Get priority color for UI.
     * 
     * @return Bootstrap color class
     */
    public String getPriorityColor() {
        if (priority == null) {
            return "secondary";
        }

        switch (priority) {
            case URGENT:
                return "danger";
            case HIGH:
                return "warning";
            case MEDIUM:
                return "info";
            case LOW:
                return "success";
            default:
                return "secondary";
        }
    }

    /**
     * Get status color for UI.
     * 
     * @return Bootstrap color class
     */
    public String getStatusColor() {
        if (status == null) {
            return "secondary";
        }

        switch (status) {
            case COMPLETED:
                return "success";
            case IN_PROGRESS:
                return "primary";
            case NOT_STARTED:
                return "secondary";
            case CANCELLED:
                return "danger";
            case DEFERRED:
                return "warning";
            default:
                return "secondary";
        }
    }
}
 