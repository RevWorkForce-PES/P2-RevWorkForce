package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing an in-app notification sent to an employee.
 *
 * Database Table: NOTIFICATIONS
 *
 * This entity is used to notify employees about:
 * - Leave approvals or rejections
 * - Performance review updates
 * - Goal progress reminders
 * - Announcements
 * - System alerts
 *
 * Relationships:
 * - Many-to-One with Employee (recipient)
 *
 * Business Rules:
 * - Notifications can be marked as read.
 * - Priority defines urgency level.
 * - Notifications may expire automatically.
 *
 * Oracle Sequence Used: notif_seq
 *
 * @author RevWorkForce Team
 */
@Getter
@Setter
@ToString(exclude = {"employee"})
@Entity
@Table(name = "NOTIFICATIONS",
indexes = {
    @Index(name = "idx_notif_employee", columnList = "employee_id"),
    @Index(name = "idx_notif_read", columnList = "is_read"),
    @Index(name = "idx_notif_created", columnList = "created_at")
})
public class Notification {

    /**
     * Primary key for notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notif_seq")
    @SequenceGenerator(name = "notif_seq", sequenceName = "notif_seq", allocationSize = 1)
    @Column(name = "notification_id")
    private Long notificationId;

    /**
     * Employee who receives this notification.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Type of notification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;

    /**
     * Short notification title.
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * Detailed notification message.
     */
    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    /**
     * Optional reference type (e.g., LEAVE_APP, REVIEW).
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;

    /**
     * Optional reference ID of related record.
     */
    @Column(name = "reference_id")
    private Long referenceId;

    /**
     * Indicates whether notification has been read.
     * Default value: 'N'
     */
    @Column(name = "is_read", nullable = false, length = 1)
    private Character isRead = 'N';

    /**
     * Timestamp when notification was read.
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * Notification priority level.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    /**
     * Record creation timestamp.
     */
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * Expiry timestamp.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Default constructor required by JPA.
     * Initializes timestamps and defaults.
     */
    public Notification() {
//        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusDays(90);
        this.isRead = 'N';
        this.priority = NotificationPriority.NORMAL;
    }

    /**
     * Constructor used when creating a new notification.
     */
    public Notification(Employee employee,
                        NotificationType notificationType,
                        String title,
                        String message,
                        NotificationPriority priority) {

        this();
        this.employee = employee;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.priority = priority != null ? priority : NotificationPriority.NORMAL;
    }

    /**
     * Full constructor.
     */
    public Notification(Long notificationId,
                        Employee employee,
                        NotificationType notificationType,
                        String title,
                        String message,
                        String referenceType,
                        Long referenceId,
                        Character isRead,
                        LocalDateTime readAt,
                        NotificationPriority priority,
                        LocalDateTime createdAt,
                        LocalDateTime expiresAt) {

        this.notificationId = notificationId;
        this.employee = employee;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.isRead = isRead;
        this.readAt = readAt;
        this.priority = priority;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    /**
     * Automatically sets creation timestamp before insert.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.expiresAt == null) {
            this.expiresAt = this.createdAt.plusDays(90);
        }
        if (this.isRead == null) {
            this.isRead = 'N';
        }
        if (this.priority == null) {
            this.priority = NotificationPriority.NORMAL;
        }
    }
}