# Comprehensive Use Case Diagrams and Specifications

This document illustrates the precise mapping of features (use cases) against the specific user roles within the RevWorkForce platform. It breaks down the system into core functional modules for better readability.

## 1. Authentication and Profile Management

```mermaid
usecaseDiagram
    actor Employee
    
    usecase "Log In" as UC1
    usecase "Log Out" as UC2
    usecase "Change Password" as UC3
    usecase "View Profile Details" as UC4
    usecase "Update Security Questions" as UC5
    usecase "Recover Account" as UC6
    
    Employee --> UC1
    Employee --> UC2
    Employee --> UC3
    Employee --> UC4
    Employee --> UC5
    Employee --> UC6
```

### Specifications
- **Log In**: Validates credentials against the database. Automatically locks account after 5 failed attempts.
- **Recover Account**: Triggered when a user forgets their password. Leverages Security Questions.

---

## 2. Leave Management Module

```mermaid
usecaseDiagram
    actor Employee
    actor Manager
    
    usecase "View Leave Balances" as L1
    usecase "Submit Leave Request" as L2
    usecase "Cancel Pending Leave" as L3
    usecase "View Leave History" as L4
    usecase "Review Team Requests" as L5
    usecase "Approve Leave" as L6
    usecase "Reject Leave" as L7
    
    Employee --> L1
    Employee --> L2
    Employee --> L3
    Employee --> L4
    
    Manager --> L5
    Manager --> L6
    Manager --> L7
```

### Specifications
- **Submit Leave Request**: The system calculates the actual duration excluding configured company holidays. Fails if duration exceeds `LeaveBalance`.
- **Review Team Requests**: Managers see overlapping team leaves to gauge department impact before approving.

---

## 3. Performance Review Module

```mermaid
usecaseDiagram
    actor Employee
    actor Manager
    actor Administrator
    
    usecase "Fill Self-Assessment" as P1
    usecase "View Past Reviews" as P2
    usecase "Initiate Review Cycle" as P3
    usecase "Provide Manager Evaluation" as P4
    usecase "View Team Matrix" as P5
    usecase "Delete Stale Records" as P6
    
    Employee --> P1
    Employee --> P2
    
    Manager --> P3
    Manager --> P4
    Manager --> P5
    
    Administrator --> P6
```

### Specifications
- **Initiate Review Cycle**: Generates a new `PerformanceReview` skeleton and alerts the Employee.
- **Fill Self-Assessment**: Employee fills in subjective achievements and soft-skills ratings.
- **Provide Manager Evaluation**: Manager inputs final overarching ratings which lock the review as `COMPLETED`.

---

## 4. Goals Tracking Module

```mermaid
usecaseDiagram
    actor Employee
    actor Manager
    
    usecase "Create Personal Goal" as G1
    usecase "Update Progress Percentage" as G2
    usecase "Cancel Goal" as G3
    usecase "Assign Team Goal" as G4
    usecase "Monitor Subordinate Goals" as G5
    usecase "Leave Goal Feedback" as G6
    
    Employee --> G1
    Employee --> G2
    Employee --> G3
    
    Manager --> G4
    Manager --> G5
    Manager --> G6
```

### Specifications
- **Update Progress**: Employees shift the integer value from 0 to 100.
- **Assign Team Goal**: Managers can forcefully drop a goal onto an employee's dashboard with a strict deadline.

---

## 5. Administration and Internal Operations

```mermaid
usecaseDiagram
    actor Administrator
    
    usecase "Generate Utilization Reports" as A1
    usecase "Configure Holidays" as A2
    usecase "Post Global Announcements" as A3
    usecase "Review Security Audits" as A4
    usecase "Manage Department Hierarchy" as A5
    
    Administrator --> A1
    Administrator --> A2
    Administrator --> A3
    Administrator --> A4
    Administrator --> A5
```

### Specifications
- **Generate Utilization Reports**: Aggregates massive data sets across all other modules into JSON summaries suitable for rendering DataTables and Charts.
- **Review Security Audits**: Admins monitor the `AuditLog` table which tracks every mutation (UPDATE/DELETE/INSERT) committed by any user.
