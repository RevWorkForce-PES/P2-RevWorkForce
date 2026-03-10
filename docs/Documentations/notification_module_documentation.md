RevWorkForce Notification Module – Detailed Documentation
==============================================================

1. Notification Module Overview
--------------------------------------------------------------

The Notification Module in the RevWorkForce system is responsible
for informing employees about important events that occur within
different modules of the application.

In large enterprise applications, users should not constantly
navigate through different pages to check updates. Instead,
notifications provide a centralized mechanism that informs
employees immediately when something important happens.

Examples of events that trigger notifications include:

• Leave request approval or rejection
• Performance review submission
• Goal updates or comments
• Company-wide announcements
• System alerts

Each notification is stored in the database and belongs to a
specific employee. This allows employees to review both recent
notifications and past notifications whenever needed.

The module includes several features designed to improve the
user experience:

• Notification bell icon in dashboard
• Unread notification badge counter
• Dropdown showing recent notifications
• Full notification history page
• Mark-as-read functionality
• Delete notification option
• Automatic cleanup of expired notifications

Notifications are categorized using a notification type so that
the system knows which module generated the notification.

Supported Notification Types:

LEAVE
PERFORMANCE
GOAL
ANNOUNCEMENT
SYSTEM

These categories help the system display contextual information
and apply different UI styles if needed.

--------------------------------------------------------------

2. Notification Module Project Structure
--------------------------------------------------------------

The Notification Module follows a layered architecture similar
to most Spring Boot enterprise applications. Each layer has a
specific responsibility.

The structure of the notification module is shown below.

src/main/java/com/revature/revworkforce

controller/
    NotificationViewController.java
    NotificationApiController.java

service/
    NotificationService.java

service/impl/
    NotificationServiceImpl.java

repository/
    NotificationRepository.java

model/
    Notification.java

dto/
    NotificationDTO.java

scheduler/
    NotificationCleanupScheduler.java


src/main/resources

templates/notifications/
    list.html

templates/fragments/
    notification-bell.html

static/js/
    notification.js

Each folder has a specific role in the architecture.

controller → handles HTTP requests  
service → business logic  
repository → database access  
model → entity mapping  
dto → data transfer objects  
scheduler → background jobs  
templates → UI views  
static/js → frontend scripts

This structure ensures separation of concerns and improves
maintainability of the codebase.

--------------------------------------------------------------

3. Files Created in the Notification Module
--------------------------------------------------------------

Below are the files created to implement the notification module.

Notification.java  
Defines the Notification entity and maps it to the database table.

NotificationDTO.java  
Transfers notification data between backend layers and frontend.

NotificationRepository.java  
Handles database queries related to notifications.

NotificationService.java  
Defines notification operations such as create, read, delete.

NotificationServiceImpl.java  
Contains business logic for notifications.

NotificationViewController.java  
Handles page-based requests such as displaying the notification page.

NotificationApiController.java  
Provides REST APIs used by the frontend.

NotificationCleanupScheduler.java  
Automatically deletes expired notifications.

notification-bell.html  
Fragment containing notification bell UI.

notifications/list.html  
Page showing all notifications.

notification.js  
Handles AJAX calls for fetching notifications.

--------------------------------------------------------------

4. Notification Generation Flow
--------------------------------------------------------------

Notifications are created automatically when system events occur.

Example Flow (Leave Approval):

Step 1  
A manager approves a leave request.

Step 2  
LeaveService calls NotificationService.

Step 3  
NotificationService creates a Notification object.

Step 4  
The notification is saved using NotificationRepository.

Step 5  
The notification is marked unread.

Step 6  
The employee sees the notification in the notification tray.

This automated process ensures that employees receive real-time
updates regarding important actions.

--------------------------------------------------------------

5. Notification Entity Explanation
--------------------------------------------------------------

The Notification entity represents the database table used
to store notifications.

Important Fields:

notificationId  
Primary key for the notification.

employee  
Reference to the employee who owns the notification.

title  
Short description of the notification.

message  
Detailed message explaining the event.

notificationType  
Indicates which module triggered the notification.

referenceType  
Optional field referencing another entity.

referenceId  
Stores ID of the related entity.

isRead  
Tracks whether the notification has been read.

createdAt  
Timestamp of notification creation.

expiresAt  
Optional expiration timestamp.

Each notification belongs to exactly one employee.

--------------------------------------------------------------

6. Notification DTO Explanation
--------------------------------------------------------------

DTO stands for Data Transfer Object.

NotificationDTO is used to transfer notification data
between backend layers and the frontend UI.

The DTO contains only necessary fields instead of the
complete entity structure.

This improves security and avoids exposing internal
database structures to the UI.

--------------------------------------------------------------

7. Notification Repository Explanation
--------------------------------------------------------------

The NotificationRepository handles all database interactions.

It uses Spring Data JPA to simplify database queries.

Important operations include:

• Fetch notifications for an employee
• Fetch unread notifications
• Count unread notifications
• Fetch recent notifications
• Delete expired notifications

This layer ensures that database operations remain
separate from business logic.

--------------------------------------------------------------

8. Notification Service Layer Explanation
--------------------------------------------------------------

The service layer contains the core business logic of the
notification module.

Responsibilities include:

• Creating notifications
• Fetching notifications
• Marking notifications as read
• Deleting notifications
• Converting entities into DTOs
• Managing scheduled cleanup

The service layer is also responsible for ensuring
business rules such as ownership validation.

--------------------------------------------------------------

9. Notification Controller Layer Explanation
--------------------------------------------------------------

The controller layer exposes endpoints that allow the UI
to interact with the notification system.

NotificationViewController  
Handles page-based operations such as displaying
the full notification list.

NotificationApiController  
Provides API endpoints used by JavaScript to
fetch notification data dynamically.

This separation ensures a clean design between
UI rendering and API operations.

--------------------------------------------------------------

10. UI Integration
--------------------------------------------------------------

The notification UI is integrated into the dashboard
using a bell icon.

The bell icon displays a badge that shows the number
of unread notifications.

When the user clicks the bell icon, a dropdown appears
showing recent notifications.

Users can click "View All" to open the complete
notifications page.

--------------------------------------------------------------

11. JavaScript Integration
--------------------------------------------------------------

notification.js handles dynamic communication
between frontend and backend.

Instead of refreshing the page to fetch new data,
AJAX calls are used.

Key functions include:

loadUnreadCount()

Fetches unread notification count.

loadRecentNotifications()

Fetches recent notifications.

markAsRead()

Marks notifications as read without refreshing the page.

--------------------------------------------------------------

12. Scheduler Explanation
--------------------------------------------------------------

To prevent the notifications table from growing indefinitely,
a scheduler periodically deletes expired notifications.

The cleanup job runs daily at 2 AM.

It deletes notifications older than 90 days.

This helps maintain database performance.

--------------------------------------------------------------

13. Security Rules
--------------------------------------------------------------

Security ensures that employees can only access
their own notifications.

Before performing operations such as reading or
deleting notifications, the system verifies
ownership.

If a user attempts to access another employee's
notification, an UnauthorizedException is thrown.

--------------------------------------------------------------

14. Notification Integration Points
--------------------------------------------------------------

The Notification Module integrates with several
other modules.

Leave Module

Triggers notifications when leave is approved
or rejected.

Goal Module

Triggers notifications when goals are updated.

Performance Module

Triggers notifications when performance reviews
are submitted.

Announcement Module

Broadcasts notifications to all employees.

--------------------------------------------------------------

15. Database Schema
--------------------------------------------------------------

Table Name:
NOTIFICATIONS

Columns include:

NOTIFICATION_ID  
EMPLOYEE_ID  
TITLE  
MESSAGE  
NOTIFICATION_TYPE  
REFERENCE_TYPE  
REFERENCE_ID  
IS_READ  
CREATED_AT  
EXPIRES_AT  

Each notification belongs to exactly one employee.

--------------------------------------------------------------

16. Error Handling
--------------------------------------------------------------

The system handles several edge cases.

Unauthorized Access

If a user attempts to access another employee’s
notification, an UnauthorizedException is thrown.

Invalid Notification ID

If a notification does not exist, a
ResourceNotFoundException is thrown.

Empty Notification List

If no notifications exist, the UI displays
"No notifications available".

--------------------------------------------------------------

17. Testing Documentation
--------------------------------------------------------------

Testing ensures the module behaves correctly.

Test cases include:

• Notification creation when events occur
• Unread notification badge updates
• Fetching recent notifications
• Marking notifications as read
• Deleting notifications
• Authorization checks

Testing can be performed using both browser UI
and Postman API testing.

--------------------------------------------------------------

18. Acceptance Criteria
--------------------------------------------------------------

The module is considered complete when:

• Notification entity is implemented
• Notification DTO is implemented
• Notification service is implemented
• Notification repository is implemented
• Notification controllers are implemented
• Scheduler is implemented
• Security rules are enforced
• UI is integrated into dashboards
• Notification APIs are functioning
• Module has been tested successfully

--------------------------------------------------------------

19. Final Status
--------------------------------------------------------------

The Notification Module is fully implemented
and integrated into the RevWorkForce system.

Backend logic, UI integration, scheduler,
security rules, and testing are completed.

The module is ready for production deployment.


==============================================================
