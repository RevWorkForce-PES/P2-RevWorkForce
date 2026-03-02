
# ## HOLIDAY MANAGEMENT MODULE DOCUMENTATION

---

## 1. ENTITY CLASSES (1 file)

* **Holiday.java** – Represents official organization holidays with soft delete support

Fields:
- holidayId
- holidayName
- holidayDate
- holidayType
- description
- isActive
- createdAt
- updatedAt

---

## 2. DTO CLASSES (2 files)

* **HolidayDTO.java** – Used for request/response with validation rules
* **HolidayStatisticsDTO.java** – Projection object for holiday statistics (counts by type)

---

## 3. REPOSITORY LAYER (1 file)

* **HolidayRepository.java**

Handles:
- Fetch active holidays
- Find by date
- Duplicate validation
- Date range filtering
- Upcoming holidays
- Bulk year filtering
- Working day support queries

---

## 4. SERVICE LAYER (2 files)

* **HolidayService.java** – Defines holiday operations
* **HolidayServiceImpl.java** – Implements validation, soft delete, bulk logic, statistics, import

---

## 5. CONTROLLER LAYER (2 files)

* **HolidayController.java** – Admin + Employee holiday endpoints
* **EmployeeDashboardController.java** – Displays upcoming holidays on dashboard

---

# ## HOLIDAY WORKFLOW

---

## CREATE HOLIDAY FLOW (ADMIN)

```

1. Admin navigates to /admin/holidays/add
   ↓
2. Enters holiday name, date, type, description
   ↓
3. System validates:

   * Date not null
   * Date not in past
   * Not weekend
   * No duplicate active holiday
     ↓
4. Holiday saved
   isActive = 'Y'
   ↓
5. Redirect to holiday list

```

---

## EDIT HOLIDAY FLOW

```

1. Admin selects holiday
   ↓
2. Updates details
   ↓
3. Validations re-applied
   ↓
4. Record updated

```

---

## SINGLE DELETE FLOW (SOFT DELETE)

```

1. Admin clicks delete
   ↓
2. System verifies holiday exists
   ↓
3. Set isActive = 'N'
   ↓
4. Record saved
   ↓
5. Redirect to holiday list

```

Note:
Holiday is NOT removed from database.

---

## YEAR-WISE DELETE FLOW (BULK SOFT DELETE)

```

1. Admin enters year
   ↓
2. System calculates:
   Jan 1 → Dec 31
   ↓
3. Fetch all active holidays in that range
   ↓
4. Set isActive = 'N' for all
   ↓
5. Save batch

```

---

## EMPLOYEE HOLIDAY CALENDAR FLOW

```

1. Employee navigates to /employee/holidays
   ↓
2. System fetches:
   findByIsActiveOrderByHolidayDateAsc('Y')
   ↓
3. Display holiday list

```

---

## UPCOMING HOLIDAYS DASHBOARD FLOW

```

1. Employee logs in
   ↓
2. Dashboard loads
   ↓
3. Service fetches:
   holidayDate > today
   ↓
4. Display upcoming holidays sorted ascending

```

---

# ## BUSINESS VALIDATION RULES

### Date Rules:

* Holiday date must not be null
* Holiday cannot be in the past
* Holiday cannot fall on Saturday or Sunday

Validation implemented using:

- DateUtil.isPastDate()
- DateUtil.isWeekend()

---

### Duplicate Rule:

* Cannot create two active holidays on same date

Repository check:
existsByHolidayDateAndIsActive(date, 'Y')

---

# ## SOFT DELETE STRATEGY

Instead of deleting records permanently:

```

holiday.setIsActive('N');

```

Benefits:
- Data recovery possible
- Audit safe
- Reporting support
- Historical data preserved

Admin listing always filters:

findByIsActiveOrderByHolidayDateAsc('Y')

---

# ## ROLE-BASED ACCESS CONTROL (RBAC)

### Role Hierarchy:

```

ADMIN > MANAGER > EMPLOYEE

```

### Access Matrix:

| Feature                  | EMPLOYEE | MANAGER | ADMIN |
| ------------------------ | -------- | ------- | ----- |
| View Holiday Calendar    | ✅        | ✅       | ✅     |
| View Upcoming Holidays   | ✅        | ✅       | ✅     |
| Add Holiday              | ❌        | ❌       | ✅     |
| Edit Holiday             | ❌        | ❌       | ✅     |
| Delete Holiday           | ❌        | ❌       | ✅     |
| Delete by Year           | ❌        | ❌       | ✅     |

Security enforced via:

- @PreAuthorize("hasRole('ADMIN')")
- @PreAuthorize("isAuthenticated()")

---

# ## ENDPOINTS & ACCESS CONTROL

### ADMIN ENDPOINTS

```

GET  /admin/holidays
GET  /admin/holidays/add
POST /admin/holidays/add
GET  /admin/holidays/edit/{id}
POST /admin/holidays/edit/{id}
POST /admin/holidays/delete/{id}
POST /admin/holidays/delete-year

```

---

### EMPLOYEE ENDPOINTS

```

GET  /employee/holidays
GET  /employee/dashboard

```

---

# ## WORKING DAY INTEGRATION (LEAVE MODULE)

Holiday module integrates with Leave module for working day calculation.

Used Methods:

- isHoliday(LocalDate date)
- getHolidaysInRange(startDate, endDate)

Working days exclude:

* Saturday
* Sunday
* Active holidays

Used inside:
DateUtil.calculateWorkingDays()

---

# ## BULK IMPORT RULES

Method:
importHolidays(List<HolidayDTO>)

Rules:

- Skip null dates
- Skip weekend holidays
- Skip duplicate active holidays
- Continue processing on error
- Return success count

---

# ## STATUS LOGIC

Holiday does not use multi-stage lifecycle.

Only status indicator:
isActive ('Y' or 'N')

---

# ## DATABASE TABLES USED

* HOLIDAYS
* EMPLOYEES (for dashboard integration)

---

# ## TESTING THE HOLIDAY MODULE

---

### 1️⃣ Create Holiday

```

Admin creates valid holiday
Expected:

* Holiday saved
* isActive = 'Y'

```

---

### 2️⃣ Past Date Validation

```

Try creating holiday in past
Expected:

* Error shown
* Record not created

```

---

### 3️⃣ Weekend Validation

```

Try adding holiday on Sunday
Expected:

* Weekend validation error

```

---

### 4️⃣ Duplicate Validation

```

Add holiday on Jan 26
Try adding Jan 26 again
Expected:

* Duplicate error

```

---

### 5️⃣ Single Delete

```

Delete holiday
Expected:

* isActive = 'N'
* Not visible in admin list

```

---

### 6️⃣ Year-wise Delete

```

Delete 2026 holidays
Expected:

* All 2026 records marked inactive
* Not visible in list

```

---

### 7️⃣ Employee View

```

Login as EMPLOYEE
Open /employee/holidays
Expected:

* Active holidays visible

```

---

### 8️⃣ Dashboard Upcoming

```

Login as EMPLOYEE
Open dashboard
Expected:

* Future holidays listed

```

---

### 9️⃣ Authorization Test

```

EMPLOYEE tries /admin/holidays
Expected:

* 403 Access Denied

```

---

# ## PROJECT STRUCTURE

```

src/main/java/com/revature/revworkforce/
├── controller/
│   ├── HolidayController.java
│   └── EmployeeDashboardController.java
├── service/
│   ├── HolidayService.java
│   └── impl/HolidayServiceImpl.java
├── repository/
│   └── HolidayRepository.java
├── dto/
│   ├── HolidayDTO.java
│   └── HolidayStatisticsDTO.java
├── model/
│   └── Holiday.java
├── util/
│   └── DateUtil.java

```

---

# ## HOLIDAY MODULE CHECKLIST

* Entity Layer
* DTO Layer
* Repository Layer
* Service Layer
* Controller Layer
* RBAC Enforcement
* Validation Rules
* Soft Delete Logic
* Bulk Delete Logic
* Leave Integration
* Manual Testing
* Template Rendering
* Exception Handling

---

# ## HOLIDAY MODULE FEATURES

* Weekend prevention
* Duplicate prevention
* Soft delete strategy
* Year-wise bulk delete
* Upcoming holiday tracking
* Leave integration support
* Role-based access control
* Import support
* Statistics support
* Dashboard integration

---

# ## 🎯 FINAL CONCLUSION

The Holiday Management Module:

* Maintains organization-wide holiday calendar
* Prevents invalid holiday entries
* Implements soft delete for production safety
* Integrates with Leave module for accurate working-day calculation
* Enforces strict role-based security
* Supports bulk operations and reporting
* Is scalable and enterprise-ready

====================================================================
HOLIDAY MODULE READY FOR PRODUCTION
====================================================================
```

---

If you want next:

* 🎤 QC-ready explanation script (how to speak this confidently)
* 📊 Diagram version (flow diagram text)
* 🧠 Interview questions from Holiday module
* 📄 Combined Leave + Holiday integration master document

Tell me what you want next.
