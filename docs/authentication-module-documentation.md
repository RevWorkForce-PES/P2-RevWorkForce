# AUTHENTICATION MODULE - COMPLETE IMPLEMENTATION

## AUTHENTICATION MODULE Documentations

### 1. DTO CLASSES (2 files)
- **LoginRequest.java** - Login form data transfer
- **ChangePasswordRequest.java** - Password change with validation

### 2. SECURITY COMPONENTS (4 files)
- **CustomUserDetailsService.java** - Load user from database for Spring Security
- **CustomAuthenticationSuccessHandler.java** - Role-based redirect after login
- **UserPrincipal.java** - Represents authenticated user with authorities
- **SecurityUtils.java** - Helper methods for security operations

### 3. SERVICE LAYER (1 file)
- **AuthService.java** - Authentication business logic (password change, login tracking, account locking)

### 4. CONTROLLER LAYER (5 files)
- **AuthController.java** - Login, logout, change password endpoints
- **DashboardController.java** - Main dashboard with role-based routing
- **AdminDashboardController.java** - Admin dashboard
- **ManagerDashboardController.java** - Manager dashboard
- **EmployeeDashboardController.java** - Employee dashboard


## AUTHENTICATION FLOW

### LOGIN FLOW:
```
1. User enters username (email or employeeId) + password
   ↓
2. Spring Security intercepts /login POST request
   ↓
3. CustomUserDetailsService.loadUserByUsername() is called
   ↓
4. Load employee from database (findByEmailOrEmployeeId)
   ↓
5. Check account status (active, not locked)
   ↓
6. Load user roles (ADMIN, MANAGER, EMPLOYEE)
   ↓
7. Spring Security verifies password using BCrypt
   ↓
8. Password correct? → Authentication successful
   ↓
9. CustomAuthenticationSuccessHandler determines redirect URL
   ↓
10. Redirect to role-specific dashboard:
    - ADMIN → /admin/dashboard
    - MANAGER → /manager/dashboard
    - EMPLOYEE → /employee/dashboard
   ↓
11. If first login → Redirect to /change-password
```

### LOGOUT FLOW:
```
1. User clicks logout
   ↓
2. GET /logout endpoint called
   ↓
3. SecurityContextLogoutHandler clears security context
   ↓
4. Invalidate HTTP session
   ↓
5. Delete cookies (JSESSIONID, remember-me)
   ↓
6. Redirect to /login?logout=true
```

## SECURITY FEATURES IMPLEMENTED

### Account Security:
- **BCrypt Password Hashing** (12 rounds)
- **Account Locking** (after 5 failed attempts)
- **Auto-unlock** (after 15 minutes)
- **Failed Login Tracking**
- **Last Login Timestamp**

### Password Security:
- **Minimum 8 characters**
- **Must contain: uppercase, lowercase, digit**
- **First-time password change required**
- **Password confirmation validation**

### Session Security:
- **30-minute session timeout**
- **1 session per user** (new login invalidates old)
- **HttpOnly cookies** (prevent XSS)
- **CSRF protection**
- **Remember me** (24 hours)

### Role-Based Access Control:
- **ADMIN** - Full system access
- **MANAGER** - Team management + employee access
- **EMPLOYEE** - Self-service access
- **Method-level security** (@PreAuthorize annotations)


## ENDPOINTS & ACCESS CONTROL

### PUBLIC ENDPOINTS:
```
GET  /                    → Redirect to /login
GET  /login               → Login page
POST /login               → Process login (Spring Security)
GET  /logout              → Logout
GET  /css/**, /js/**      → Static resources
```

### AUTHENTICATED ENDPOINTS:
```
GET  /dashboard           → Role-based redirect
GET  /change-password     → Change password page
POST /change-password     → Process password change
```

### ADMIN ENDPOINTS (ROLE_ADMIN):
```
GET  /admin/dashboard     → Admin dashboard
```

### MANAGER ENDPOINTS (ROLE_MANAGER, ROLE_ADMIN):
```
GET  /manager/dashboard   → Manager dashboard
```

### EMPLOYEE ENDPOINTS (ALL AUTHENTICATED):
```
GET  /employee/dashboard  → Employee dashboard
```

### ERROR PAGES:
```
GET  /error/403           → Access Denied
GET  /error/404           → Not Found
GET  /error/500           → Internal Server Error
```

## DATABASE INTEGRATION

### Tables Used:
- **EMPLOYEES** - User authentication data
- **ROLES** - System roles (ADMIN, MANAGER, EMPLOYEE)
- **EMPLOYEE_ROLES** - User-role mapping

### Authentication Fields in EMPLOYEES:
```sql
employee_id VARCHAR2(20)           -- Username (primary)
email VARCHAR2(100)                -- Alternate username
password_hash VARCHAR2(255)        -- BCrypt hash
first_login CHAR(1)                -- 'Y' or 'N'
last_login TIMESTAMP               -- Last successful login
failed_login_attempts NUMBER       -- Failed attempt counter
account_locked CHAR(1)             -- 'Y' or 'N'
locked_until TIMESTAMP             -- Auto-unlock time
status VARCHAR2(20)                -- ACTIVE, INACTIVE, etc.
```

## TESTING THE AUTHENTICATION

### Test Users (from seed data):
```
Admin User:
- Username: ADM001 or rajesh.kumar@revature.com
- Password: password123
- Role: ADMIN
- Expected Redirect: /admin/dashboard

Manager User:
- Username: MGR001 or priya.sharma@revature.com
- Password: password123
- Role: MANAGER
- Expected Redirect: /manager/dashboard

Employee User:
- Username: EMP001 or mastan.sayyad@revature.com
- Password: password123
- Role: EMPLOYEE
- Expected Redirect: /employee/dashboard
```

### Test Scenarios:

#### 1. Successful Login:
```
1. Navigate to http://localhost:8080/login
2. Enter username: ADM001
3. Enter password: password123
4. Click Login
5. Expected: Redirect to /admin/dashboard
```

#### 2. Invalid Credentials:
```
1. Enter username: ADM001
2. Enter password: wrongpassword
3. Expected: Error message "Invalid username or password"
4. Failed attempt counter increased
```

#### 3. Account Locking:
```
1. Enter wrong password 5 times
2. Expected: Account locked for 15 minutes
3. Error: "Account is locked until [timestamp]"
4. After 15 minutes: Auto-unlock
```

#### 4. First-Time Login:
```
1. Login with new user credentials
2. Expected: Redirect to /change-password
3. Must change password before accessing dashboard
```

#### 5. Change Password:
```
1. Navigate to /change-password
2. Enter current password
3. Enter new password (min 8 chars, uppercase, lowercase, digit)
4. Confirm new password
5. Expected: Success message, redirect to dashboard
```

#### 6. Session Timeout:
```
1. Login successfully
2. Wait 30 minutes (no activity)
3. Try to access any page
4. Expected: Redirect to /login?expired=true
```

#### 7. Logout:
```
1. Click Logout button
2. Expected: Redirect to /login?logout=true
3. Session invalidated
4. Cannot access protected pages
```

#### 8. Remember Me:
```
1. Login with "Remember Me" checkbox
2. Close browser
3. Reopen browser and navigate to site
4. Expected: Still logged in (for 24 hours)
```

## PROJECT STRUCTURE

```
src/main/java/com/revature/revworkforce/
├── dto/
│   ├── LoginRequest.java 
│   └── ChangePasswordRequest.java 
├── security/
│   ├── CustomUserDetailsService.java 
│   ├── CustomAuthenticationSuccessHandler.java 
│   ├── UserPrincipal.java 
│   └── SecurityUtils.java 
├── service/
│   └── AuthService.java 
└── controller/
    ├── AuthController.java 
    ├── DashboardController.java 
    ├── AdminDashboardController.java 
    ├── ManagerDashboardController.java 
    └── EmployeeDashboardController.java 
```

---

## NEXT STEPS: AUTHENTICATION UI

To complete the authentication module, we need to create Thymeleaf templates:

### Required Templates:

1. **auth/login.html** - Login page
2. **auth/change-password.html** - Change password page
3. **admin/dashboard.html** - Admin dashboard
4. **manager/dashboard.html** - Manager dashboard
5. **employee/dashboard.html** - Employee dashboard
6. **error/403.html** - Access Denied
7. **error/404.html** - Not Found
8. **error/500.html** - Server Error
9. **fragments/header.html** - Common header
10. **fragments/footer.html** - Common footer
11. **fragments/navbar.html** - Navigation bar


## AUTHENTICATION MODULE CHECKLIST

- DTOs 
- Security Components 
- Service Layer 
- Controllers 
- Configuration 
- Database Schema 
- Exception Handling


---

## AUTHENTICATION MODULE FEATURES

- **Login with Email or Employee ID**
- **BCrypt Password Hashing (12 rounds)**
- **Role-Based Dashboard Redirect**
- **Account Locking (5 failed attempts)**
- **Auto-Unlock (15 minutes)**
- **Session Management (30 min timeout)**
- **Remember Me (24 hours)**
- **First-Time Password Change**
- **Password Strength Validation**
- **Last Login Tracking**
- **Failed Login Tracking**
- **CSRF Protection**
- **Method-Level Security**
- **Role Hierarchy (ADMIN > MANAGER > EMPLOYEE)**


## 💻 CODE EXAMPLES

### Get Current User in Controller:
```java
@GetMapping("/profile")
public String profile(Model model) {
    String employeeId = SecurityUtils.getCurrentUsername();
    Employee employee = employeeRepository.findById(employeeId).orElse(null);
    model.addAttribute("employee", employee);
    return "profile";
}
```

### Check User Role:
```java
if (SecurityUtils.hasRole("ADMIN")) {
    // Admin-only logic
}

if (SecurityUtils.hasAnyRole("MANAGER", "ADMIN")) {
    // Manager or Admin logic
}
```

### Change Password:
```java
ChangePasswordRequest request = new ChangePasswordRequest();
request.setCurrentPassword("oldPass123");
request.setNewPassword("NewPass123");
request.setConfirmPassword("NewPass123");

authService.changePassword(employeeId, request);
```



