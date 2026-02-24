package com.revature.revworkforce.model;

import com.revature.revworkforce.enums.AnnouncementPriority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing company announcements.
 *
 * Database Table: ANNOUNCEMENTS
 *
 * Oracle Sequence Used: announcement_seq
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "ANNOUNCEMENTS")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_seq")
    @SequenceGenerator(name = "announcement_seq", sequenceName = "announcement_seq", allocationSize = 1)
    @Column(name = "announcement_id")
    private Long announcementId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "announcement_type", length = 50)
    private String announcementType = "General";

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private AnnouncementPriority priority = AnnouncementPriority.NORMAL;

    @Column(name = "target_audience", length = 50)
    private String targetAudience = "ALL";

    @Column(name = "is_active", length = 1)
    private Character isActive = 'Y';

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * Employee who created the announcement.
     */
    @Column(name = "created_by", nullable = false, length = 20)
    private String createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}