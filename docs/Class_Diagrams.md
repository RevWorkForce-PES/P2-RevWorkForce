# Detailed Class Diagrams

This document contains comprehensive Class Diagrams mapping all properties for Entities and Data Transfer Objects (DTOs) within the RevWorkForce platform.

## 1. Core HR Domain Models

```mermaid
classDiagram
    class Employee {
        -String employeeId
        -String firstName
        -String lastName
        -String email
        -String phone
        -LocalDate dateOfBirth
        -Gender gender
        -String address
        -String city
        -String state
        -String postalCode
        -String country
        -String emergencyContactName
        -String emergencyContactPhone
        -Department department
        -Designation designation
        -Employee manager
        -LocalDate joiningDate
        -LocalDate leavingDate
        -BigDecimal salary
        -EmployeeStatus status
        -String passwordHash
        -Character firstLogin
        -LocalDateTime lastLogin
        -Integer failedLoginAttempts
        -Character accountLocked
        -LocalDateTime lockedUntil
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Set~Role~ roles
    }

    class Department {
        -Long departmentId
        -String departmentName
        -String departmentHead
        -String description
        -Character isActive
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Designation {
        -Long designationId
        -String designationName
        -String designationLevel
        -BigDecimal minSalary
        -BigDecimal maxSalary
        -String description
        -Character isActive
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Role {
        -Long roleId
        -String roleName
        -String roleDescription
        -LocalDateTime createdAt
    }

    class EmployeeRole {
        -String employeeId
        -Long roleId
        -Employee employee
        -Role role
        -LocalDateTime assignedAt
        -String assignedBy
    }

    Employee "*" --> "1" Department : belongs to
    Employee "*" --> "1" Designation : holds
    Employee "*" --> "1" Employee : supervised by
    Employee "1" <--> "*" Role : linked via EmployeeRole
```

## 2. Leave and Holiday Management

```mermaid
classDiagram
    class LeaveApplication {
        -Long applicationId
        -Employee employee
        -LeaveType leaveType
        -LocalDate startDate
        -LocalDate endDate
        -Integer totalDays
        -String reason
        -LeaveStatus status
        -LocalDateTime appliedOn
        -Employee approvedBy
        -LocalDateTime approvedOn
        -String rejectionReason
        -String comments
    }

    class LeaveBalance {
        -Long balanceId
        -Employee employee
        -LeaveType leaveType
        -Integer year
        -Integer totalAllocated
        -Integer used
        -Integer balance
        -Integer carriedForward
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class LeaveType {
        -Long leaveTypeId
        -String leaveCode
        -String leaveName
        -Integer defaultDays
        -Integer maxCarryForward
        -String description
        -Character requiresApproval
        -Character isActive
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Holiday {
        -Long holidayId
        -LocalDate holidayDate
        -String holidayName
        -HolidayType holidayType
        -Character isOptional
        -String description
        -Character isActive
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    LeaveApplication "*" --> "1" Employee : submitted by
    LeaveApplication "*" --> "1" LeaveType : of type
    LeaveBalance "*" --> "1" Employee : owned by
    LeaveBalance "*" --> "1" LeaveType : mapped to
```

## 3. Performance & Goals System

```mermaid
classDiagram
    class PerformanceReview {
        -Long reviewId
        -Employee employee
        -Integer reviewYear
        -String reviewPeriod
        -String keyDeliverables
        -String achievements
        -String improvementAreas
        -BigDecimal selfAssessmentRating
        -String selfAssessmentComments
        -String managerFeedback
        -BigDecimal technicalSkills
        -BigDecimal communication
        -BigDecimal teamwork
        -BigDecimal leadership
        -BigDecimal punctuality
        -BigDecimal managerRating
        -String managerComments
        -BigDecimal finalRating
        -ReviewStatus status
        -LocalDate submittedDate
        -Employee reviewedBy
        -LocalDate reviewedDate
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Goal {
        -Long goalId
        -Employee employee
        -String goalTitle
        -String goalDescription
        -String category
        -LocalDate deadline
        -Priority priority
        -Integer progress
        -GoalStatus status
        -String managerComments
        -LocalDateTime completedAt
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    PerformanceReview "*" --> "1" Employee : target
    PerformanceReview "*" --> "1" Employee : reviewer
    Goal "*" --> "1" Employee : assigned to
```

## 4. System Logs, Notifications and Announcements

```mermaid
classDiagram
    class AuditLog {
        -Long auditId
        -Employee employee
        -String action
        -String tableName
        -String recordId
        -String fieldName
        -String oldValue
        -String newValue
        -String description
        -String ipAddress
        -String userAgent
        -LocalDateTime createdAt
    }

    class Notification {
        -Long notificationId
        -Employee employee
        -NotificationType notificationType
        -String title
        -String message
        -String referenceType
        -Long referenceId
        -Character isRead
        -LocalDateTime readAt
        -NotificationPriority priority
        -LocalDateTime createdAt
        -LocalDateTime expiresAt
    }

    class Announcement {
        -Long announcementId
        -String title
        -String message
        -String announcementType
        -Priority priority
        -String targetAudience
        -Character isActive
        -LocalDate publishDate
        -LocalDate expiryDate
        -Employee createdBy
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    AuditLog "*" --> "1" Employee : triggered by
    Notification "*" --> "1" Employee : received by
    Announcement "*" --> "1" Employee : posted by
```

## 5. Primary DTOs (Data Transfer Objects)

```mermaid
classDiagram
    class EmployeeDTO {
        -String employeeId
        -String firstName
        -String lastName
        -String email
        -String phone
        -Long departmentId
        -String departmentName
        -Long designationId
        -String designationName
        -String managerId
        -String managerName
        -LocalDate joiningDate
        -BigDecimal salary
        -EmployeeStatus status
        -Set~Long~ roleIds
        -Set~String~ roleNames
    }

    class LeaveApplicationDTO {
        -String leaveType
        -LocalDate startDate
        -LocalDate endDate
        -String reason
        -Integer totalDays
        -LeaveStatus status
        -String managerComments
        -String rejectionReason
    }

    class PerformanceReviewDTO {
        -Long reviewId
        -String employeeId
        -String employeeName
        -Integer reviewYear
        -String reviewPeriod
        -ReviewStatus status
        -String achievements
        -BigDecimal selfAssessmentRating
        -BigDecimal managerRating
        -BigDecimal overallRating
        -String reviewedByName
    }
    
    class ReportDTO {
        -Long totalEmployees
        -Long activeEmployees
        -Long pendingLeaves
        -Long approvedLeaves
        -Long totalGoals
        -Map employeeDistribution
        -Map leaveStatistics
        -Map departmentStatistics
    }
```
