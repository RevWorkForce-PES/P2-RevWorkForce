//package com.revature.revworkforce.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import com.revature.revworkforce.enums.NotificationPriority;
//import com.revature.revworkforce.enums.NotificationType;
//import com.revature.revworkforce.model.Employee;
//import com.revature.revworkforce.model.Notification;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.data.domain.PageRequest;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//class NotificationRepositoryTest {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    // =========================================================
//    // FIND BY EMPLOYEE
//    // =========================================================
//    @Test
//    void findByEmployeeOrderByCreatedAtDesc_ReturnsList() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId("EMP001");
//
//        Notification notification = new Notification();
//        notification.setEmployee(employee);
//        notification.setMessage("Test notification");
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setIsRead('N');
//
//        notificationRepository.save(notification);
//
//        List<Notification> result =
//                notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee);
//
//        assertThat(result).isNotEmpty();
//    }
//
//    // =========================================================
//    // FIND UNREAD NOTIFICATIONS
//    // =========================================================
//    @Test
//    void findByEmployeeAndIsReadOrderByCreatedAtDesc_ReturnsUnread() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId("EMP001");
//
//        Notification notification = new Notification();
//        notification.setEmployee(employee);
//        notification.setMessage("Unread notification");
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setIsRead('N');
//
//        notificationRepository.save(notification);
//
//        List<Notification> result =
//                notificationRepository.findByEmployeeAndIsReadOrderByCreatedAtDesc(employee, 'N');
//
//        assertThat(result).isNotEmpty();
//    }
//
//    // =========================================================
//    // COUNT UNREAD
//    // =========================================================
//    @Test
//    void countByEmployeeAndIsRead_ReturnsCount() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId("EMP002");
//
//        Notification notification = new Notification();
//        notification.setEmployee(employee);
//        notification.setMessage("Unread message");
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setIsRead('N');
//
//        notificationRepository.save(notification);
//
//        long count =
//                notificationRepository.countByEmployeeAndIsRead(employee, 'N');
//
//        assertThat(count).isEqualTo(1);
//    }
//
//    // =========================================================
//    // FIND RECENT NOTIFICATIONS
//    // =========================================================
//    @Test
//    void findRecentByEmployee_ReturnsLimitedResults() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId("EMP003");
//
//        Notification notification = new Notification();
//        notification.setEmployee(employee);
//        notification.setMessage("Recent notification");
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setIsRead('N');
//
//        notificationRepository.save(notification);
//
//        List<Notification> result =
//                notificationRepository.findRecentByEmployee(
//                        employee,
//                        PageRequest.of(0,5));
//
//        assertThat(result).isNotEmpty();
//    }
//
//    // =========================================================
//    // FIND BY TYPE
//    // =========================================================
//    @Test
//    void findByEmployeeAndNotificationType_ReturnsList() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId("EMP004");
//
//        Notification notification = new Notification();
//        notification.setEmployee(employee);
//        notification.setNotificationType(NotificationType.SYSTEM);
//        notification.setMessage("System notification");
//        notification.setCreatedAt(LocalDateTime.now());
//        notification.setIsRead('N');
//
//        notificationRepository.save(notification);
//
//        List<Notification> result =
//                notificationRepository.findByEmployeeAndNotificationType(
//                        employee,
//                        NotificationType.SYSTEM);
//
//        assertThat(result).isNotEmpty();
//    }
//}