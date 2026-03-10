package com.revature.revworkforce.enums;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumTests {

    @Test
    void testAnnouncementPriority() {
        assertThat(AnnouncementPriority.fromString("LOW")).isEqualTo(AnnouncementPriority.LOW);
        assertThat(AnnouncementPriority.fromString(" normal ")).isEqualTo(AnnouncementPriority.NORMAL);
        assertThat(AnnouncementPriority.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> AnnouncementPriority.fromString("INVALID"));
    }

    @Test
    void testAnnouncementType() {
        assertThat(AnnouncementType.fromString("GENERAL")).isEqualTo(AnnouncementType.GENERAL);
        assertThat(AnnouncementType.fromString(" policy ")).isEqualTo(AnnouncementType.POLICY);
        assertThat(AnnouncementType.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> AnnouncementType.fromString("INVALID"));
    }

    @Test
    void testAuditAction() {
        assertThat(AuditAction.fromString("INSERT")).isEqualTo(AuditAction.INSERT);
        assertThat(AuditAction.fromString(" update ")).isEqualTo(AuditAction.UPDATE);
        assertThat(AuditAction.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> AuditAction.fromString("INVALID"));
    }

    @Test
    void testEmployeeStatus() {
        assertThat(EmployeeStatus.fromString("ACTIVE")).isEqualTo(EmployeeStatus.ACTIVE);
        assertThat(EmployeeStatus.fromString("ACTIVE")).isEqualTo(EmployeeStatus.ACTIVE);
        assertThat(EmployeeStatus.fromString("On Leave")).isEqualTo(EmployeeStatus.ON_LEAVE);
        assertThat(EmployeeStatus.fromString(null)).isNull();
        assertThat(EmployeeStatus.fromString("")).isNull();
        assertThat(EmployeeStatus.fromString("INVALID")).isNull();

        EmployeeStatus active = EmployeeStatus.ACTIVE;
        assertThat(active.getDisplayName()).isEqualTo("Active");
        assertThat(active.getDescription()).isNotEmpty();
        assertThat(active.canLogin()).isTrue();
        assertThat(active.canApplyLeave()).isTrue();
        assertThat(active.toString()).isEqualTo("Active");

        assertThat(EmployeeStatus.INACTIVE.canLogin()).isFalse();
        assertThat(EmployeeStatus.TERMINATED.canApplyLeave()).isFalse();
    }

    @Test
    void testGender() {
        assertThat(Gender.fromString("M")).isEqualTo(Gender.M);
        assertThat(Gender.fromString(" f ")).isEqualTo(Gender.F);
        assertThat(Gender.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> Gender.fromString("INVALID"));
    }

    @Test
    void testGoalStatus() {
        assertThat(GoalStatus.fromString("NOT_STARTED")).isEqualTo(GoalStatus.NOT_STARTED);
        assertThat(GoalStatus.fromString(" completed ")).isEqualTo(GoalStatus.COMPLETED);
        assertThat(GoalStatus.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> GoalStatus.fromString("INVALID"));
    }

    @Test
    void testHolidayType() {
        assertThat(HolidayType.fromString("NATIONAL")).isEqualTo(HolidayType.NATIONAL);
        assertThat(HolidayType.fromString(" regional ")).isEqualTo(HolidayType.REGIONAL);
        assertThat(HolidayType.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> HolidayType.fromString("INVALID"));
    }

    @Test
    void testLeaveStatus() {
        assertThat(LeaveStatus.fromString("PENDING")).isEqualTo(LeaveStatus.PENDING);
        assertThat(LeaveStatus.fromString(" approved ")).isEqualTo(LeaveStatus.APPROVED);
        assertThat(LeaveStatus.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> LeaveStatus.fromString("INVALID"));
    }

    @Test
    void testNotificationPriority() {
        assertThat(NotificationPriority.fromString("LOW")).isEqualTo(NotificationPriority.LOW);
        assertThat(NotificationPriority.fromString(" high ")).isEqualTo(NotificationPriority.HIGH);
        assertThat(NotificationPriority.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> NotificationPriority.fromString("INVALID"));
    }

    @Test
    void testNotificationType() {
        assertThat(NotificationType.fromString("SYSTEM")).isEqualTo(NotificationType.SYSTEM);
        assertThat(NotificationType.fromString(" goal ")).isEqualTo(NotificationType.GOAL);
        assertThat(NotificationType.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> NotificationType.fromString("INVALID"));
    }

    @Test
    void testPriority() {
        assertThat(Priority.fromString("LOW")).isEqualTo(Priority.LOW);
        assertThat(Priority.fromString("HIGH")).isEqualTo(Priority.HIGH);
        assertThat(Priority.fromString("Urgent")).isEqualTo(Priority.URGENT);
        assertThat(Priority.fromString(null)).isNull();
        assertThat(Priority.fromString("")).isNull();
        assertThat(Priority.fromString("INVALID")).isNull();

        Priority high = Priority.HIGH;
        assertThat(high.getDisplayName()).isEqualTo("High");
        assertThat(high.getDescription()).isNotEmpty();
        assertThat(high.getBootstrapClass()).isEqualTo("danger");
        assertThat(high.getWeight()).isEqualTo(4);
        assertThat(Priority.getDefaultPriority()).isEqualTo(Priority.MEDIUM);
    }

    @Test
    void testReviewStatus() {
        assertThat(ReviewStatus.fromString("PENDING_SELF_ASSESSMENT")).isEqualTo(ReviewStatus.PENDING_SELF_ASSESSMENT);
        assertThat(ReviewStatus.fromString(" completed ")).isEqualTo(ReviewStatus.COMPLETED);
        assertThat(ReviewStatus.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> ReviewStatus.fromString("INVALID"));
    }

    @Test
    void testTargetAudience() {
        assertThat(TargetAudience.fromString("ALL")).isEqualTo(TargetAudience.ALL);
        assertThat(TargetAudience.fromString(" department ")).isEqualTo(TargetAudience.DEPARTMENT);
        assertThat(TargetAudience.fromString(null)).isNull();
        assertThrows(IllegalArgumentException.class, () -> TargetAudience.fromString("INVALID"));
    }
}
