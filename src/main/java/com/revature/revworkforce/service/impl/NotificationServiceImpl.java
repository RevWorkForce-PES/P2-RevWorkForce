package com.revature.revworkforce.service.impl;

import com.revature.revworkforce.enums.EmployeeStatus;
import com.revature.revworkforce.enums.NotificationPriority;
import com.revature.revworkforce.enums.NotificationType;
import com.revature.revworkforce.model.Announcement;
import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.model.Notification;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.repository.NotificationRepository;
import com.revature.revworkforce.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;

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
    public void createNotification(Employee employee, NotificationType type, String title, String message,
            NotificationPriority priority) {
        Notification notification = new Notification(employee, type, title, message, priority);
        notificationRepository.save(notification);
    }
}
