# Reports & Analytics Module Documentation

## Overview
The Reports & Analytics Module provides comprehensive administrative insights into the RevWorkForce system. It aggregates and metrics from various domains including Employees, Departments, Leave Applications, Performance Reviews, Goals, and Attendance.

## Key Features
- **Dashboard Statistics:** High-level metrics for quick overview.
- **Employee Demographics:** Breakdown of workforce by department, status, designation, gender, and average tenure.
- **Leave Analytics:** Monthly tracking, total days taken, and leave status distributions.
- **Department Metrics:** Salary averages, employee counts, and goal completion rates per department.
- **Performance Distributions:** Average ratings and distribution bands.
- **Goal Completion Rates:** Progress averages and overdue alerts.
- **Attendance Calculation:** Precise attendance rates based on active employees and approved leave overlap, factoring in company holidays and weekends.

## Architecture

### 1. Data Transfer Object (DTO)
- `ReportDTO`: A unified response structure leveraging dynamic `Map` collections to accommodate different report data schemas efficiently.

### 2. Controller Layer
- `ReportController.java`
- Exposes exclusively secured endpoints mapped under `/admin/reports/`.
- Access restricted to `ROLE_ADMIN` users via Spring Security `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`.

### 3. Service Layer
- `ReportService.java`
- Contains the core business logic.
- Implements efficient tracking to avoid loading full object models when counting or distributing by groups. Utilizes the DateUtil class for precise Working Days deduction.

### 4. Repository Layer
We extended the following repositories with native grouping/aggregation JPQL queries:
- `EmployeeRepository` (`countEmployeesByDepartment`, etc.)
- `LeaveApplicationRepository` (`countLeavesByStatus`, `sumTotalLeaveDaysByStatus`, etc.)
- `PerformanceReviewRepository` (`countRatingDistribution`, `getAverageRating`)
- `GoalRepository` (`countGoalsByStatus`, `getAverageProgress`, `countOverdueGoals`)

## API Endpoints

All endpoints are `GET` requests and require a Bearer token belonging to an ADMIN user.

| Endpoint | Method | Query Parameters | Description |
|---|---|---|---|
| `/admin/reports` | GET | None | Retrieves general Dashboard Statistics (total active employees, departments, goals, etc.) |
| `/admin/reports/employees` | GET | None | Retrieves in-depth Employee demographics and tenure. |
| `/admin/reports/leaves` | GET | `year` (optional) | Retrieves leave stats broken down by month and type. Defaults to current year if `year` is omitted. |
| `/admin/reports/departments` | GET | `departmentId` (op) | Retrieves department summary. If `departmentId` is provided, includes metrics like average salary and goal completion rate. |
| `/admin/reports/performance` | GET | `year` (optional) | Retrieves rating distributions and averages. Defaults to current year. |
| `/admin/reports/goals` | GET | None | Retrieves overall goal stats including overdue counts and completion rate. |
| `/admin/reports/attendance` | GET | `year`, `month` (op)| Calculates attendance rate dynamically using Holiday sets and approved leaves. Defaults to current year / total year if month is omitted. |

## KPI Calculations

### 1. Attendance Rate
Calculated periodically based on working days minus approved leave days taken.
- **Logic**: Iterates over start and end dates. Uses `DateUtil.calculateWorkingDays` providing actual company Holidays as exclusionary data points. Validates how many leaves overlap within that exact month/year.
- **Formula**: `((Working Days * Total Active Employees) - Total Approved Leave Days) / (Working Days * Total Active Employees) * 100`

### 2. Goal Completion Rate
Evaluates the proportion of goals marked as `COMPLETED`.
- **Formula**: `(Completed Goals / Total Goals) * 100`
- **Department Goal Completion**: Iterates strictly over goals owned by individuals inside that target department.

### 3. Average Tenure
Determined dynamically during query fetching.
- **Formula**: `AVG(Current Year - Joining Year)`

### 4. Average Salary
Stream operation per-department pulling valid decimal salaries and generating the arithmetic mean.

## Testing via Postman

1. **Authentication:** Obtain a Bearer Token for an Admin user via the auth login endpoint.
2. **Access Dashboard:** GET `http://localhost:8080/admin/reports` with the Authorization Header populated.
3. **Validating Filters:** Hit `http://localhost:8080/admin/reports/leaves?year=2024` and ensure the `filters` key in the JSON output reflects `year=2024`.
4. **Testing Security:** Acquire a token for a standard non-admin Employee. Triggering any of the above endpoints will yield a standard `403 Forbidden` JSON response governed by global security handler.
