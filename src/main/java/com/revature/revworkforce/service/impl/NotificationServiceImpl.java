package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.dto.NotificationDTO;
import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.exception.ResourceNotFoundException;
import com.revature.revworkforce.exception.UnauthorizedException;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Notification;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.NotificationRepository;
import com.revature.revworkforce.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;
    @Value("${app.notification.expiry-days}")
    private int expiryDays;

@Value("${app.notification.max-unread}")
private int maxUnread;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
            EmployeeRepository employeeRepository) {
        this.notificationRepository = notificationRepository;
        this.employeeRepository = employeeRepository;
    }
   
    @Override
    public void createAnnouncementForAll(Announcement announcement) {
        List<Employee> activeEmployees = employeeRepository.findByStatusOrderByFirstNameAsc(EmployeeStatus.ACTIVE);

        List<Notification> notifications = activeEmployees.stream().map(employee -> {
            Notification notification = new Notification(
                    employee,
                    NotificationType.ANNOUNCEMENT,
                    "New Announcement: " + announcement.getTitle(),
                    announcement.getMessage() != null && announcement.getMessage().length() > 200
                            ? announcement.getMessage().substring(0, 197) + "..."
                            : announcement.getMessage(),
                    NotificationPriority.NORMAL);
            notification.setReferenceType("ANNOUNCEMENT");
            notification.setReferenceId(announcement.getAnnouncementId());
            return notification;
        }).collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
    }

    @Override
    public void createNotification(Employee employee,
                                   NotificationType type,
                                   String title,
                                   String message,
                                   NotificationPriority priority) {

        // 1️⃣ Check unread count
        long unreadCount = notificationRepository
                .countByEmployeeAndIsRead(employee, 'N');

        // 2️⃣ If limit reached → delete oldest unread
        if (unreadCount >= maxUnread) {

            List<Notification> unreadList =
                    notificationRepository
                            .findByEmployeeAndIsReadOrderByCreatedAtDesc(employee, 'N');

            if (!unreadList.isEmpty()) {
                Notification oldest =
                        unreadList.get(unreadList.size() - 1);

                notificationRepository.delete(oldest);
            }
        }

        // 3️⃣ Create new notification
        Notification notification =
                new Notification(employee, type, title, message, priority);

        // 4️⃣ Set expiry dynamically
        notification.setExpiresAt(
                java.time.LocalDateTime.now().plusDays(expiryDays)
        );

        // 5️⃣ Save
        notificationRepository.save(notification);
    }
    
    private Employee getEmployeeOrThrow(String employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee", "employeeId", employeeId));
    }
    private NotificationDTO mapToDTO(Notification notification) {

        NotificationDTO dto = new NotificationDTO();

        dto.setNotificationId(notification.getNotificationId());
        dto.setEmployeeId(notification.getEmployee().getEmployeeId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getNotificationType());
        dto.setReferenceType(notification.getReferenceType());
        dto.setReferenceId(notification.getReferenceId());
        dto.setIsRead(notification.getIsRead());
        dto.setReadAt(notification.getReadAt());
        dto.setPriority(notification.getPriority());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setExpiresAt(notification.getExpiresAt());

        return dto;
    }
    @Override
    public List<NotificationDTO> getEmployeeNotifications(String employeeId) {

        Employee employee = getEmployeeOrThrow(employeeId);

        return notificationRepository
                .findByEmployeeOrderByCreatedAtDesc(employee)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<NotificationDTO> getUnreadNotifications(String employeeId) {

        Employee employee = getEmployeeOrThrow(employeeId);

        return notificationRepository
                .findByEmployeeAndIsReadOrderByCreatedAtDesc(employee, 'N')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public long getUnreadCount(String employeeId) {

        Employee employee = getEmployeeOrThrow(employeeId);

        return notificationRepository
                .countByEmployeeAndIsRead(employee, 'N');
    }
    @Override
    public void markAsRead(Long notificationId, String employeeId) {

        Notification notification = notificationRepository.findById(notificationId)
        		.orElseThrow(() ->
                new ResourceNotFoundException("Notification", "notificationId", notificationId));
        if (!notification.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException();
        }

        notification.setIsRead('Y');
        notification.setReadAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
    @Override
    public void markAllAsRead(String employeeId) {

        Employee employee = getEmployeeOrThrow(employeeId);

        notificationRepository.markAllAsRead(employee, LocalDateTime.now());
    }
    @Override
    public void deleteNotification(Long notificationId, String employeeId) {

        Notification notification = notificationRepository.findById(notificationId)
        		.orElseThrow(() ->
                new ResourceNotFoundException("Notification", "notificationId", notificationId));
        if (!notification.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedException();
        }

        notificationRepository.delete(notification);
    }
    @Override
    public void deleteExpiredNotifications() {
        notificationRepository.deleteExpiredNotifications(LocalDateTime.now());
    }
    @Override
    public List<NotificationDTO> getRecentNotifications(String employeeId, int limit) {

        Employee employee = getEmployeeOrThrow(employeeId);

        List<Notification> notifications =
                notificationRepository.findRecentByEmployee(
                        employee,
                        PageRequest.of(0, limit)
                );

        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    
    @Scheduled(cron = "0 0 2 * * ?") // Every day at 2 AM
    @Transactional
    public void scheduledCleanup() {
        notificationRepository.deleteExpiredNotifications(LocalDateTime.now());
    }
    
}
