# Comprehensive Sequence Diagrams

This document charts out the detailed interactions between the System Actors (Employees, Managers, Admins), Controllers, Service Implementations, and Repositories. It provides an architectural glimpse into how data is processed within RevWorkForce.

## 1. Leave Management: End-to-End Application and Approval
This sequence tracks the steps from an employee submitting a leave to the manager taking an action on it, and the resulting validations.

```mermaid
sequenceDiagram
    actor Employee
    participant UI as Browser/Thymeleaf
    participant LC as LeaveController
    participant LS as LeaveServiceImpl
    participant NS as NotificationServiceImpl
    participant LR as LeaveRepository
    participant LBR as LeaveBalanceRepository
    actor Manager
    
    %% Employee Form Submission
    Employee->>UI: Submit Leave Form (Dates, Reason, Type)
    UI->>LC: POST /employee/apply HTTP/1.1
    LC->>LS: applyForLeave(employeeEmail, LeaveApplicationDTO)
    
    %% Business Valditions
    LS->>LBR: findByEmployeeAndLeaveType(employee.getId(), type.getId())
    LBR-->>LS: LeaveBalance
    LS->>LS: calculateRequestedDays(startDate, endDate)
    LS->>LS: validateDateOverlaps(employee, dates)
    alt Balance Insufficient
        LS-->>LC: throw LeaveAllocationException("Insufficient Balance")
        LC-->>UI: Return to Form with Error
    else Valid Application
        LS->>LR: save(new LeaveApplication)
        LR-->>LS: Application Persistent Entity
        LS->>NS: triggerNotification(manager, "A new leave requires approval")
        LS-->>LC: Return Success Payload
        LC-->>UI: Redirect to Leave History View
    end
    
    %% Manager Action
    Manager->>UI: Clicks "Approve" on Pending Request Item
    UI->>LC: POST /manager/approve/{id} HTTP/1.1
    LC->>LS: approveLeave(id, managerIdentifier)
    LS->>LR: findById(id)
    LR-->>LS: Pending LeaveApplication
    LS->>LS: verifyManagerAuthority(manager, application.getEmployee())
    LS->>LBR: updateLeaveBalance(employee, daysToDeduct)
    LBR-->>LS: Updated Balance Persistent Entity
    LS->>LR: setStatus(APPROVED); save(LeaveApplication)
    LR-->>LS: Approved Entity
    LS->>NS: triggerNotification(employee, "Leave Approved")
    LS-->>LC: Return Success Payload
    LC-->>UI: Redirect to Manager Dashboard
```

## 2. Performance Reviews: Standard Evaluation Cycle
This workflow tracks a Manager kicking off a review, the employee filling their assessment, and the Manager sealing the final official ratings.

```mermaid
sequenceDiagram
    actor Manager
    actor Employee
    participant PC as PerformanceReviewController
    participant PS as PerformanceReviewServiceImpl
    participant PR as PerformanceReviewRepository
    
    %% Manager Initiates Cycle
    Manager->>PC: POST /manager/reviews/create {employeeId, year}
    PC->>PS: initiateReview(Manager.getId(), ReviewDTO)
    PS->>PS: validateEligibility(employeeId)
    PS->>PR: save(new Review(STATUS: PENDING_SELF_ASSESSMENT))
    PR-->>PS: Initiated Entity
    PS-->>PC: Return Payload
    
    %% Employee Action
    Employee->>PC: POST /employee/reviews/self-assessment/{reviewId} {selfRatings}
    PC->>PS: submitSelfAssessment(employeeId, reviewId, ReviewDTO)
    PS->>PR: findById(reviewId)
    PR-->>PS: Active Entity
    PS->>PS: mapSelfRatingsToEntity()
    PS->>PR: save(Updated Review(STATUS: PENDING_MANAGER_REVIEW))
    PR-->>PS: Updated Entity
    PS-->>PC: Return Payload
    
    %% Manager Finalizes
    Manager->>PC: POST /manager/reviews/review/{reviewId} {managerRatings}
    PC->>PS: finalizeReview(managerId, reviewId, ReviewDTO)
    PS->>PR: findById(reviewId)
    PR-->>PS: Active Entity waiting on Manager
    PS->>PS: mapManagerRatingsToEntity()
    PS->>PS: calculateOverallRating(managerRatings)
    PS->>PR: save(Finalized Review(STATUS: COMPLETED))
    PR-->>PS: Completed Entity
    PS-->>PC: Return Payload
```

## 3. Goals Tracking: Self-Assignment & Reporting
This sequence tracks an employee creating a goal and progressively updating it until completion.

```mermaid
sequenceDiagram
    actor Employee
    participant GC as GoalController
    participant GS as GoalServiceImpl
    participant GR as GoalRepository
    actor SysAdmin

    Employee->>GC: POST /employee/goals/create {Title, Deadline}
    GC->>GS: createGoal(employeeId, GoalDTO)
    GS->>GR: save(Goal(status: IN_PROGRESS, progress: 0))
    GR-->>GS: Persistent Goal Entity
    GS-->>GC: Return Success State
    
    loop During execution cycle
        Employee->>GC: POST /employee/goals/progress/{id} {progress: 50}
        GC->>GS: updateProgress(employeeId, goalId, 50)
        GS->>GR: findById(goalId)
        GR-->>GS: Active Entity
        GS->>GR: save(Updated Entity)
        GR-->>GS: Saved Entity
        GS-->>GC: Return Payload
    end

    Employee->>GC: POST /employee/goals/progress/{id} {progress: 100}
    GC->>GS: updateProgress(employeeId, goalId, 100)
    GS->>GS: evaluateCompletionThreshold(100)
    GS->>GR: save(Goal(status: COMPLETED, completedAt: now()))
    GR-->>GS: Completed Entity
    GS-->>GC: Return Payload
    
    SysAdmin->>GS: REST API GET /admin/reports/goals
    GS->>GR: findAllActiveAndCompleted()
    GR-->>GS: List~Goal~
    GS->>GS: aggregateGoalStatistics(List)
    GS-->>SysAdmin: Return comprehensive JSON Map Report
```
