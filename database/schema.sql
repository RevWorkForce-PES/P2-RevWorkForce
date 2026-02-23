-- ============================================
-- RevWorkForce Database Schema
-- Oracle 19c
-- Database: revworkforce-db
-- Password: revpassword
-- ============================================
-- Author: RevWorkForce Team
-- Created: 2026
-- Description: Complete HRM system database schema
-- Tables: 14
-- ============================================

-- ============================================
-- SECTION 1: DROP EXISTING OBJECTS
-- ============================================
-- Drop tables in reverse dependency order to avoid FK constraints errors
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE AUDIT_LOGS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE ANNOUNCEMENTS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE NOTIFICATIONS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE GOALS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE PERFORMANCE_REVIEWS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE HOLIDAYS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE LEAVE_BALANCES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE LEAVE_APPLICATIONS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE LEAVE_TYPES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE EMPLOYEE_ROLES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE EMPLOYEES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE ROLES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE DESIGNATIONS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE DEPARTMENTS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Drop sequences
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE dept_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE desig_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE role_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE leave_type_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE leave_app_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE leave_bal_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE holiday_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE review_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE goal_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE notif_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE announce_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE audit_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- ============================================
-- SECTION 2: CREATE SEQUENCES
-- ============================================

CREATE SEQUENCE dept_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE desig_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE role_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE leave_type_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE leave_app_seq START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE leave_bal_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE holiday_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE review_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE goal_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE notif_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE announce_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE audit_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ============================================
-- SECTION 3: CREATE TABLES
-- ============================================

-- ============================================
-- TABLE 1: DEPARTMENTS
-- Purpose: Store organizational departments
-- ============================================
CREATE TABLE DEPARTMENTS (
    department_id NUMBER PRIMARY KEY,
    department_name VARCHAR2(100) NOT NULL UNIQUE,
    department_head VARCHAR2(20),
    description VARCHAR2(500),
    is_active CHAR(1) DEFAULT 'Y' CHECK (is_active IN ('Y', 'N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE DEPARTMENTS IS 'Stores organizational departments';
COMMENT ON COLUMN DEPARTMENTS.department_id IS 'Primary key, auto-generated';
COMMENT ON COLUMN DEPARTMENTS.department_name IS 'Unique department name';
COMMENT ON COLUMN DEPARTMENTS.department_head IS 'Employee ID of department head';
COMMENT ON COLUMN DEPARTMENTS.is_active IS 'Y=Active, N=Inactive';

-- ============================================
-- TABLE 2: DESIGNATIONS
-- Purpose: Store job titles/positions
-- ============================================
CREATE TABLE DESIGNATIONS (
    designation_id NUMBER PRIMARY KEY,
    designation_name VARCHAR2(100) NOT NULL UNIQUE,
    designation_level VARCHAR2(20),
    min_salary NUMBER(10,2),
    max_salary NUMBER(10,2),
    description VARCHAR2(500),
    is_active CHAR(1) DEFAULT 'Y' CHECK (is_active IN ('Y', 'N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE DESIGNATIONS IS 'Stores job titles and positions';
COMMENT ON COLUMN DESIGNATIONS.designation_level IS 'Junior/Mid/Senior/Lead/Manager';
COMMENT ON COLUMN DESIGNATIONS.min_salary IS 'Minimum salary for this designation';
COMMENT ON COLUMN DESIGNATIONS.max_salary IS 'Maximum salary for this designation';

-- ============================================
-- TABLE 3: ROLES
-- Purpose: Store user roles (ADMIN, MANAGER, EMPLOYEE)
-- ============================================
CREATE TABLE ROLES (
    role_id NUMBER PRIMARY KEY,
    role_name VARCHAR2(50) NOT NULL UNIQUE,
    role_description VARCHAR2(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE ROLES IS 'Stores user roles for access control';
COMMENT ON COLUMN ROLES.role_name IS 'ADMIN, MANAGER, EMPLOYEE';

-- ============================================
-- TABLE 4: EMPLOYEES
-- Purpose: Store employee information
-- ============================================
CREATE TABLE EMPLOYEES (
    employee_id VARCHAR2(20) PRIMARY KEY,
    first_name VARCHAR2(50) NOT NULL,
    last_name VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    phone VARCHAR2(15),
    date_of_birth DATE,
    gender CHAR(1) CHECK (gender IN ('M', 'F', 'O')),
    address VARCHAR2(500),
    city VARCHAR2(50),
    state VARCHAR2(50),
    postal_code VARCHAR2(10),
    country VARCHAR2(50) DEFAULT 'India',
    emergency_contact_name VARCHAR2(100),
    emergency_contact_phone VARCHAR2(15),
    department_id NUMBER,
    designation_id NUMBER,
    manager_id VARCHAR2(20),
    joining_date DATE DEFAULT SYSDATE,
    leaving_date DATE,
    salary NUMBER(10,2),
    status VARCHAR2(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Inactive', 'On Leave', 'Terminated')),
    password_hash VARCHAR2(255) NOT NULL,
    first_login CHAR(1) DEFAULT 'Y' CHECK (first_login IN ('Y', 'N')),
    last_login TIMESTAMP,
    failed_login_attempts NUMBER DEFAULT 0,
    account_locked CHAR(1) DEFAULT 'N' CHECK (account_locked IN ('Y', 'N')),
    locked_until TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR2(20),
    updated_by VARCHAR2(20),
    CONSTRAINT fk_emp_dept FOREIGN KEY (department_id) REFERENCES DEPARTMENTS(department_id),
    CONSTRAINT fk_emp_desig FOREIGN KEY (designation_id) REFERENCES DESIGNATIONS(designation_id),
    CONSTRAINT fk_emp_mgr FOREIGN KEY (manager_id) REFERENCES EMPLOYEES(employee_id),
    CONSTRAINT chk_salary_positive CHECK (salary > 0),
    CONSTRAINT chk_joining_before_leaving CHECK (leaving_date IS NULL OR leaving_date >= joining_date)
);

COMMENT ON TABLE EMPLOYEES IS 'Stores all employee information';
COMMENT ON COLUMN EMPLOYEES.employee_id IS 'Unique employee ID (format: EMPXXX, MGRXXX, ADMXXX)';
COMMENT ON COLUMN EMPLOYEES.password_hash IS 'BCrypt hashed password (12 rounds)';
COMMENT ON COLUMN EMPLOYEES.first_login IS 'Y if user needs to change password on first login';
COMMENT ON COLUMN EMPLOYEES.failed_login_attempts IS 'Counter for failed login attempts';
COMMENT ON COLUMN EMPLOYEES.account_locked IS 'Y if account is locked due to failed attempts';

-- Create indexes for EMPLOYEES table
CREATE INDEX idx_emp_email ON EMPLOYEES(email);
CREATE INDEX idx_emp_dept ON EMPLOYEES(department_id);
CREATE INDEX idx_emp_mgr ON EMPLOYEES(manager_id);
CREATE INDEX idx_emp_status ON EMPLOYEES(status);
CREATE INDEX idx_emp_name ON EMPLOYEES(first_name, last_name);

-- ============================================
-- TABLE 5: EMPLOYEE_ROLES
-- Purpose: Many-to-many relationship between employees and roles
-- ============================================
CREATE TABLE EMPLOYEE_ROLES (
    employee_id VARCHAR2(20) NOT NULL,
    role_id NUMBER NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR2(20),
    PRIMARY KEY (employee_id, role_id),
    CONSTRAINT fk_emprole_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_emprole_role FOREIGN KEY (role_id) REFERENCES ROLES(role_id)
);

COMMENT ON TABLE EMPLOYEE_ROLES IS 'Maps employees to their roles (many-to-many)';

-- ============================================
-- TABLE 6: LEAVE_TYPES
-- Purpose: Store different types of leave
-- ============================================
CREATE TABLE LEAVE_TYPES (
    leave_type_id NUMBER PRIMARY KEY,
    leave_code VARCHAR2(10) NOT NULL UNIQUE,
    leave_name VARCHAR2(50) NOT NULL,
    default_days NUMBER NOT NULL,
    max_carry_forward NUMBER DEFAULT 0,
    description VARCHAR2(500),
    requires_approval CHAR(1) DEFAULT 'Y' CHECK (requires_approval IN ('Y', 'N')),
    is_active CHAR(1) DEFAULT 'Y' CHECK (is_active IN ('Y', 'N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_default_days_positive CHECK (default_days >= 0),
    CONSTRAINT chk_carry_forward_valid CHECK (max_carry_forward >= 0)
);

COMMENT ON TABLE LEAVE_TYPES IS 'Stores different types of leave (CL, SL, PL, etc.)';
COMMENT ON COLUMN LEAVE_TYPES.leave_code IS 'Short code (CL, SL, PL, PRIV)';
COMMENT ON COLUMN LEAVE_TYPES.default_days IS 'Default annual quota for this leave type';
COMMENT ON COLUMN LEAVE_TYPES.max_carry_forward IS 'Maximum days that can be carried forward to next year';

-- ============================================
-- TABLE 7: LEAVE_APPLICATIONS
-- Purpose: Store leave requests
-- ============================================
CREATE TABLE LEAVE_APPLICATIONS (
    application_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL,
    leave_type_id NUMBER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days NUMBER NOT NULL,
    reason VARCHAR2(500) NOT NULL,
    status VARCHAR2(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    applied_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by VARCHAR2(20),
    approved_on TIMESTAMP,
    rejection_reason VARCHAR2(500),
    comments VARCHAR2(500),
    CONSTRAINT fk_leave_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_leave_type FOREIGN KEY (leave_type_id) REFERENCES LEAVE_TYPES(leave_type_id),
    CONSTRAINT fk_leave_approver FOREIGN KEY (approved_by) REFERENCES EMPLOYEES(employee_id),
    CONSTRAINT chk_leave_dates CHECK (end_date >= start_date),
    CONSTRAINT chk_leave_days CHECK (total_days > 0),
    CONSTRAINT chk_leave_reason_length CHECK (LENGTH(reason) >= 10)
);

COMMENT ON TABLE LEAVE_APPLICATIONS IS 'Stores all leave applications';
COMMENT ON COLUMN LEAVE_APPLICATIONS.total_days IS 'Number of working days (calculated, excludes weekends and holidays)';
COMMENT ON COLUMN LEAVE_APPLICATIONS.reason IS 'Reason for leave (minimum 10 characters)';
COMMENT ON COLUMN LEAVE_APPLICATIONS.rejection_reason IS 'Reason for rejection (mandatory if status=REJECTED)';

-- Create indexes for LEAVE_APPLICATIONS table
CREATE INDEX idx_leave_emp ON LEAVE_APPLICATIONS(employee_id);
CREATE INDEX idx_leave_status ON LEAVE_APPLICATIONS(status);
CREATE INDEX idx_leave_dates ON LEAVE_APPLICATIONS(start_date, end_date);
CREATE INDEX idx_leave_approver ON LEAVE_APPLICATIONS(approved_by);

-- ============================================
-- TABLE 8: LEAVE_BALANCES
-- Purpose: Track leave balances for each employee
-- ============================================
CREATE TABLE LEAVE_BALANCES (
    balance_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL,
    leave_type_id NUMBER NOT NULL,
    year NUMBER(4) NOT NULL,
    total_allocated NUMBER DEFAULT 0,
    used NUMBER DEFAULT 0,
    balance NUMBER DEFAULT 0,
    carried_forward NUMBER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_balance_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_balance_type FOREIGN KEY (leave_type_id) REFERENCES LEAVE_TYPES(leave_type_id),
    CONSTRAINT uk_emp_leave_year UNIQUE (employee_id, leave_type_id, year),
    CONSTRAINT chk_balance_valid CHECK (balance >= 0),
    CONSTRAINT chk_used_valid CHECK (used >= 0),
    CONSTRAINT chk_balance_calculation CHECK (balance = (total_allocated + carried_forward - used))
);

COMMENT ON TABLE LEAVE_BALANCES IS 'Tracks leave balances per employee per year';
COMMENT ON COLUMN LEAVE_BALANCES.total_allocated IS 'Total leaves allocated for the year';
COMMENT ON COLUMN LEAVE_BALANCES.used IS 'Number of leaves used';
COMMENT ON COLUMN LEAVE_BALANCES.balance IS 'Remaining balance (allocated + carried_forward - used)';
COMMENT ON COLUMN LEAVE_BALANCES.carried_forward IS 'Leaves carried forward from previous year';

-- Create indexes for LEAVE_BALANCES table
CREATE INDEX idx_balance_emp ON LEAVE_BALANCES(employee_id);
CREATE INDEX idx_balance_year ON LEAVE_BALANCES(year);

-- ============================================
-- TABLE 9: HOLIDAYS
-- Purpose: Store company holidays
-- ============================================
CREATE TABLE HOLIDAYS (
    holiday_id NUMBER PRIMARY KEY,
    holiday_date DATE NOT NULL UNIQUE,
    holiday_name VARCHAR2(100) NOT NULL,
    holiday_type VARCHAR2(50) DEFAULT 'National',
    is_optional CHAR(1) DEFAULT 'N' CHECK (is_optional IN ('Y', 'N')),
    description VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR2(20)
);

COMMENT ON TABLE HOLIDAYS IS 'Stores company holidays for working days calculation';
COMMENT ON COLUMN HOLIDAYS.holiday_type IS 'National/Regional/Festival/Company-specific';
COMMENT ON COLUMN HOLIDAYS.is_optional IS 'Y if employees can choose to work';

-- Create index for HOLIDAYS table
CREATE INDEX idx_holiday_date ON HOLIDAYS(holiday_date);

-- ============================================
-- TABLE 10: PERFORMANCE_REVIEWS
-- Purpose: Store annual performance reviews
-- ============================================
CREATE TABLE PERFORMANCE_REVIEWS (
    review_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL,
    review_year NUMBER(4) NOT NULL,
    review_period VARCHAR2(50),
    key_deliverables CLOB,
    major_accomplishments CLOB,
    areas_of_improvement CLOB,
    self_assessment_rating NUMBER(2,1),
    self_assessment_comments VARCHAR2(1000),
    manager_feedback CLOB,
    manager_rating NUMBER(2,1),
    manager_comments VARCHAR2(1000),
    final_rating NUMBER(2,1),
    status VARCHAR2(20) DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'SUBMITTED', 'REVIEWED', 'COMPLETED')),
    submitted_date DATE,
    reviewed_by VARCHAR2(20),
    reviewed_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_review_mgr FOREIGN KEY (reviewed_by) REFERENCES EMPLOYEES(employee_id),
    CONSTRAINT chk_self_rating CHECK (self_assessment_rating IS NULL OR (self_assessment_rating BETWEEN 1.0 AND 5.0)),
    CONSTRAINT chk_mgr_rating CHECK (manager_rating IS NULL OR (manager_rating BETWEEN 1.0 AND 5.0)),
    CONSTRAINT chk_final_rating CHECK (final_rating IS NULL OR (final_rating BETWEEN 1.0 AND 5.0)),
    CONSTRAINT uk_emp_review_year UNIQUE (employee_id, review_year)
);

COMMENT ON TABLE PERFORMANCE_REVIEWS IS 'Stores annual performance reviews';
COMMENT ON COLUMN PERFORMANCE_REVIEWS.review_period IS 'e.g., Q1-2024, H1-2024, Annual-2024';
COMMENT ON COLUMN PERFORMANCE_REVIEWS.self_assessment_rating IS 'Employee rating (1.0 to 5.0)';
COMMENT ON COLUMN PERFORMANCE_REVIEWS.manager_rating IS 'Manager rating (1.0 to 5.0)';
COMMENT ON COLUMN PERFORMANCE_REVIEWS.final_rating IS 'Final rating (calculated or override)';

-- Create indexes for PERFORMANCE_REVIEWS table
CREATE INDEX idx_review_emp ON PERFORMANCE_REVIEWS(employee_id);
CREATE INDEX idx_review_year ON PERFORMANCE_REVIEWS(review_year);
CREATE INDEX idx_review_status ON PERFORMANCE_REVIEWS(status);

-- ============================================
-- TABLE 11: GOALS
-- Purpose: Store employee goals
-- ============================================
CREATE TABLE GOALS (
    goal_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL,
    goal_title VARCHAR2(200) NOT NULL,
    goal_description VARCHAR2(1000) NOT NULL,
    category VARCHAR2(50),
    deadline DATE,
    priority VARCHAR2(20) CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    progress NUMBER DEFAULT 0,
    status VARCHAR2(20) DEFAULT 'NOT_STARTED' CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'DEFERRED')),
    manager_comments VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_goal_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE,
    CONSTRAINT chk_progress CHECK (progress BETWEEN 0 AND 100),
    CONSTRAINT chk_goal_deadline CHECK (deadline IS NULL OR deadline >= TRUNC(SYSDATE))
);

COMMENT ON TABLE GOALS IS 'Stores employee goals and objectives';
COMMENT ON COLUMN GOALS.category IS 'Technical/Professional Development/Business/Personal';
COMMENT ON COLUMN GOALS.progress IS 'Percentage completion (0-100)';
COMMENT ON COLUMN GOALS.priority IS 'Goal priority level';

-- Create indexes for GOALS table
CREATE INDEX idx_goal_emp ON GOALS(employee_id);
CREATE INDEX idx_goal_status ON GOALS(status);
CREATE INDEX idx_goal_deadline ON GOALS(deadline);

-- ============================================
-- TABLE 12: NOTIFICATIONS
-- Purpose: Store in-app notifications
-- ============================================
CREATE TABLE NOTIFICATIONS (
    notification_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20) NOT NULL,
    notification_type VARCHAR2(50),
    title VARCHAR2(200) NOT NULL,
    message VARCHAR2(1000) NOT NULL,
    reference_type VARCHAR2(50),
    reference_id NUMBER,
    is_read CHAR(1) DEFAULT 'N' CHECK (is_read IN ('Y', 'N')),
    read_at TIMESTAMP,
    priority VARCHAR2(20) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    CONSTRAINT fk_notif_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id) ON DELETE CASCADE
);

COMMENT ON TABLE NOTIFICATIONS IS 'Stores in-app notifications for users';
COMMENT ON COLUMN NOTIFICATIONS.notification_type IS 'LEAVE, PERFORMANCE, GOAL, ANNOUNCEMENT, SYSTEM';
COMMENT ON COLUMN NOTIFICATIONS.reference_type IS 'Type of referenced object (LEAVE_APP, PERFORMANCE_REVIEW, etc.)';
COMMENT ON COLUMN NOTIFICATIONS.reference_id IS 'ID of referenced object';
COMMENT ON COLUMN NOTIFICATIONS.expires_at IS 'Notification expiry (auto-delete after 90 days)';

-- Create indexes for NOTIFICATIONS table
CREATE INDEX idx_notif_emp ON NOTIFICATIONS(employee_id);
CREATE INDEX idx_notif_read ON NOTIFICATIONS(is_read);
CREATE INDEX idx_notif_created ON NOTIFICATIONS(created_at);

-- ============================================
-- TABLE 13: ANNOUNCEMENTS
-- Purpose: Store company-wide announcements
-- ============================================
CREATE TABLE ANNOUNCEMENTS (
    announcement_id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    message CLOB NOT NULL,
    announcement_type VARCHAR2(50) DEFAULT 'General',
    priority VARCHAR2(20) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    target_audience VARCHAR2(50) DEFAULT 'ALL',
    is_active CHAR(1) DEFAULT 'Y' CHECK (is_active IN ('Y', 'N')),
    publish_date DATE DEFAULT SYSDATE,
    expiry_date DATE,
    created_by VARCHAR2(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_announce_creator FOREIGN KEY (created_by) REFERENCES EMPLOYEES(employee_id)
);

COMMENT ON TABLE ANNOUNCEMENTS IS 'Stores company-wide announcements';
COMMENT ON COLUMN ANNOUNCEMENTS.announcement_type IS 'General/Policy/Event/Emergency/Celebration';
COMMENT ON COLUMN ANNOUNCEMENTS.target_audience IS 'ALL/DEPARTMENT/DESIGNATION/SPECIFIC';

-- Create index for ANNOUNCEMENTS table
CREATE INDEX idx_announce_active ON ANNOUNCEMENTS(is_active, publish_date);

-- ============================================
-- TABLE 14: AUDIT_LOGS
-- Purpose: Store audit trail of all system changes
-- ============================================
CREATE TABLE AUDIT_LOGS (
    audit_id NUMBER PRIMARY KEY,
    employee_id VARCHAR2(20),
    action VARCHAR2(50) NOT NULL,
    table_name VARCHAR2(50) NOT NULL,
    record_id VARCHAR2(50),
    field_name VARCHAR2(100),
    old_value CLOB,
    new_value CLOB,
    description VARCHAR2(500),
    ip_address VARCHAR2(45),
    user_agent VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_emp FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id)
);

COMMENT ON TABLE AUDIT_LOGS IS 'Immutable audit trail of all system changes';
COMMENT ON COLUMN AUDIT_LOGS.action IS 'INSERT/UPDATE/DELETE/LOGIN/LOGOUT/APPROVE/REJECT';
COMMENT ON COLUMN AUDIT_LOGS.table_name IS 'Name of the table affected';
COMMENT ON COLUMN AUDIT_LOGS.record_id IS 'ID of the record affected';

-- Create indexes for AUDIT_LOGS table
CREATE INDEX idx_audit_emp ON AUDIT_LOGS(employee_id);
CREATE INDEX idx_audit_table ON AUDIT_LOGS(table_name);
CREATE INDEX idx_audit_action ON AUDIT_LOGS(action);
CREATE INDEX idx_audit_created ON AUDIT_LOGS(created_at);

-- ============================================
-- SECTION 4: CREATE TRIGGERS
-- ============================================

-- Trigger to update UPDATED_AT timestamp on EMPLOYEES table
CREATE OR REPLACE TRIGGER trg_employees_updated
BEFORE UPDATE ON EMPLOYEES
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Trigger to update UPDATED_AT timestamp on DEPARTMENTS table
CREATE OR REPLACE TRIGGER trg_departments_updated
BEFORE UPDATE ON DEPARTMENTS
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Trigger to update UPDATED_AT timestamp on LEAVE_BALANCES table
CREATE OR REPLACE TRIGGER trg_leave_balances_updated
BEFORE UPDATE ON LEAVE_BALANCES
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Trigger to auto-calculate final rating in PERFORMANCE_REVIEWS
CREATE OR REPLACE TRIGGER trg_calculate_final_rating
BEFORE UPDATE ON PERFORMANCE_REVIEWS
FOR EACH ROW
BEGIN
    IF :NEW.self_assessment_rating IS NOT NULL AND :NEW.manager_rating IS NOT NULL THEN
        :NEW.final_rating := ROUND((:NEW.self_assessment_rating + :NEW.manager_rating) / 2, 1);
    END IF;
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Trigger to auto-complete goal when progress reaches 100%
CREATE OR REPLACE TRIGGER trg_goal_auto_complete
BEFORE UPDATE ON GOALS
FOR EACH ROW
BEGIN
    IF :NEW.progress >= 100 AND :OLD.progress < 100 THEN
        :NEW.status := 'COMPLETED';
        :NEW.completed_at := CURRENT_TIMESTAMP;
    END IF;
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- ============================================
-- SECTION 5: GRANT PERMISSIONS
-- ============================================

-- Grant all privileges on tables and sequences to current user
GRANT ALL ON DEPARTMENTS TO PUBLIC;
GRANT ALL ON DESIGNATIONS TO PUBLIC;
GRANT ALL ON ROLES TO PUBLIC;
GRANT ALL ON EMPLOYEES TO PUBLIC;
GRANT ALL ON EMPLOYEE_ROLES TO PUBLIC;
GRANT ALL ON LEAVE_TYPES TO PUBLIC;
GRANT ALL ON LEAVE_APPLICATIONS TO PUBLIC;
GRANT ALL ON LEAVE_BALANCES TO PUBLIC;
GRANT ALL ON HOLIDAYS TO PUBLIC;
GRANT ALL ON PERFORMANCE_REVIEWS TO PUBLIC;
GRANT ALL ON GOALS TO PUBLIC;
GRANT ALL ON NOTIFICATIONS TO PUBLIC;
GRANT ALL ON ANNOUNCEMENTS TO PUBLIC;
GRANT ALL ON AUDIT_LOGS TO PUBLIC;

GRANT ALL ON dept_seq TO PUBLIC;
GRANT ALL ON desig_seq TO PUBLIC;
GRANT ALL ON role_seq TO PUBLIC;
GRANT ALL ON leave_type_seq TO PUBLIC;
GRANT ALL ON leave_app_seq TO PUBLIC;
GRANT ALL ON leave_bal_seq TO PUBLIC;
GRANT ALL ON holiday_seq TO PUBLIC;
GRANT ALL ON review_seq TO PUBLIC;
GRANT ALL ON goal_seq TO PUBLIC;
GRANT ALL ON notif_seq TO PUBLIC;
GRANT ALL ON announce_seq TO PUBLIC;
GRANT ALL ON audit_seq TO PUBLIC;

-- ============================================
-- SECTION 6: VERIFICATION QUERIES
-- ============================================

-- Verify all tables created
SELECT table_name 
FROM user_tables 
ORDER BY table_name;

-- Verify all sequences created
SELECT sequence_name 
FROM user_sequences 
ORDER BY sequence_name;

-- Verify all constraints
SELECT constraint_name, constraint_type, table_name 
FROM user_constraints 
WHERE table_name IN (
    'DEPARTMENTS', 'DESIGNATIONS', 'ROLES', 'EMPLOYEES', 
    'EMPLOYEE_ROLES', 'LEAVE_TYPES', 'LEAVE_APPLICATIONS', 
    'LEAVE_BALANCES', 'HOLIDAYS', 'PERFORMANCE_REVIEWS', 
    'GOALS', 'NOTIFICATIONS', 'ANNOUNCEMENTS', 'AUDIT_LOGS'
)
ORDER BY table_name, constraint_name;

-- Display summary
SELECT 'Schema creation completed successfully!' AS STATUS FROM DUAL;
SELECT COUNT(*) AS TOTAL_TABLES FROM user_tables;
SELECT COUNT(*) AS TOTAL_SEQUENCES FROM user_sequences;
SELECT COUNT(*) AS TOTAL_INDEXES FROM user_indexes WHERE table_owner = USER;

COMMIT;

-- ============================================
-- END OF SCHEMA SCRIPT
-- ============================================