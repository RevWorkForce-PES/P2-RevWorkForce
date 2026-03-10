# Department & Designation Module Documentation

## 1. Overview
The Department and Designation modules are core components of the RevWorkForce application that manage the organizational structure. They allow administrators to define and maintain the various departments within the company and the specific job roles (designations) available within those departments.

These modules are tightly integrated with Employee Management, as every employee is typically assigned to a Department and holds a Designation.

## 2. Architecture & Tech Stack
*   **Backend:** Spring Boot, Spring Web MVC (Controllers), Spring Data JPA (Repositories)
*   **Frontend:** Thymeleaf (Server-side rendering), HTML, CSS, JavaScript
*   **Security:** Spring Security (End-points are protected via `@PreAuthorize("hasRole('ADMIN')")`)
*   **Database:** Configured via Spring Data JPA (Entities map to database tables)

## 3. Data Models (Entities & DTOs)

### 3.1 Department
*   **Entity (`Department.java`):** Represents the `departments` table in the database.
*   **DTO (`DepartmentDTO.java`):** Used for data transfer between the client and server.
    *   `departmentId` (Long): Unique identifier.
    *   `departmentName` (String): Name of the department (e.g., "Engineering", "HR").
    *   `description` (String): Brief description of the department's function.
    *   `isActive` (Character): Status ('Y' for Active, 'N' for Inactive).
    *   `employeeCount` (Integer): *Calculated field* showing the number of active employees currently assigned to this department.

### 3.2 Designation
*   **Entity (`Designation.java`):** Represents the `designations` table in the database.
*   **DTO (`DesignationDTO.java`):** Used for data transfer.
    *   `designationId` (Long): Unique identifier.
    *   `designationName` (String): Name of the role (e.g., "Software Engineer", "Sales Manager").
    *   `description` (String): Brief description of the role's responsibilities.
    *   `departmentId` (Long): The ID of the department this designation belongs to (Optional/depending on structural rules).
    *   `level` (Integer): Hierarchy level of the designation.
    *   `isActive` (Character): Status ('Y' for Active, 'N' for Inactive).
    *   `employeeCount` (Integer): *Calculated field* showing the number of active employees holding this designation.

## 4. Service Layer Business Logic

The Service layer (`DepartmentServiceImpl` and `DesignationServiceImpl`) handles the core logic before saving to or retrieving from the database.

*   **Employee Count Calculation:** When retrieving lists of departments or designations, the service layer actively queries the `EmployeeRepository` (`countByDepartment` and `countByDesignation`) to dynamically calculate and populate the `employeeCount` field in the respective DTOs.
*   **Validation:** Ensuring names are unique or valid before creation/updates.
*   **Mapping:** Converting between Database Entities and Data Transfer Objects (DTOs) using tools like ModelMapper or manual mapping logic.

## 5. Controller Endpoints (Spring MVC)

Because this application uses Thymeleaf for server-side rendering, these endpoints return **HTML Views** rather than JSON payloads. All endpoints listed below require the user to be authenticated and hold the `ADMIN` role.

### 5.1 Department Controller (`/admin/departments`)
| HTTP Method | Endpoint | Description | View Returned |
| :--- | :--- | :--- | :--- |
| `GET` | `/admin/departments` | Lists all departments. | `admin/departments/list.html` |
| `GET` | `/admin/departments/system-config` | Aggregated view of Departments & Designations. | `frontend/pages/admin/system-config.html` |
| `GET` | `/admin/departments/add` | Shows the form to create a new department. | `admin/departments/form.html` |
| `POST` | `/admin/departments/add` | Submits form data to create a department. | *Redirects to `/admin/departments`* |
| `GET` | `/admin/departments/edit/{id}` | Shows the form to edit an existing department. | `admin/departments/form.html` |
| `POST` | `/admin/departments/edit/{id}` | Submits changes for an existing department. | *Redirects to `/admin/departments`* |
| `POST` | `/admin/departments/delete/{id}` | Deactivates or removes a department. | *Redirects to `/admin/departments`* |

### 5.2 Designation Controller (`/admin/designations`)
| HTTP Method | Endpoint | Description | View Returned |
| :--- | :--- | :--- | :--- |
| `GET` | `/admin/designations` | Lists all designations. | `admin/designations/list.html` |
| `GET` | `/admin/designations/add` | Shows the form to create a new designation. | `admin/designations/form.html` |
| `POST` | `/admin/designations/add` | Submits form data to create a designation. | *Redirects to `/admin/designations`* |
| `GET` | `/admin/designations/edit/{id}` | Shows the form to edit an existing designation. | `admin/designations/form.html` |
| `POST` | `/admin/designations/edit/{id}` | Submits changes for an existing designation. | *Redirects to `/admin/designations`* |
| `POST` | `/admin/designations/delete/{id}`| Deactivates or removes a designation. | *Redirects to `/admin/designations`* |

## 6. Frontend Integration Points
The data from these modules is dynamically injected into several frontend views using Thymeleaf `th:each` loops:

1.  **System Configuration (`system-config.html`):** Displays comprehensive datatables for both models.
2.  **Admin Dashboard (`dashboard.html`):** Displays the total count of active departments as a top-level metric.
3.  **Employee Management (`employee-management.html`):**
    *   Populates the "Department" column in the employee list.
    *   Populates the "Department" and "Designation" `<select>` dropdown menus in the "Add Employee" form.
4.  **Profile Directory (`profile-directory.html`):**
    *   Populates the "All Departments" search filter `<select>` menu.
    *   Displays the department and designation names on the individual Colleague Cards.

## 7. Testing Notes
Because these are Spring MVC endpoints secured by Spring Security, they rely on `JSESSIONID` cookies and CSRF tokens.

*   **Recommended Testing:** Manual UI testing through the browser is the most reliable method for ensuring forms submit correctly, data binds to the Thymeleaf model, and redirects function as intended.
*   **API Client Testing (Postman/Curl):** To test via Postman, you must first authenticate to retrieve the session cookie, extract the CSRF token from the login page or a subsequent response, and include both the cookie and the `X-CSRF-TOKEN` header in your `application/x-www-form-urlencoded` POST requests.
