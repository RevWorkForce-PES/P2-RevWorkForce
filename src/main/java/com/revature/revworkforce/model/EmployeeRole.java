package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing role assignment of an employee.
 *
 * Database Table: EMPLOYEE_ROLES
 *
 * Relationships:
 * - Many-to-One with Employee
 * - Many-to-One with Role
 *
 * Oracle Sequence Used: emp_role_seq
 */
@Getter
@Setter
@ToString(exclude = {"employee", "role"})
@Entity
@Table(name = "EMPLOYEE_ROLES")
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_role_seq")
    @SequenceGenerator(name = "emp_role_seq", sequenceName = "emp_role_seq", allocationSize = 1)
    @Column(name = "employee_role_id")
    private Long employeeRoleId;

    /**
     * Employee assigned to role.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Assigned role.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * When role was assigned.
     */
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    /**
     * Who assigned this role.
     */
    @Column(name = "assigned_by", length = 20)
    private String assignedBy;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}