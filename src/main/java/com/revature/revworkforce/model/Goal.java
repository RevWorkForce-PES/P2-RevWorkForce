package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.GoalPriority;
import com.revature.revworkforce.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a goal assigned to an employee.
 *
 * Database Table: GOALS
 *
 * This entity is used to track employee goals such as:
 * - Technical objectives
 * - Professional development
 * - Business targets
 *
 * Relationships:
 * - Many-to-One with Employee
 *
 * Status Lifecycle:
 * NOT_STARTED → IN_PROGRESS → COMPLETED
 * Goals can also be CANCELLED or DEFERRED.
 *
 * Business Rules:
 * - Progress must be between 0 and 100.
 * - When progress reaches 100, goal can be marked COMPLETED.
 *
 * Oracle Sequence Used: goal_seq
 *
 * @author RevWorkForce Team
 */
@Getter
@Setter
@ToString(exclude = {"employee"})
@Entity
@Table(name = "GOALS")
public class Goal {

    /**
     * Primary key for goal.
     * Auto-generated using Oracle sequence goal_seq.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goal_seq")
    @SequenceGenerator(name = "goal_seq", sequenceName = "goal_seq", allocationSize = 1)
    @Column(name = "goal_id")
    private Long goalId;

    /**
     * Employee to whom this goal belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Short title of the goal.
     */
    @Column(name = "goal_title", nullable = false, length = 200)
    private String goalTitle;

    /**
     * Detailed description of the goal.
     */
    @Column(name = "goal_description", nullable = false, length = 1000)
    private String goalDescription;

    /**
     * Goal category (Technical, Business, Personal, etc.).
     */
    @Column(name = "category", length = 50)
    private String category;

    /**
     * Target completion deadline.
     */
    @Column(name = "deadline")
    private LocalDate deadline;

    /**
     * Priority level of the goal.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private GoalPriority priority;

    /**
     * Percentage completion of goal (0–100).
     */
    @Column(name = "progress")
    private Integer progress = 0;

    /**
     * Current lifecycle status of the goal.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private GoalStatus status = GoalStatus.NOT_STARTED;

    /**
     * Manager feedback or comments.
     */
    @Column(name = "manager_comments", length = 500)
    private String managerComments;

    /**
     * Timestamp when goal was completed.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Record creation timestamp.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA.
     */
    public Goal() {
    }

    /**
     * Constructor for creating a new goal (without ID).
     */
    public Goal(Employee employee,
                String goalTitle,
                String goalDescription,
                String category,
                LocalDate deadline,
                GoalPriority priority) {

        this.employee = employee;
        this.goalTitle = goalTitle;
        this.goalDescription = goalDescription;
        this.category = category;
        this.deadline = deadline;
        this.priority = priority;
        this.progress = 0;
        this.status = GoalStatus.NOT_STARTED;
    }

    /**
     * Full constructor.
     */
    public Goal(Long goalId,
                Employee employee,
                String goalTitle,
                String goalDescription,
                String category,
                LocalDate deadline,
                GoalPriority priority,
                Integer progress,
                GoalStatus status,
                String managerComments,
                LocalDateTime completedAt,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {

        this.goalId = goalId;
        this.employee = employee;
        this.goalTitle = goalTitle;
        this.goalDescription = goalDescription;
        this.category = category;
        this.deadline = deadline;
        this.priority = priority;
        this.progress = progress;
        this.status = status;
        this.managerComments = managerComments;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Automatically sets timestamps before insert.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates modified timestamp before update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}