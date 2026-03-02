
LEAVE MANAGEMENT MODULE DOCUMENTATION (Final – Complete Version)
Enterprise-Grade Technical Documentation
==================================================================

1️⃣ MODULE OVERVIEW

The Leave Management Module handles:

Leave application lifecycle
Manager approval workflow
Leave balance tracking
Overlap detection
Working day calculation
Status transition enforcement
Role-based authorization (RBAC)

This module integrates with:

Authentication Module
Employee Module
Holiday Module

Explanation:
The Leave Management Module is a core functional component of the HRM system.
It ensures structured processing of employee leave requests while maintaining
data integrity, workflow correctness, and security enforcement.

The module is designed to prevent inconsistent leave states, avoid balance
miscalculations, and enforce hierarchical authorization logic.


==================================================================

2️⃣ LAYERED ARCHITECTURE

The Leave Module follows clean layered architecture:

Controller → Service → Repository → Entity → Database

Explanation:
This layered structure ensures separation of concerns.

Controller Layer:
Handles HTTP requests, input binding, and method-level security.

Service Layer:
Contains business logic, validation rules, authorization checks,
and transactional processing.

Repository Layer:
Handles database interaction using Spring Data JPA.

Entity Layer:
Represents domain models mapped to relational tables.

Database Layer:
Ensures persistent storage and referential integrity.

This architecture ensures maintainability, scalability, and testability.


==================================================================

3️⃣ ENTITY (MODEL) LAYER

1️⃣ LeaveApplication.java

Represents leave request lifecycle.

Key Fields:
applicationId
employee (ManyToOne)
leaveType (ManyToOne)
startDate
endDate
totalDays
reason
status (Enum)
approvedBy
approvedOn
rejectionReason
comments
appliedOn

Status stored as:
@Enumerated(EnumType.STRING)

Explanation:
This entity manages the full lifecycle of a leave request.
It stores both business data (dates, reason, type) and audit metadata
(approvedBy, approvedOn, rejectionReason).

Storing status as STRING prevents enum ordinal corruption in database.


------------------------------------------------------------

2️⃣ LeaveBalance.java

Stores yearly leave balance per employee.

Fields:
employee
leaveType
year
totalAllocated
used
balance

Explanation:
This entity ensures leave balance integrity.
It tracks allocated days, used days, and remaining balance.
All approval and cancellation operations update this entity
inside a transactional boundary.


------------------------------------------------------------

3️⃣ LeaveType.java

Master table for leave types:
CL
SL
PL
etc.

Explanation:
This entity represents configurable leave categories.
It allows flexible extension of leave policies without modifying
core business logic.


------------------------------------------------------------

4️⃣ Holiday.java

Used in working day calculation.

Explanation:
Holiday entity ensures accurate working day calculation by excluding
organization-defined holidays during leave computation.


==================================================================

4️⃣ ENUMS

LeaveStatus.java

PENDING
APPROVED
REJECTED
CANCELLED

Explanation:
The LeaveStatus enum defines controlled workflow states.
Strict status transition enforcement prevents inconsistent lifecycle behavior.


==================================================================

5️⃣ DTO LAYER

LeaveApplicationDTO.java

Used for:
Applying leave
Validation binding
Transfer between Controller & Service

Includes:
leaveType
startDate
endDate
reason
totalDays
status
managerComments
rejectionReason

Explanation:
DTO layer isolates internal entities from external input.
It prevents direct entity exposure and supports validation annotations.


------------------------------------------------------------

LeaveBalanceDTO.java

Used for displaying:
leaveType
totalAllocated
usedLeaves
remainingBalance
year

Explanation:
Used only for data projection and UI representation.
Prevents unnecessary exposure of entity structure.


==================================================================

6️⃣ REPOSITORY LAYER

LeaveApplicationRepository

Handles:
findByEmployee_EmployeeIdOrderByAppliedOnDesc()
findOverlappingLeaves()
findPendingLeavesByManagerId()
findTeamLeavesByManagerId()

Explanation:
These methods enable:
• Employee history retrieval
• Overlap validation logic
• Manager pending approval queries
• Team leave tracking


------------------------------------------------------------

LeaveBalanceRepository

Handles:
findByEmployee_EmployeeIdAndYear()
findByEmployeeAndLeaveTypeAndYear()

Ensures accurate yearly leave tracking.


------------------------------------------------------------

LeaveTypeRepository

Handles:
findByLeaveCode()


------------------------------------------------------------

HolidayRepository

Used for:
existsByHolidayDate()


==================================================================

7️⃣ SERVICE LAYER

LeaveService.java

Defines full leave lifecycle methods:

applyLeave()
approveLeave()
rejectLeave()
cancelLeave()
getLeaveById()
getEmployeeLeaveHistory()
getPendingLeavesForManager()
getTeamLeaves()
getLeaveBalances()
validateLeaveOverlap()
calculateWorkingDays()
validateLeaveBalance()
initializeLeaveBalances()


------------------------------------------------------------

LeaveServiceImpl.java

Contains business rules:

Date validation
Overlap detection
Working day calculation
Continuous leave limit validation
Balance deduction
Balance restoration
Manager authorization
Admin override logic
Status transition enforcement

All methods wrapped under:
@Transactional

Explanation:
Service layer is the heart of business logic.
It ensures:
• Atomic database updates
• Rollback on failure
• Multi-step validation consistency
• Authorization enforcement beyond controller level


==================================================================

8️⃣ CONTROLLER LAYER

LeaveController.java

Handles:

Employee Endpoints
GET  /leave/employee/apply
POST /leave/employee/apply
GET  /leave/employee/history
GET  /leave/employee/balance
POST /leave/employee/cancel/{id}

Manager Endpoints
GET  /leave/manager/pending
GET  /leave/manager/team
GET  /leave/manager/review/{id}
POST /leave/manager/approve/{id}
POST /leave/manager/reject/{id}

Explanation:
Controller layer:
• Accepts HTTP requests
• Binds DTOs
• Enforces RBAC using @PreAuthorize
• Delegates logic to Service layer


==================================================================

9️⃣ ROLE-BASED ACCESS CONTROL (RBAC)

Hierarchical RBAC:

ADMIN > MANAGER > EMPLOYEE

Access Matrix:

Feature      EMPLOYEE   MANAGER   ADMIN
Apply Leave     ✅         ✅        ✅
View Own Leave  ✅         ✅        ✅
Manager Approval ❌        ✅        ✅
Team Leave View  ❌        ✅        ✅

Security enforced via:
@PreAuthorize
AND
Service-level authorization validation.

Explanation:
Dual-layer enforcement ensures:
• Endpoint protection
• Business-level protection
• Admin override capability
• Prevention of unauthorized approval attempts


==================================================================

🔟 LEAVE WORKFLOW

APPLY FLOW
Validate date range
Check overlap
Calculate working days
Check leave balance
Create LeaveApplication (PENDING)

Explanation:
Balance is not deducted at apply stage.
Prevents premature balance modification.


------------------------------------------------------------

APPROVAL FLOW
Validate status
Validate manager authorization
Deduct leave balance
Update status → APPROVED

Executed inside transaction to ensure consistency.


------------------------------------------------------------

REJECTION FLOW
Validate status
Validate authorization
Update status → REJECTED
Store rejection reason


------------------------------------------------------------

CANCELLATION FLOW
If PENDING:
→ Status = CANCELLED
If APPROVED:
→ Restore leave balance
→ Status = CANCELLED


==================================================================

1️⃣1️⃣ STATUS TRANSITION MATRIX

From        To
PENDING     APPROVED
PENDING     REJECTED
PENDING     CANCELLED
APPROVED    CANCELLED
REJECTED    ❌
CANCELLED   ❌

Invalid transitions throw:
InvalidStatusTransitionException

Explanation:
Strict state machine prevents illegal lifecycle modifications.


==================================================================

1️⃣2️⃣ WORKING DAY CALCULATION

Excludes:
Saturday
Sunday
Holidays

Uses:
holidayRepository.existsByHolidayDate()

Ensures only business days counted.
Prevents incorrect balance deduction.


==================================================================

1️⃣3️⃣ VALIDATION RULES

Date Rules:
End date ≥ Start date
Cannot overlap existing PENDING/APPROVED leave
Continuous leave ≤ 15 working days

Balance Rules:
Requested days ≤ remaining balance

Rejection Rules:
Rejection reason mandatory

Explanation:
Validations protect business integrity and prevent inconsistent records.


==================================================================

1️⃣4️⃣ EXCEPTION HANDLING

Custom Exceptions Used:

ResourceNotFoundException
ValidationException
LeaveOverlapException
InsufficientLeaveBalanceException
InvalidStatusTransitionException
UnauthorizedException

Explanation:
Custom exceptions ensure meaningful error reporting
and controlled transaction rollback.


==================================================================

1️⃣5️⃣ DATABASE TABLES USED

Table                Purpose
LEAVE_APPLICATIONS   Leave records
LEAVE_BALANCES       Year-wise balance
LEAVE_TYPES          Leave master
HOLIDAYS             Holiday calendar
EMPLOYEES            Employee hierarchy


==================================================================

1️⃣6️⃣ TESTING SCENARIOS

Apply Leave
Overlap Validation
Insufficient Balance
Manager Approval
Admin Approval
Rejection
Cancellation with Balance Restoration
Unauthorized Approval Attempt

Tested via:
Browser
API (Postman)
Database verification


==================================================================

1️⃣7️⃣ PROJECT STRUCTURE

src/main/java/com/revature/revworkforce/
├── controller/
│   └── LeaveController.java
├── service/
│   ├── LeaveService.java
│   └── impl/LeaveServiceImpl.java
├── repository/
│   ├── LeaveApplicationRepository.java
│   ├── LeaveBalanceRepository.java
│   ├── LeaveTypeRepository.java
│   └── HolidayRepository.java
├── dto/
│   ├── LeaveApplicationDTO.java
│   └── LeaveBalanceDTO.java
├── model/
│   ├── LeaveApplication.java
│   ├── LeaveBalance.java
│   └── LeaveType.java
├── enums/
│   └── LeaveStatus.java
├── exception/
│   └── (Custom Exceptions)


==================================================================

1️⃣8️⃣ MODULE FEATURES

Working day calculation (weekend + holiday exclusion)
Strict status transitions
Overlap prevention
Hierarchical RBAC
Balance integrity protection
Transaction-safe approval flow
Admin override capability
Manager-team authorization validation
Exception-driven validation


==================================================================

🏁 MODULE COMPLETION CHECKLIST

DTO Layer
Entity Layer
Repository Layer
Service Layer
Controller Layer
RBAC Enforcement
Validation Rules
Exception Handling
Workflow Integrity
Database Integration
Manual Testing


==================================================================

🎯 FINAL CONCLUSION

The Leave Management Module:
Implements full enterprise leave lifecycle
Enforces strict business rules
Maintains financial/leave balance integrity
Implements hierarchical RBAC
Prevents unauthorized approval
Is transaction-safe and scalable
It is production-ready and aligned with enterprise architecture standards.


==================================================================
