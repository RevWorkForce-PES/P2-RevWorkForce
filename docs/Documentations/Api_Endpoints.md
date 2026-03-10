# Detailed API Endpoints

This document tracks all REST API endpoints and MVC mapped routes accessible via the RevWorkForce platform.

## 1. Authentication & Users
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `AuthController` | `POST` | `/api/auth/login` | PUBLIC | Authenticates the user credentials and establishes a session/JWT. |
| `AuthController` | `POST` | `/api/auth/logout` | ALL | Terminates the current active session. |
| `UserApiController` | `GET` | `/api/user/info` | ALL | Returns a JSON Map of current active user metadata |
| `GlobalErrorController` | `GET` | `/error` | PUBLIC | Handles framework-level thrown exceptions and renders the error page. |

## 2. Dashboard Rendering Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `EmployeeDashboardController` | `GET` | `/employee/dashboard` | EMPLOYEE | Main dashboard view for employees showing their active stats. |
| `ManagerDashboardController` | `GET` | `/manager/dashboard` | MANAGER | Overview of the team's metrics and pending tasks. |
| `ManagerDashboardController` | `GET` | `/manager/team-management` | MANAGER | Detailed view of reporting employees. |

## 3. Leave Management Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `LeaveController` | `GET` | `/employee/apply` | EMPLOYEE | Renders the leave application form page. |
| `LeaveController` | `GET` | `/employee/history` | EMPLOYEE | Renders the employee's past leave histories. |
| `LeaveController` | `GET` | `/employee/balance` | EMPLOYEE | Renders employee's current leave balances chart. |
| `LeaveController` | `POST` | `/employee/apply` | EMPLOYEE | Submits `LeaveApplicationDTO` to request a new leave. |
| `LeaveController` | `POST` | `/employee/cancel/{id}` | EMPLOYEE | Cancels an un-processed leave request by its ID. |
| `LeaveController` | `GET` | `/manager/pending` | MANAGER | Renders the table of pending leave requests from subordinates. |
| `LeaveController` | `GET` | `/manager/team` | MANAGER | General view of the team's current leave statuses. |
| `LeaveController` | `GET` | `/manager/review/{id}` | MANAGER | Opens details of a specific pending leave application. |
| `LeaveController` | `POST` | `/manager/approve/{id}` | MANAGER | Processes and Approves the leave `id`. |
| `LeaveController` | `POST` | `/manager/reject/{id}` | MANAGER | Processes and Rejects the leave `id`. |
| `AdminActionController` | `POST` | `/admin/leave/revoke/{id}` | ADMIN | Administrator override to forcefully revoke an approved leave. |

## 4. Goals Management Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `GoalController` | `GET` | `/employee/goals` | EMPLOYEE | Dashboard displaying all assigned goals. |
| `GoalController` | `GET` | `/employee/goals/create` | EMPLOYEE | Shows form to create self-assigned goals. |
| `GoalController` | `POST` | `/employee/goals/create` | EMPLOYEE | Submits new goal payload. |
| `GoalController` | `GET` | `/employee/goals/edit/{id}` | EMPLOYEE | Renders form to edit an existing goal. |
| `GoalController` | `POST` | `/employee/goals/edit/{id}` | EMPLOYEE | Saves edits to the goal payload. |
| `GoalController` | `GET` | `/employee/goals/progress/{id}` | EMPLOYEE | Renders form to log progress on the goal. |
| `GoalController` | `POST` | `/employee/goals/progress/{id}` | EMPLOYEE | Updates the progress completion integer on the goal. |
| `GoalController` | `POST` | `/employee/goals/cancel/{id}` | EMPLOYEE | Cancels an active goal. |
| `GoalController` | `GET` | `/employee/goals/view/{id}` | EMPLOYEE | Views full detailed pane of the goal. |
| `GoalController` | `POST` | `/employee/goals/delete/{id}` | EMPLOYEE | Drops self-assigned goals entirely. |
| `GoalController` | `GET` | `/manager/goals/team` | MANAGER | Dashboard view of all subordinate goals. |
| `GoalController` | `GET` | `/manager/goals/comment/{id}` | MANAGER | Renders form for manager to leave feedback on a goal. |
| `GoalController` | `POST` | `/manager/goals/comment/{id}` | MANAGER | Submits feedback on subordinate's goal. |
| `GoalController` | `GET` | `/manager/goals/view/{id}` | MANAGER | Views detailed metric pane for subordinate goal. |

## 5. Performance Reviews Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `PerformanceReviewController` | `GET` | `/employee/reviews` | EMPLOYEE | View active and past performance review states. |
| `PerformanceReviewController` | `GET` | `/employee/reviews/self-assessment/{reviewId}` | EMPLOYEE | Displays self-assessment evaluation questionnaire. |
| `PerformanceReviewController` | `POST` | `/employee/reviews/self-assessment/{reviewId}` | EMPLOYEE | Submits the employee's self-ratings. |
| `PerformanceReviewController` | `GET` | `/manager/reviews` | MANAGER | Renders table listing team performance reviews. |
| `PerformanceReviewController` | `GET` | `/manager/reviews/pending` | MANAGER | Shortcut filter for reviews requiring manager input. |
| `PerformanceReviewController` | `GET` | `/manager/reviews/create` | MANAGER | Displays form to initiate a new performance cycle. |
| `PerformanceReviewController` | `POST` | `/manager/reviews/create` | MANAGER | Kicks off the review cycle for an employee. |
| `PerformanceReviewController` | `GET` | `/manager/reviews/review/{id}` | MANAGER | Manager's form to input official final ratings. |
| `PerformanceReviewController` | `POST` | `/manager/reviews/review/{id}` | MANAGER | Submits final evaluation block. |
| `PerformanceReviewController` | `GET` | `/manager/reviews/view/{id}` | MANAGER | Complete readout of the entire review cycle properties. |
| `PerformanceReviewController` | `POST` | `/admin/reviews/delete/{id}` | ADMIN | Authorized drop of a review record from the database. |

## 6. Notifications Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `NotificationApiController` | `GET` | `/api/notifications/unread-count` | ALL | REST API returning integer of unread notifications. |
| `NotificationApiController` | `GET` | `/api/notifications/recent` | ALL | REST API returning JSON payload of latest `NotificationDTO`s. |
| `NotificationApiController` | `POST` | `/api/notifications/mark-read/{id}` | ALL | REST API flags the notification as Read. |
| `NotificationApiController` | `POST` | `/api/notifications/mark-all-read` | ALL | REST API bulk flag for all unread items. |
| `NotificationViewController` | `GET` | `/notifications` | ALL | Renders user's notifications feed view page. |
| `NotificationViewController` | `GET` | `/notifications/unread` | ALL | Filtered render of feed view page. |
| `NotificationViewController` | `POST` | `/notifications/mark-read/{id}` | ALL | Form POST variant of matching API call. |
| `NotificationViewController` | `POST` | `/notifications/mark-all-read` | ALL | Form POST variant of matching API call. |
| `NotificationViewController` | `POST` | `/notifications/delete/{id}` | ALL | Form POST to delete a notification record. |

## 7. Configuration Data (Holidays, Announcements, Audit)
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `HolidayController` | `GET` | `/employee/holidays` | ALL | Views the read-only annual calendar schedule. |
| `HolidayController` | `GET` | `/admin/holidays` | ADMIN | Manageable list of registered dates. |
| `HolidayController` | `GET`/`POST` | `/admin/holidays/add` | ADMIN | Operations to add new dates to the calendar. |
| `HolidayController` | `GET`/`POST` | `/admin/holidays/edit/{id}` | ADMIN | Edit existing dates and re-calibrate status. |
| `HolidayController` | `POST` | `/admin/holidays/delete/{id}` | ADMIN | Removes specific holiday records. |
## 8. Department Management Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `DepartmentController` | `GET` | `/admin/departments` | ADMIN | Lists all departments. |
| `DepartmentController` | `GET` | `/admin/departments/add` | ADMIN | Shows form to add a new department. |
| `DepartmentController` | `POST` | `/admin/departments/add` | ADMIN | Processes new department creation. |
| `DepartmentController` | `GET` | `/admin/departments/edit/{id}` | ADMIN | Shows form to edit an existing department. |
| `DepartmentController` | `POST` | `/admin/departments/edit/{id}` | ADMIN | Processes department updates. |
| `DepartmentController` | `POST` | `/admin/departments/delete/{id}` | ADMIN | Deactivates/Deletes a department. |

## 9. Designation Management Mappings
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `DesignationController` | `GET` | `/admin/designations` | ADMIN | Lists all designations. |
| `DesignationController` | `GET` | `/admin/designations/add` | ADMIN | Shows form to add a new designation. |
| `DesignationController` | `POST` | `/admin/designations/add` | ADMIN | Processes new designation creation. |
| `DesignationController` | `GET` | `/admin/designations/edit/{id}` | ADMIN | Shows form to edit an existing designation. |
| `DesignationController` | `POST` | `/admin/designations/edit/{id}` | ADMIN | Processes designation updates. |
| `DesignationController` | `POST` | `/admin/designations/delete/{id}` | ADMIN | Deactivates/Deletes a designation. |

## 10. Admin & Employee Account Control
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `AdminController` | `GET` | `/admin/settings` | ADMIN | Dashboard for admin-specific settings. |
| `AdminController` | `GET` | `/admin/user-management` | ADMIN | Central view for managing all system employees. |
| `AdminController` | `POST` | `/admin/reset-password/{id}` | ADMIN | Force reset of an employee's password. |
| `AdminController` | `POST` | `/admin/lock-account/{id}` | ADMIN | Manually locks a user's account. |
| `AdminController` | `POST` | `/admin/unlock-account/{id}` | ADMIN | Manually unlocks a user's account. |
| `AdminController` | `POST` | `/admin/api/bulk-activate` | ADMIN | Activates multiple accounts in a single batch. |
| `AdminController` | `POST` | `/admin/api/bulk-deactivate` | ADMIN | Deactivates multiple accounts in a single batch. |
| `AdminController` | `GET` | `/admin/health` | ADMIN | Database and system health check status. |
| `AdminController` | `GET` | `/admin/api/stats` | ADMIN | System-wide statistics for dashboards. |

## 11. Leave Type Configuration & Quota
| Controller | Method | Endpoint Path | Role / Access | Description |
|---|---|---|---|---|
| `AdminController` | `GET` | `/admin/leave-quota` | ADMIN | Page to manage annual leave quotas. |
| `AdminController` | `POST` | `/admin/leave-quota/assign` | ADMIN | Assigns specific day counts to an employee quota. |
| `AdminController` | `GET` | `/admin/leave-types/add` | ADMIN | Form to create a new leave type category. |
| `AdminController` | `GET` | `/admin/leave-types/edit/{id}` | ADMIN | Form to modify existing leave type rules. |
| `AdminController` | `POST` | `/admin/leave-types/save` | ADMIN | Persists leave type changes. |
| `AdminController` | `GET` | `/admin/leave-types/delete/{id}` | ADMIN | Removes a leave type category. |
