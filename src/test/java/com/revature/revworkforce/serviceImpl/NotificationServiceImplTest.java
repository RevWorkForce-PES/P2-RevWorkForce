package com.revature.revworkforce.serviceImpl;

import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @BeforeEach
    void setup() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");

        notification = new Notification();
        notification.setNotificationId(1L);
        notification.setEmployee(employee);
        notification.setTitle("Test");
        notification.setMessage("Test message");
        notification.setNotificationType(NotificationType.SYSTEM);
        notification.setPriority(NotificationPriority.NORMAL);
        notification.setIsRead('N');
        notification.setCreatedAt(LocalDateTime.now());
    }

    // =========================================================
    // GET EMPLOYEE NOTIFICATIONS
    // =========================================================

    @Test
    void getEmployeeNotifications_ReturnsList() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee))
                .thenReturn(List.of(notification));

        var result = notificationService.getEmployeeNotifications("EMP001");

        assertThat(result).hasSize(1);
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    @Test
    void getUnreadCount_ReturnsCount() {

        when(employeeRepository.findById("EMP001"))
                .thenReturn(Optional.of(employee));

        when(notificationRepository.countByEmployeeAndIsRead(employee, 'N'))
                .thenReturn(3L);

        long count = notificationService.getUnreadCount("EMP001");

        assertThat(count).isEqualTo(3);
    }

    // =========================================================
    // MARK AS READ SUCCESS
    // =========================================================

    @Test
    void markAsRead_Success() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L, "EMP001");

        verify(notificationRepository).save(notification);
        assertThat(notification.getIsRead()).isEqualTo('Y');
    }

    // =========================================================
    // MARK AS READ NOT FOUND
    // =========================================================

    @Test
    void markAsRead_NotFound_ThrowsException() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                notificationService.markAsRead(1L, "EMP001"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // =========================================================
    // MARK AS READ UNAUTHORIZED
    // =========================================================

    @Test
    void markAsRead_Unauthorized_ThrowsException() {

        Employee other = new Employee();
        other.setEmployeeId("EMP999");

        notification.setEmployee(other);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        assertThatThrownBy(() ->
                notificationService.markAsRead(1L, "EMP001"))
                .isInstanceOf(UnauthorizedException.class);
    }

    // =========================================================
    // DELETE NOTIFICATION
    // =========================================================

    @Test
    void deleteNotification_Success() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        notificationService.deleteNotification(1L, "EMP001");

        verify(notificationRepository).delete(notification);
    }

    // =========================================================
    // DELETE EXPIRED NOTIFICATIONS
    // =========================================================

    @Test
    void deleteExpiredNotifications_CallsRepository() {

        notificationService.deleteExpiredNotifications();

        verify(notificationRepository)
                .deleteExpiredNotifications(any(LocalDateTime.class));
    }
}