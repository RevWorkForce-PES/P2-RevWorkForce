# Comprehensive Use Case Diagrams and Specifications

This document illustrates the precise mapping of features (use cases) against the specific user roles within the RevWorkForce platform. It breaks down the system into core functional modules for better readability.

## 1. Authentication and Profile Management

```mermaid
flowchart LR
    Actor((Employee))
    
    UC1([Log In])
    UC2([Log Out])
    UC3([Change Password])
    UC4([View Profile Details])
    UC5([Update Security Questions])
    UC6([Recover Account])
    
    Actor --> UC1
    Actor --> UC2
    Actor --> UC3
    Actor --> UC4
    Actor --> UC5
    Actor --> UC6
```

### Specifications
- **Log In**: Validates credentials against the database. Automatically locks account after 5 failed attempts.
- **Recover Account**: Triggered when a user forgets their password. Leverages Security Questions.

---

## 2. Leave Management Module

```mermaid
flowchart LR
    Emp((Employee))
    Mgr((Manager))
    
    L1([View Leave Balances])
    L2([Submit Leave Request])
    L3([Cancel Pending Leave])
    L4([View Leave History])
    L5([Review Team Requests])
    L6([Approve Leave])
    L7([Reject Leave])
    
    Emp --> L1
    Emp --> L2
    Emp --> L3
    Emp --> L4
    
    Mgr --> L5
    Mgr --> L6
    Mgr --> L7
```

### Specifications
- **Submit Leave Request**: The system calculates the actual duration excluding configured company holidays. Fails if duration exceeds `LeaveBalance`.
- **Review Team Requests**: Managers see overlapping team leaves to gauge department impact before approving.

---

## 3. Performance Review Module

```mermaid
flowchart LR
    Emp((Employee))
    Mgr((Manager))
    Adm((Administrator))
    
    P1([Fill Self-Assessment])
    P2([View Past Reviews])
    P3([Initiate Review Cycle])
    P4([Provide Manager Evaluation])
    P5([View Team Matrix])
    P6([Delete Stale Records])
    
    Emp --> P1
    Emp --> P2
    
    Mgr --> P3
    Mgr --> P4
    Mgr --> P5
    
    Adm --> P6
```

### Specifications
- **Initiate Review Cycle**: Generates a new `PerformanceReview` skeleton and alerts the Employee.
- **Fill Self-Assessment**: Employee fills in subjective achievements and soft-skills ratings.
- **Provide Manager Evaluation**: Manager inputs final overarching ratings which lock the review as `COMPLETED`.

---

## 4. Goals Tracking Module

```mermaid
flowchart LR
    Emp((Employee))
    Mgr((Manager))
    
    G1([Create Personal Goal])
    G2([Update Progress Percentage])
    G3([Cancel Goal])
    G4([Assign Team Goal])
    G5([Monitor Subordinate Goals])
    G6([Leave Goal Feedback])
    
    Emp --> G1
    Emp --> G2
    Emp --> G3
    
    Mgr --> G4
    Mgr --> G5
    Mgr --> G6
```

### Specifications
- **Update Progress**: Employees shift the integer value from 0 to 100.
- **Assign Team Goal**: Managers can forcefully drop a goal onto an employee's dashboard with a strict deadline.

---

## 5. Administration and Internal Operations

```mermaid
flowchart LR
    Adm((Administrator))
    
    A1([Generate Utilization Reports])
    A2([Configure Holidays])
    A3([Post Global Announcements])
    A4([Review Security Audits])
    A5([Manage Department Hierarchy])
    
    Adm --> A1
    Adm --> A2
    Adm --> A3
    Adm --> A4
    Adm --> A5
```

### Specifications
- **Generate Utilization Reports**: Aggregates massive data sets across all other modules into JSON summaries suitable for rendering DataTables and Charts.
- **Review Security Audits**: Admins monitor the `AuditLog` table which tracks every mutation (UPDATE/DELETE/INSERT) committed by any user.
