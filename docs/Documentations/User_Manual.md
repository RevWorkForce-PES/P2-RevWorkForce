# Comprehensive User Manual

## Welcome to the RevWorkForce Platform

The unified HR platform streamlines communication between you, your reporting manager, and organizational operations. It provides centralized tools for evaluating performance, setting goals, and tracking attendance schedules.

---

## 🔒 1. Access & Account Management
### System Login
- **URL**: Go to the main deployment portal.
- **Credentials**: Enter your corporate Email Address or internal Employee ID.
- **Forgot Password**: Use the recovery link on the login page. You will be prompted with the two security questions established during your orientation.

### Account Security Policies
1. **First-time Login Workflow**: You cannot bypass the initial screen. You are forced to establish a permanent password overriding the temporary one issued by HR.
2. **Lockout Parameters**: After 5 consecutive invalid authentication attempts, the system drops into a hardened `LOCKED` state. This state utilizes a timeout mechanism. You must wait 15 minutes before re-attempting or contact an Administrator for an immediate manual override.

---

## 👤 2. Standard Employee Toolset

As an employee, RevWorkForce acts as your digital HR representative. 

### 2.1 My Profile & Information
- **Directory**: The global directory allows finding organizational contacts, viewing reporting chains, and fetching email parameters.
- **My Information**: Update your localized address and emergency contact details seamlessly. Note: Updating your legal name or bank-routing data requires offline authorization.

### 2.2 Leaves & Allowances (Time Off)
1. **Balance Information**: Navigate to `Leaves -> Balances`. The visual chart breaks down your allotments (e.g., Casual Leaves remaining vs. Sick Leaves expended).
2. **Applying for Leave**: 
   - Navigate to `Leaves -> Apply`. 
   - Choose your type, pick the dates.
   - The system validates that there are no intersections with existing requests or global company holidays, then forwards it to your manager.
3. **Leave History Tracking**: Track real-time status updates on all submissions. If a manager has not yet viewed your application (`PENDING`), you retain the ability to withdraw it via the **Cancel Application** action.

### 2.3 Managing Objectives (Goals)
- **Reviewing Targets**: Assigned goals appear under the `My Goals` dashboard.
- **Updating Metrics**: It is strictly your responsibility to maintain the velocity of your targets. Click `Log Progress` and adjust the slider to signify completion percentages.
- **Automation**: Shifting a goal's progress explicitly to `100%` triggers background automation; the goal assumes a `COMPLETED` state and generates a closure notification to your manager.

### 2.4 Performance Review Self-Assessments
- **Receiving Cycles**: HR or your Manager kicks off Evaluation Cycles quarterly or annually. You will receive an immediate notification alerting you that intervention is required.
- **Providing Insight**: Click down into the active Review. Use the rich-text fields to articulate subjective accomplishments. Score yourself on standard organizational principles using the sliding 1-5 scales.
- **Submission**: Once submitted, it passes to your Manager and cannot be undone via standard tools.

---

## 👥 3. Managerial Toolset

Managers possess all standard privileges of an Employee, augmented with definitive authority over the subordinate team mapped structurally to their profile.

### 3.1 The Team Console
- By navigating to `Manager Dashboard -> Team Management`, you can view holistic data points. It indicates how many active reports you have, lists their current goals, and identifies unread review statuses.

### 3.2 Processing Leaves
- **The Queue**: All leaves requested by a subordinate pool into the `Team Leaves -> Pending Approvals` routing board.
- **Contextual Review**: Before actioning, compare the specific leave dates against identical requests from other team members within the same week to prevent operational resource exhaustion.
- **Execution Actions**:
  - **Approve**: Clicks instantly finalize the record. Corresponding balances decrease and an approval alert routes to the employee.
  - **Reject**: Forces a text-prompt requiring you to justify the rejection. This reasoning is visibly attached to the employee's history log.

### 3.3 Driving Performance Evaluation Cycles
- **Initiating the Process**: You possess the authority to construct a `Performance Review Cycle` specific to any subordinate, bounding it by Year and operational Period.
- **The Final Grading Format**: After the employee submits their internal self-assessment mapping, the review cycles back into your `Pending Action` list. 
- You interpret their commentary, and apply permanent overriding `Manager Scores` using rigorous numeric evaluation grids. 
- Submission of these scores permanently seals the record, aggregating a `Final Organization Rating` that serves as standard reporting foundation for the administration tier.

### 3.4 Active Goal Assignments
- Use the structural `Assign Team Goal` action to enforce targets on employees without their direct invocation. You control priority levels and strict expiration limits directly.
- Leave intermediate comments iteratively on their active objective cards offering course corrections.

---

## ⚙️ 4. Administrator Operations
Administrators act outside standard reporting hierarchies to handle configuration and raw data extractions.

### 4.1 Reports Execution Engine
- Accessible under `Admin Dashboard -> Operations -> System Reports`.
- The engine computes macro trends rather than micro actions. Generated output identifies the highest utilization rates of leaves across respective departments, visualizes the distribution of aggregate Performance Ratings company-wide, and extracts lists suitable for export to payroll processing structures.

### 4.2 Application Configuration Matrices
- Configure `Holidays` dynamically. Injecting a public or company-wide observance day immediately restructures the active system calendars. Future Leave duration calculations automatically deduct the public holiday block, saving employees' allowed balances.
- Manage `Announcements`. Trigger organization-wide broadcasts scaling urgency based on standard alerts vs emergency popover banners intercepting primary logins.

### 4.3 Override Authorities
- While audit tracking is persistent, Admins hold `DELETE` access levels. These functions exist as failsafes to resolve severe user-error states (e.g. terminating duplicate performance reviews, overriding locked user authentication tokens). Admin mutations always generate immutable rows on the background `AuditLog` table capturing action histories.
