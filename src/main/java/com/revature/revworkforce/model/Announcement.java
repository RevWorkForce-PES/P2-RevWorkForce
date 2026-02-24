package com.revature.revworkforce.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANNOUNCEMENTS")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announce_seq_gen")
    @SequenceGenerator(name = "announce_seq_gen", sequenceName = "announce_seq", allocationSize = 1)
    @Column(name = "announcement_id")
    private Long announcementId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "announcement_type", length = 50)
    private String announcementType;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "target_audience", length = 50)
    private String targetAudience;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}