# ## LEAVE MANAGEMENT MODULE Documentations

---

## 1. ENTITY CLASSES (4 files)

* **LeaveApplication.java** – Represents complete leave request lifecycle
* **LeaveBalance.java** – Stores yearly leave balance per employee
* **LeaveType.java** – Master table for leave categories (CL, SL, PL, etc.)
* **Holiday.java** – Used for working day calculation

---

## 2. DTO CLASSES (2 files)

* **LeaveApplicationDTO.java** – Leave application data transfer with validation
* **LeaveBalanceDTO.java** – Leave balance projection for UI display

---

## 3. REPOSITORY LAYER (4 files)

* **LeaveApplicationRepository.java** – Handles leave queries (history, overlap, pending, team leaves)
* **LeaveBalanceRepository.java** – Handles leave balance retrieval and updates
* **LeaveTypeRepository.java** – Retrieves leave type master data
* **HolidayRepository.java** – Checks holidays for working day calculation

---

## 4. SERVICE LAYER (2 files)

* **LeaveService.java** – Defines full leave lifecycle operations
* **LeaveServiceImpl.java** – Implements business logic (validation, approval, RBAC, balance updates)

---

## 5. CONTROLLER LAYER (1 file)

* **LeaveController.java** – Handles employee and manager leave endpoints

---

# ## LEAVE WORKFLOW

---

## APPLY LEAVE FLOW

```
1. Employee selects leave type
   ↓
2. Selects start and end date
   ↓
3. Enters reason for leave
   ↓
4. System validates:
      - Date range
      - Not past date
      - Working days > 0
      - No overlap with existing leave
      - Sufficient leave balance
   ↓
5. LeaveApplication created
   Status = PENDING
   ↓
6. Redirect to Leave History
```

Important:

* Balance is NOT deducted at apply stage.
* Leave remains pending until manager approval.

---

## APPROVAL FLOW

```
1. Manager views /leave/manager/pending
   ↓
2. Selects leave request
   ↓
3. Approves leave
   ↓
4. System validates:
      - Status must be PENDING
      - Manager must be direct manager
      - Or user must be ADMIN
   ↓
5. Deduct leave balance
   ↓
6. Update status → APPROVED
   ↓
7. Store approvedBy and approvedOn
```

---

## REJECTION FLOW

```
1. Manager selects leave
   ↓
2. Provides mandatory rejection reason
   ↓
3. Status updated → REJECTED
   ↓
4. Rejection reason stored
```

Note:

* No balance deduction happens on rejection.

---

## CANCELLATION FLOW

```
If PENDING:
   → Status = CANCELLED

If APPROVED:
   → Restore leave balance
   → Status = CANCELLED
```

---

# ## STATUS TRANSITION MATRIX

| Current Status | Allowed Next Status |
| -------------- | ------------------- |
| PENDING        | APPROVED            |
| PENDING        | REJECTED            |
| PENDING        | CANCELLED           |
| APPROVED       | CANCELLED           |
| REJECTED       | ❌ Not Allowed       |
| CANCELLED      | ❌ Not Allowed       |

Invalid transitions throw:

* **InvalidStatusTransitionException**

---

# ## ROLE-BASED ACCESS CONTROL (RBAC)

### Role Hierarchy:

```
ADMIN > MANAGER > EMPLOYEE
```

### Access Matrix:

| Feature          | EMPLOYEE | MANAGER | ADMIN |
| ---------------- | -------- | ------- | ----- |
| Apply Leave      | ✅        | ✅       | ✅     |
| View Own Leave   | ✅        | ✅       | ✅     |
| Cancel Own Leave | ✅        | ✅       | ✅     |
| Approve Leave    | ❌        | ✅       | ✅     |
| View Team Leaves | ❌        | ✅       | ✅     |

Security enforced via:

* `@PreAuthorize`
* Service-level authorization validation
* Admin override logic inside Service

---

# ## ENDPOINTS & ACCESS CONTROL

### EMPLOYEE ENDPOINTS (ROLE_EMPLOYEE, ROLE_MANAGER, ROLE_ADMIN)

```
GET  /leave/employee/apply
POST /leave/employee/apply
GET  /leave/employee/history
GET  /leave/employee/balance
POST /leave/employee/cancel/{id}
```

---

### MANAGER ENDPOINTS (ROLE_MANAGER, ROLE_ADMIN)

```
GET  /leave/manager/pending
GET  /leave/manager/team
GET  /leave/manager/review/{id}
POST /leave/manager/approve/{id}
POST /leave/manager/reject/{id}
```

---

# ## BUSINESS VALIDATION RULES

### Date Validation:

* Start date ≥ today
* End date ≥ start date
* Continuous leave ≤ 15 working days

### Overlap Validation:

* Cannot overlap with PENDING or APPROVED leave

### Balance Validation:

* Requested days ≤ remaining balance

### Rejection Rule:

* Rejection reason mandatory

---

# ## WORKING DAY CALCULATION

Working days exclude:

* Saturday
* Sunday
* Holidays (from HOLIDAYS table)

Uses:

```java
holidayRepository.existsByHolidayDate()
```

Only business days are counted for balance deduction.

---

# ## DATABASE INTEGRATION

### Tables Used:

* **LEAVE_APPLICATIONS** – Stores leave records
* **LEAVE_BALANCES** – Year-wise leave balance
* **LEAVE_TYPES** – Leave master data
* **HOLIDAYS** – Holiday calendar
* **EMPLOYEES** – Employee & manager hierarchy

---

# ## TESTING THE LEAVE MODULE

---

### 1️⃣ Apply Leave (Verify PENDING)

```
Login as EMPLOYEE
Go to /leave/employee/apply
Apply leave for 3 working days
Submit

Expected:
- Redirect to history page
- Status = PENDING
- No balance deducted
```

---

### 2️⃣ Overlap Validation

```
Apply leave March 10–12
Try applying March 11–15

Expected:
- Overlap error shown
- Leave not created
```

---

### 3️⃣ Insufficient Balance

```
Remaining balance: 2 days
Try applying for 5 days

Expected:
- InsufficientLeaveBalanceException
- Leave not created
```

---

### 4️⃣ Manager Approval

```
Login as MANAGER
Go to /leave/manager/pending
Approve leave

Expected:
- Status → APPROVED
- Balance deducted
- approvedBy populated
```

---

### 5️⃣ Rejection

```
Manager rejects leave
Provide valid rejection reason

Expected:
- Status → REJECTED
- No balance deduction
```

---

### 6️⃣ Cancellation

```
Employee cancels approved leave

Expected:
- Status → CANCELLED
- Balance restored
```

---

### 7️⃣ Authorization Test

```
EMPLOYEE tries accessing:
/leave/manager/pending

Expected:
- 403 Access Denied
```

---

# ## PROJECT STRUCTURE

```
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
│   └── Custom Exceptions
```

---

# ## LEAVE MODULE CHECKLIST

* Entity Layer
* DTO Layer
* Repository Layer
* Service Layer
* Controller Layer
* RBAC Enforcement
* Validation Rules
* Status Transition Logic
* Exception Handling
* Database Integration
* Manual Testing
* Postman API Testing

---

# ## LEAVE MODULE FEATURES

* Working day calculation (weekend + holiday exclusion)
* Strict status transitions
* Overlap prevention
* Hierarchical RBAC
* Balance deduction & restoration
* Transaction-safe approval flow
* Admin override capability
* Manager-team authorization validation
* Exception-driven validation

---

# ## 🎯 FINAL CONCLUSION

The Leave Management Module:

* Implements complete enterprise leave lifecycle
* Enforces strict business rules
* Maintains leave balance integrity
* Implements hierarchical RBAC
* Prevents unauthorized approval
* Is transaction-safe and scalable

