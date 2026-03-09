package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Notification;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.NotificationRepository;
import com.revature.revworkforce.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Employee employee;
    private Notification notification;
    private Announcement announcement;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "expiryDays", 90);
        ReflectionTestUtils.setField(notificationService, "maxUnread", 50);

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");

        notification = new Notification();
        notification.setNotificationId(1L);
        notification.setEmployee(employee);
        notification.setTitle("Test Notification");
        notification.setIsRead('N');

        announcement = new Announcement();
        announcement.setAnnouncementId(1L);
        announcement.setTitle("Test Announcement");
        announcement.setMessage("Ann Message");
    }

    @Test
    void createAnnouncementForAll_Success() {
        when(employeeRepository.findByStatusOrderByFirstNameAsc(EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList(employee));

        notificationService.createAnnouncementForAll(announcement);

        verify(notificationRepository).saveAll(anyList());
    }

    @Test
    void createNotification_Success() {
        when(notificationRepository.countByEmployeeAndIsRead(employee, 'N')).thenReturn(10L);

        notificationService.createNotification(employee, NotificationType.GOAL, "Title", "Message",
                NotificationPriority.NORMAL);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_MaxUnreadReached() {
        when(notificationRepository.countByEmployeeAndIsRead(employee, 'N')).thenReturn(100L); // assume maxUnread is 50
        when(notificationRepository.findByEmployeeAndIsRead(eq(employee), eq('N'), any()))
                .thenReturn(Arrays.asList(notification));

        notificationService.createNotification(employee, NotificationType.GOAL, "Title", "Message",
                NotificationPriority.NORMAL);

        verify(notificationRepository).delete(notification);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void getEmployeeNotifications_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee))
                .thenReturn(Arrays.asList(notification));

        List<NotificationDTO> result = notificationService.getEmployeeNotifications("EMP001");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Notification");
    }

    @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L, "EMP001");

        assertThat(notification.getIsRead()).isEqualTo('Y');
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_Unauthorized() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        assertThrows(UnauthorizedException.class, () -> notificationService.markAsRead(1L, "EMP002"));
    }

    @Test
    void deleteNotification_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.deleteNotification(1L, "EMP001");

        verify(notificationRepository).delete(notification);
    }

    @Test
    void deleteExpiredNotifications_Success() {
        notificationService.deleteExpiredNotifications();
        verify(notificationRepository).deleteExpiredNotifications(any(LocalDateTime.class));
    }

    @Test
    void getUnreadNotifications_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));
        when(notificationRepository.findByEmployeeAndIsReadOrderByCreatedAtDesc(employee, 'N'))
                .thenReturn(Arrays.asList(notification));

        List<NotificationDTO> result = notificationService.getUnreadNotifications("EMP001");

        assertThat(result).hasSize(1);
    }

    @Test
    void markAllAsRead_Success() {
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(employee));

        notificationService.markAllAsRead("EMP001");

        verify(notificationRepository).markAllAsRead(eq(employee), any(LocalDateTime.class));
    }
}
