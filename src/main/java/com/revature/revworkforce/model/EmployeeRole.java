package com.revature.revworkforce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class representing the Employee-Role relationship (Junction Table).
 * 
 * Maps to database table: EMPLOYEE_ROLES
 * This is a Many-to-Many relationship between Employee and Role.
 * 
 * @author RevWorkForce Team
 */
@Entity
@Table(name = "EMPLOYEE_ROLES")
@IdClass(EmployeeRole.EmployeeRoleId.class)
public class EmployeeRole {

    @Id
    @Column(name = "employee_id", length = 20)
    private String employeeId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by", length = 20)
    private String assignedBy;

    // Constructors
    public EmployeeRole() {
        this.assignedAt = LocalDateTime.now();
    }

    public EmployeeRole(String employeeId, Long roleId) {
        this();
        this.employeeId = employeeId;
        this.roleId = roleId;
    }

    public EmployeeRole(String employeeId, Long roleId, String assignedBy) {
        this();
        this.employeeId = employeeId;
        this.roleId = roleId;
        this.assignedBy = assignedBy;
    }

    public EmployeeRole(Employee employee, Role role) {
        this();
        this.employee = employee;
        this.role = role;
        this.employeeId = employee.getEmployeeId();
        this.roleId = role.getRoleId();
    }

    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (employee != null) {
            this.employeeId = employee.getEmployeeId();
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            this.roleId = role.getRoleId();
        }
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeRole that = (EmployeeRole) o;
        return Objects.equals(employeeId, that.employeeId) &&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, roleId);
    }

    @Override
    public String toString() {
        return "EmployeeRole{" +
                "employeeId='" + employeeId + '\'' +
                ", roleId=" + roleId +
                ", assignedAt=" + assignedAt +
                ", assignedBy='" + assignedBy + '\'' +
                '}';
    }

    /**
     * Composite Primary Key class for EmployeeRole.
     * Required for @IdClass annotation.
     */
    public static class EmployeeRoleId implements Serializable {
        
        private String employeeId;
        private Long roleId;

        // Default constructor
        public EmployeeRoleId() {
        }

        public EmployeeRoleId(String employeeId, Long roleId) {
            this.employeeId = employeeId;
            this.roleId = roleId;
        }

        // Getters and Setters
        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmployeeRoleId that = (EmployeeRoleId) o;
            return Objects.equals(employeeId, that.employeeId) &&
                   Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(employeeId, roleId);
        }
    }
}