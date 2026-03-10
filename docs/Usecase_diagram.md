# Comprehensive Use Case Diagrams

This document illustrates the precise mapping of features (use cases) against the specific user roles within the RevWorkForce platform.

## Global Organization Use Cases

```mermaid
usecaseDiagram
    actor Employee as EMP
    actor Manager as MGR
    actor Administrator as ADM

    %% Structural layout for grouping
    package "Core Access & Personal Data" {
        usecase "Authenticate (Login/Logout)" as auth1
        usecase "Update Password" as prof1
        usecase "View Personal Information" as prof2
        usecase "Receive System Notifications" as noti1
    }
    
    package "Leave Management Module" {
        usecase "Apply for Personal Leave" as lev1
        usecase "Check Available Allowances" as lev2
        usecase "Review Team Leave Requests" as lev3
        usecase "Approve/Reject Requests" as lev4
    }

    package "Performance & Development Module" {
        usecase "Declare Active Goals" as perf1
        usecase "Update Goal Checkpoints" as perf2
        usecase "Submit Personal Evaluation" as perf3
        usecase "Monitor Subordinate Goals" as perf4
        usecase "Provide Evaluation Ratings" as perf5
        usecase "Kickoff Evaluation Cycle" as perf6
    }
    
    package "Administration & Reporting Module" {
        usecase "Configure Company Holidays" as adm1
        usecase "Delete Expired Review Cycles" as adm2
        usecase "Post Organization Announcements" as adm3
        usecase "Audit Log Reviews" as adm4
        usecase "Generate Metric Reports" as adm5
    }

    %% Aligning Employee Access
    EMP --> auth1
    EMP --> prof1
    EMP --> prof2
    EMP --> noti1
    EMP --> lev1
    EMP --> lev2
    EMP --> perf1
    EMP --> perf2
    EMP --> perf3

    %% Aligning Manager Access
    MGR --> auth1
    MGR --> prof1
    MGR --> prof2
    MGR --> noti1
    MGR --> lev1
    MGR --> lev2
    MGR --> perf1
    MGR --> perf2
    MGR --> perf3
    
    %% Manager specific extended privileges
    MGR --> lev3
    MGR --> lev4
    MGR --> perf4
    MGR --> perf5
    MGR --> perf6

    %% Aligning Admin Access
    ADM --> auth1
    ADM --> prof1
    ADM --> prof2
    ADM --> adm1
    ADM --> adm2
    ADM --> adm3
    ADM --> adm4
    ADM --> adm5

```

## Actor Privileges Matrix
| Feature Package | Endpoint Path Context | Employee | Manager | Administrator |
|---|---|---|---|---|
| Core Details | `/api/user/*` | Read / Edit | Read / Edit | Scope Read / Write |
| Password Updates | `/api/auth/*` | Edit | Edit | Edit |
| My Leaves | `/employee/apply`, `/employee/history` | Write | Write | No Access |
| Team Leaves | `/manager/review/*`, `/manager/pending` | No Access | Write (Approve) | No Access |
| My Goals | `/employee/goals/*` | Write | Write | No Access |
| Team Goals | `/manager/goals/*` | No Access | Read / Comment | No Access |
| My Reviews | `/employee/reviews/*` | Write (Self only) | Write (Self only) | No Access |
| Team Reviews | `/manager/reviews/*` | No Access | Write | Read / Soft Delete |
| Organization Data | `/admin/*` | Global View Only | Global View Only | Full Read / Extraction |
| Alerts & Notices | `/notifications`, API | Subscribed events | Subscribed events | Broad Broadcasts |

