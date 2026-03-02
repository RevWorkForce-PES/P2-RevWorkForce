# Admin Operations Module Documentation

The Admin Operations Module provides administrative management features including employee account control, system monitoring, and dashboard statistics.

## User Management Flow

### 1. Password Reset
- **Endpoint**: `POST /admin/reset-password/{id}`
- **Parameters**: `newPassword` (String, min 8 characters)
- **Flow**: 
  - Admin initiates password reset for a target employee.
  - The system validates the new password length (≥ 8 characters).
  - The password is encoded using `PasswordEncoder`.
  - The `firstLogin` flag is set to `'Y'`.
  - On the user's next login, they will be forced to change their password.

### 2. Lock / Unlock Account
- **Endpoint**: `POST /admin/lock-account/{id}` & `POST /admin/unlock-account/{id}`
- **Lock Flow**: Sets the user's `accountLocked` flag to `'Y'` and records a `lockedUntil` timestamp. The user is prevented from logging in.
- **Unlock Flow**: Resets the `accountLocked` flag to `'N'`, clears failed login attempts, and removes the lock timestamp.

## Bulk Activation/Deactivation Flow

### 1. Bulk Activation
- **Endpoint**: `POST /admin/api/bulk-activate`
- **Request Body**: `List<String>` of employee IDs
- **Flow**: Iterates through the provided list, updates the `EmployeeStatus` of each valid employee to `ACTIVE`, and records the `updatedAt` timestamp. Returns the count of successfully activated employees.

### 2. Bulk Deactivation
- **Endpoint**: `POST /admin/api/bulk-deactivate`
- **Request Body**: `List<String>` of employee IDs
- **Flow**: Iterates through the list, updates the `EmployeeStatus` of each valid employee to `INACTIVE`. Returns the count of successfully deactivated employees.

## System Dashboard Metrics Documentation

- **Endpoint**: `GET /admin/api/stats`
- **Metrics Collected**:
  - `totalEmployees`: Total count of employees in the database.
  - `activeEmployees`: Total count of employees with `ACTIVE` status.
  - `inactiveEmployees`: Total count of employees with `INACTIVE` status.
  - `lockedAccounts`: Count of accounts currently flagged as `accountLocked = 'Y'`.
  - `totalRoles`: Total number of roles stored in the `Role` repository.

## Database Health Check Documentation

- **Endpoint**: `GET /admin/health`
- **Flow**: Performs a simple count query on the `EmployeeRepository` to verify database connectivity.
- **Response**:
  - `status`: "UP" if successful, "DOWN" otherwise.
  - `employeeCount`: Total number of records (if UP).
  - `error`: Exception details (if DOWN).
  - `timestamp`: Current system time.

## API Documentation (Postman Setup)

### Security Requirements
All endpoints are secured and require the `ROLE_ADMIN` authority. Authentication must be provided via the existing system configured headers. CSRF protection applies to all `POST` requests.

### Example Requests

#### Reset Password
```http
POST /admin/reset-password/EMP001?newPassword=SecureNewPassword123
```

#### Lock Account
```http
POST /admin/lock-account/EMP002
```

#### Bulk Activate
```http
POST /admin/api/bulk-activate
Content-Type: application/json

[
  "EMP003",
  "EMP004"
]
```

#### Get System Statistics
```http
GET /admin/api/stats
```
**Example Response**:
```json
{
  "activeEmployees": 150,
  "inactiveEmployees": 5,
  "lockedAccounts": 2,
  "totalEmployees": 155,
  "totalRoles": 3
}
```
