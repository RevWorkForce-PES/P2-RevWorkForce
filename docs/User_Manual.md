# Comprehensive User Manual

## Welcome to the RevWorkForce Platform

The unified HR platform streamlines communication between you, your reporting manager, and organizational operations.

---

## 🔒 1. Access & Account Security
- **Login**: Accessed via the portal landing. Supports email addresses or Employee IDs directly.
- **Password Policies**: Accounts will automatically lock after consecutive failed verification attempts. Contact your SysAdmin for unlocks.
- **First-time Login**: Users are prompted through a mandatory workflow to change temporary passwords and configure their primary Security Questions.

---

## 👤 2. Standard Employee Toolset

### 2.1 Leaves & Allowances
- **Applying**: Navigate to `Leaves -> Apply`. Select your Leave Type appropriately. You cannot apply for back-dated leaves or spans that overlap an already pending/approved system request.
- **Balance Checking**: Navigate to `Leaves -> Balances`. Ensure you have sufficient balance in the pool before submitting a request.
- **History Tracker**: Navigate to `Leaves -> History`. From here, you can withdraw an application that has not yet been processed by your manager.

### 2.2 Goal Assignments
- **My Goals Tab**: Upon assignment, goals appear actively here.
- **Progress Tracking**: You are responsible for manually updating your percent-completion milestone markers. Clicking `Log Progress` allows incrementing your progress metric.
- **Status Shifts**: Reaching `100%` automatically seals the goal as `COMPLETED` and notifies your manager.

### 2.3 Performance Self-Assessments
- **Notification**: When the HR cycle begins, you will receive a notification of a pending review.
- **Input Forms**: You must fill out the subjective commentary boxes detailing key deliverables, and score yourself contextually against metrics like Communication, Technical Skills, and Leadership.

---

## 👥 3. Managerial Toolset

You possess all regular Employee actions, as well as the authority to execute workflow actions upon personnel reporting to you structurally.

### 3.1 Managing Subordinates
- Focus your actions within the `Team Management` console.
- **Goal Assignment**: You can forge new structural goals on behalf of your employee and enforce priority thresholds and rigid deadlines directly. This is sent to their own dashboard.
- **Commenting**: Intervene on an employee's active uncompleted goal to provide midpoint guiding comments that they will see synchronously.

### 3.2 Leave Adjudications
- **The Queue**: Navigate to `Team Leaves`. This populates entirely with standard `PENDING` states from your direct reports.
- **Context Review**: Always compare pending individual leave dates against overall `Team Schedule` projections. Simultaneous absences will be highlighted here.
- **Approval Actions**: Selecting Approve hard-deducts the balance values directly from the employee. Rejecting a request necessitates appending a standard `Rejection Reason` string informing the employee of the decision context.

### 3.3 Driving Review Cycles
- **Initiation Process**: You instantiate the `Performance Review Cycle` for a subordinate, bounding it by Year and specific Quarter. This sends an alert signaling the employee to start their subjective self-assessment.
- **Final Grading Form**: Post self-assessment, you review their commentary and finalize with permanent `Manager Scores` and a definitive `Overall Review Grading` formula.

---

## ⚙️ 4. Administrator Operations
Administrators maintain cross-organizational data tracking parameters and bypass strict report structures to maintain system continuity.

### 4.1 Reports Execution
- Accessible under `Admin Dashboard -> Operations`.
- **System Extractions**: Generational algorithms provide real-time aggregation sets tracking the percentage utilization of total sick leaves company-wide, performance percentile spreads, and user metrics formatted visually or exported raw.

### 4.2 Calender Updates
- Maintain the static Holiday definitions. Setting an upcoming holiday enforces the application layer to nullify leave-day-counter tracking if an employee’s requested holiday spans the registered date.

### 4.3 Forced Deletions
- In circumstances of error, the admin can perform hard-deletes of initialized Performance Reviews and Leave items, triggering rollback logs. Use these operations lightly as they permanently erase audit context tracks.
