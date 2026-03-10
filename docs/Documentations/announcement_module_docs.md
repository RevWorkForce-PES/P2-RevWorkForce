# Announcements Module Documentation

## 1. Announcement Module Overview
The Announcements Module provides a centralized way for Admins to create, update, delete, and manage company-wide announcements. These announcements are broadcasted to all active employees and displayed on an announcements dashboard. The module supports three types of announcements: `INFO`, `WARNING`, and `URGENT`. All announcements have an automatic lifecycle (active, inactive) and an expiry time based on scheduled tasks.

## 2. Broadcast Notification Flow Documentation
When an Admin creates an announcement using `POST /admin/announcements/create`:
1. The `AnnouncementController` maps the DTO and invokes `createAnnouncement()` on the `AnnouncementService`.
2. The service saves the `Announcement` entity.
3. The service calls `NotificationService.createAnnouncementForAll(savedAnnouncement)`.
4. `NotificationService` retrieves all active employees via `EmployeeRepository.findByStatusOrderByFirstNameAsc(EmployeeStatus.ACTIVE)`.
5. It uses Java Streams to map each employee to a `Notification` entity of type `ANNOUNCEMENT`.
6. The list of notifications is bulk-saved using `notificationRepository.saveAll(notifications)`.
7. Employees will see these notifications in their personal notification tray.

## 3. Expiry Handling Documentation
Announcements are equipped with an `expiryDate` attribute. 
**Real-Time Query Filtering:**
Active announcements queries (`findActiveAnnouncements`) naturally exclude announcements where `expiryDate < CURRENT_DATE`.
**Scheduled Deactivation:**
The system runs a cron job daily (`@Scheduled(cron = "0 0 1 * * ?")`) which invokes `deactivateExpired()`. This executes an efficient bulk SQL update:
`UPDATE Announcement a SET a.isActive = 'N' WHERE a.isActive = 'Y' AND a.expiryDate < :date`

## 4. API Documentation

### Admin Endpoints
- **GET** `/admin/announcements`: List all announcements (Returns HTML View)
- **GET** `/admin/announcements/create`: Display internal form for creation
- **POST** `/admin/announcements/create`: Accepts form-data and creates an announcement. Triggers broadcasts. (Redirects)
- **GET** `/admin/announcements/edit/{id}`: Display form populated with `{id}` info
- **POST** `/admin/announcements/edit/{id}`: Submits changes to `{id}` announcement
- **POST** `/admin/announcements/deactivate/{id}`: Admin manually deactivates `{id}` announcement (Updates `isActive` to 'N')
- **POST** `/admin/announcements/delete/{id}`: Permanently deletes `{id}` announcement.

### Employee Endpoints
- **GET** `/announcements`: View all active company announcements (Returns HTML View)
- **GET** `/announcements/{id}`: View details for `{id}`

## 5. Scheduled Task Documentation
**Task Name:** `deactivateExpiredAnnouncements`
**Location:** `AnnouncementServiceImpl.java`
**Trigger Expression:** `0 0 1 * * ?` (Daily at 1:00 AM)
**Action:** Invokes `AnnouncementRepository.deactivateExpired()`, identifying any `isActive = 'Y'` announcement that has exceeded its `expiryDate`, changing its status to `isActive = 'N'`.

## 6. Testing Documentation (Postman + Manual Browser)

### 1. Create Announcement
- **Login** as ADMIN
- **Navigate** to `/admin/announcements/create` in browser or `GET /admin/announcements/create` in Postman for CSRF token.
- **Submit POST** to `/admin/announcements/create` payload.
- **Verify** notification is logged and `success` redirect exists.

### 2. Update Announcement
- **Navigate** to `/admin/announcements/edit/{id}`
- **Submit POST** changing `title` and `content`.
- **Query DB** to verify `updated_at` has changed.

### 3. View Active Announcements
- **Login** as EMPLOYEE
- **Navigate** to `/announcements`
- **Verify** visibility is restricted to properties where `isActive = 'Y'` and `expiryDate >= TODAY`.

### 4. Expiry Handling
- **Create** announcement with past expiry.
- **Trigger Scheduler** natively via Spring Boot actuator or test context.
- **VerifyDB** `isActive = 'N'` for that specific row.

### 5. Manual Deactivation
- **Submit POST** to `/admin/announcements/deactivate/{id}` (CSRF allowed via ADMIN login).
- **Verify DB** `isActive = 'N'`.

### 6. Delete Announcement
- **Submit POST** to `/admin/announcements/delete/{id}`
- **Verify DB** Row corresponding to ID is completely missing.
