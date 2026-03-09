-- ============================================
-- RevWorkForce Seed Data
-- Oracle 19c
-- Database: revworkforce-db
-- Password: revpassword
-- ============================================
-- Author: RevWorkForce Team
-- Created: 2026
-- Description: Seed data for testing and development
-- ============================================

SET SERVEROUTPUT ON;

BEGIN
    DBMS_OUTPUT.PUT_LINE('======================================');
    DBMS_OUTPUT.PUT_LINE('Starting RevWorkForce Seed Data Load');
    DBMS_OUTPUT.PUT_LINE('======================================');
END;
/

-- ============================================
-- SECTION 1: INSERT DEPARTMENTS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Departments...');
END;
/

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Information Technology', 'Software development, infrastructure, and IT support');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Human Resources', 'Recruitment, employee relations, and HR operations');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Finance', 'Accounting, financial planning, and auditing');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Marketing', 'Brand management, digital marketing, and customer engagement');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Operations', 'Business operations, logistics, and process management');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Sales', 'Business development and client relations');

INSERT INTO DEPARTMENTS (department_id, department_name, description) 
VALUES (dept_seq.NEXTVAL, 'Quality Assurance', 'Testing, quality control, and process improvement');

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Departments inserted: 7');
END;
/

-- ============================================
-- SECTION 2: INSERT DESIGNATIONS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Designations...');
END;
/

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Software Engineer', 'Junior', 400000, 600000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Senior Software Engineer', 'Senior', 700000, 1000000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Team Lead', 'Lead', 1000000, 1400000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Manager', 'Manager', 1200000, 1800000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Senior Manager', 'Senior Manager', 1800000, 2500000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'HR Specialist', 'Mid', 450000, 700000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Finance Analyst', 'Mid', 500000, 800000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Marketing Executive', 'Junior', 350000, 550000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'QA Engineer', 'Junior', 380000, 580000);

INSERT INTO DESIGNATIONS (designation_id, designation_name, designation_level, min_salary, max_salary) 
VALUES (desig_seq.NEXTVAL, 'Sales Executive', 'Junior', 400000, 600000);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Designations inserted: 10');
END;
/

-- ============================================
-- SECTION 3: INSERT ROLES
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Roles...');
END;
/

INSERT INTO ROLES (role_id, role_name, role_description) 
VALUES (role_seq.NEXTVAL, 'ADMIN', 'System administrator with full access');

INSERT INTO ROLES (role_id, role_name, role_description) 
VALUES (role_seq.NEXTVAL, 'MANAGER', 'Manager with team management and approval privileges');

INSERT INTO ROLES (role_id, role_name, role_description) 
VALUES (role_seq.NEXTVAL, 'EMPLOYEE', 'Regular employee with self-service access');

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Roles inserted: 3');
END;
/

-- ============================================
-- SECTION 4: INSERT EMPLOYEES
-- Password: password123 (BCrypt hash with 12 rounds)
-- Hash: $2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Employees...');
END;
/

-- Admin User
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'ADM001', 'Mastan', 'Sayyad', 'admin@revworkforce.com', '9876543210', 
    DATE '2000-02-17', 'M',
    '123 MG Road', 'Bengaluru', 'Karnataka', '560001', 'India',
    1, 5, DATE '2020-01-01', 2000000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Manager 1 (IT Department)
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'MGR001', 'Priya', 'Sharma', 'manager@revworkforce.com', '9876543211',
    DATE '1988-07-22', 'F',
    '456 Brigade Road', 'Bengaluru', 'Karnataka', '560025', 'India',
    1, 4, 'ADM001', DATE '2020-03-15', 1500000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Manager 2 (HR Department)
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'MGR002', 'Amit', 'Patel', 'amit.patel@revworkforce.com', '9876543212',
    DATE '1986-11-08', 'M',
    '789 Whitefield', 'Bengaluru', 'Karnataka', '560066', 'India',
    2, 4, 'ADM001', DATE '2020-06-01', 1400000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Employees under MGR001 (IT Department)
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP001', 'Sanjay', 'kv', 'employee1@revworkforce.com', '9876543213',
    DATE '1995-05-10', 'M',
    '101 Koramangala', 'Bengaluru', 'Karnataka', '560034', 'India',
    1, 2, 'MGR001', DATE '2022-01-15', 800000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP002', 'Gururaj', 'Shetty', 'employee2@revworkforce.com', '9876543214',
    DATE '1996-08-20', 'M',
    '202 Indiranagar', 'Bengaluru', 'Karnataka', '560038', 'India',
    1, 2, 'MGR001', DATE '2022-02-01', 750000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP003', 'Aishwarya', 'MS', 'employee3@revworkforce.com', '9876543215',
    DATE '1997-03-14', 'F',
    '303 Jayanagar', 'Bengaluru', 'Karnataka', '560041', 'India',
    1, 1, 'MGR001', DATE '2023-01-10', 550000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP004', 'Shahla', 'Nigar', 'employee4@revworkforce.com', '9876543216',
    DATE '1996-12-05', 'F',
    '404 HSR Layout', 'Bengaluru', 'Karnataka', '560102', 'India',
    1, 1, 'MGR001', DATE '2023-02-15', 520000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP005', 'Chinmayi', 'Maske', 'employee5@revworkforce.com', '9876543217',
    DATE '1998-01-28', 'F',
    '505 Marathahalli', 'Bengaluru', 'Karnataka', '560037', 'India',
    7, 9, 'MGR001', DATE '2023-03-01', 480000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP006', 'Nagarjuna', 'Ummanaboyina', 'employee6@revworkforce.com', '9876543218',
    DATE '1997-06-18', 'M',
    '606 Electronic City', 'Bengaluru', 'Karnataka', '560100', 'India',
    1, 2, 'MGR001', DATE '2022-08-01', 720000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Employees under MGR002 (HR Department)
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP007', 'Sneha', 'Reddy', 'sneha.reddy@revworkforce.com', '9876543219',
    DATE '1994-09-25', 'F',
    '707 JP Nagar', 'Bengaluru', 'Karnataka', '560078', 'India',
    2, 6, 'MGR002', DATE '2021-05-01', 600000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Finance Department
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP008', 'Vikram', 'Singh', 'vikram.singh@revworkforce.com', '9876543220',
    DATE '1990-04-12', 'M',
    '808 BTM Layout', 'Bengaluru', 'Karnataka', '560076', 'India',
    3, 7, 'ADM001', DATE '2021-08-15', 700000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Marketing Department
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP009', 'Ananya', 'Iyer', 'ananya.iyer@revworkforce.com', '9876543221',
    DATE '1998-11-30', 'F',
    '909 Sarjapur Road', 'Bengaluru', 'Karnataka', '560035', 'India',
    4, 8, 'ADM001', DATE '2023-04-01', 450000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

-- Sales Department
INSERT INTO EMPLOYEES (
    employee_id, first_name, last_name, email, phone, date_of_birth, gender,
    address, city, state, postal_code, country,
    department_id, designation_id, manager_id, joining_date, salary, status, password_hash, first_login
) VALUES (
    'EMP010', 'Karthik', 'Rao', 'karthik.rao@revworkforce.com', '9876543222',
    DATE '1993-02-17', 'M',
    '1010 Banashankari', 'Bengaluru', 'Karnataka', '560070', 'India',
    6, 10, 'ADM001', DATE '2022-09-01', 550000, 'ACTIVE',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW', 'N'
);

UPDATE employees
SET password_hash = '$2a$12$u0tDD6pKq/rNmFFHziwEWO7FEb6W0hi.OYaFie3nOC5iITIUhkk3S';

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Employees inserted: 10');
END;
/

-- ============================================
-- SECTION 5: INSERT EMPLOYEE_ROLES
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Employee Roles...');
END;
/

-- Admin role
INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('ADM001', 1, 'ADM001'); -- ADMIN

-- Manager roles
INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('MGR001', 2, 'ADM001'); -- MANAGER

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('MGR002', 2, 'ADM001'); -- MANAGER

-- Employee roles
INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP001', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP002', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP003', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP004', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP005', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP006', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP007', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP008', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP009', 3, 'ADM001'); -- EMPLOYEE

INSERT INTO EMPLOYEE_ROLES (employee_id, role_id, assigned_by) 
VALUES ('EMP010', 3, 'ADM001'); -- EMPLOYEE

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Employee Roles assigned: 13');
END;
/

-- ============================================
-- SECTION 6: INSERT LEAVE_TYPES
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Leave Types...');
END;
/

INSERT INTO LEAVE_TYPES (leave_type_id, leave_code, leave_name, default_days, max_carry_forward, description) 
VALUES (leave_type_seq.NEXTVAL, 'CL', 'Casual Leave', 12, 5, 'Short-term leaves for personal reasons');

INSERT INTO LEAVE_TYPES (leave_type_id, leave_code, leave_name, default_days, max_carry_forward, description) 
VALUES (leave_type_seq.NEXTVAL, 'SL', 'Sick Leave', 12, 5, 'Medical reasons and health issues');

INSERT INTO LEAVE_TYPES (leave_type_id, leave_code, leave_name, default_days, max_carry_forward, description) 
VALUES (leave_type_seq.NEXTVAL, 'PL', 'Paid Leave', 18, 10, 'Planned vacations and personal time off');

INSERT INTO LEAVE_TYPES (leave_type_id, leave_code, leave_name, default_days, max_carry_forward, description) 
VALUES (leave_type_seq.NEXTVAL, 'PRIV', 'Privilege Leave', 15, 0, 'Earned leave for long-term employees');

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Leave Types inserted: 4');
END;
/

-- ============================================
-- SECTION 7: INSERT LEAVE_BALANCES
-- Initialize leave balances for all employees for current year
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Initializing Leave Balances...');
END;
/

DECLARE
    v_year NUMBER := EXTRACT(YEAR FROM SYSDATE);
    v_count NUMBER := 0;
BEGIN
    -- Loop through each employee
    FOR emp IN (SELECT employee_id FROM EMPLOYEES WHERE status = 'ACTIVE') LOOP
        -- Loop through each leave type
        FOR lt IN (SELECT leave_type_id, default_days FROM LEAVE_TYPES WHERE is_active = 'Y') LOOP
            INSERT INTO LEAVE_BALANCES (
                balance_id, employee_id, leave_type_id, year, 
                total_allocated, used, balance, carried_forward
            ) VALUES (
                leave_bal_seq.NEXTVAL, 
                emp.employee_id, 
                lt.leave_type_id, 
                v_year,
                lt.default_days, 
                0, 
                lt.default_days, 
                0
            );
            v_count := v_count + 1;
        END LOOP;
    END LOOP;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('✓ Leave Balances initialized: ' || v_count || ' records');
END;
/

-- ============================================
-- SECTION 8: INSERT SAMPLE LEAVE APPLICATIONS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Leave Applications...');
END;
/

-- Pending leave application
INSERT INTO LEAVE_APPLICATIONS (
    application_id, employee_id, leave_type_id, start_date, end_date, total_days, reason, status
) VALUES (
    leave_app_seq.NEXTVAL, 'EMP001', 1, 
    DATE '2024-03-15', DATE '2024-03-16', 2,
    'Family function to attend in hometown', 'PENDING'
);

-- Approved leave application
INSERT INTO LEAVE_APPLICATIONS (
    application_id, employee_id, leave_type_id, start_date, end_date, total_days, reason, 
    status, approved_by, approved_on, comments
) VALUES (
    leave_app_seq.NEXTVAL, 'EMP002', 3, 
    DATE '2024-02-10', DATE '2024-02-14', 5,
    'Vacation trip to Goa with family', 
    'APPROVED', 'MGR001', SYSDATE - 5, 'Approved. Enjoy your vacation!'
);

-- Rejected leave application
INSERT INTO LEAVE_APPLICATIONS (
    application_id, employee_id, leave_type_id, start_date, end_date, total_days, reason, 
    status, approved_by, approved_on, rejection_reason
) VALUES (
    leave_app_seq.NEXTVAL, 'EMP003', 1, 
    DATE '2024-03-01', DATE '2024-03-05', 5,
    'Personal work to complete',
    'REJECTED', 'MGR001', SYSDATE - 2, 'Project deadline approaching. Please reschedule after March 15.'
);

-- Another pending leave
INSERT INTO LEAVE_APPLICATIONS (
    application_id, employee_id, leave_type_id, start_date, end_date, total_days, reason, status
) VALUES (
    leave_app_seq.NEXTVAL, 'EMP004', 2, 
    DATE '2024-03-20', DATE '2024-03-21', 2,
    'Medical checkup and consultation', 'PENDING'
);

-- Historical approved leave
INSERT INTO LEAVE_APPLICATIONS (
    application_id, employee_id, leave_type_id, start_date, end_date, total_days, reason, 
    status, approved_by, approved_on, comments
) VALUES (
    leave_app_seq.NEXTVAL, 'EMP001', 2, 
    DATE '2024-01-15', DATE '2024-01-17', 3,
    'Fever and cold, need rest',
    'APPROVED', 'MGR001', DATE '2024-01-14', 'Get well soon!'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Leave Applications inserted: 5');
END;
/

-- Update leave balances for approved leaves
UPDATE LEAVE_BALANCES 
SET used = used + 5, balance = balance - 5
WHERE employee_id = 'EMP002' AND leave_type_id = 3 AND year = EXTRACT(YEAR FROM SYSDATE);

UPDATE LEAVE_BALANCES 
SET used = used + 3, balance = balance - 3
WHERE employee_id = 'EMP001' AND leave_type_id = 2 AND year = EXTRACT(YEAR FROM SYSDATE);

COMMIT;

-- ============================================
-- SECTION 9: INSERT HOLIDAYS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Company Holidays...');
END;
/

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-01-26', 'Republic Day', 'National', 'Indian Republic Day celebration');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-03-08', 'Maha Shivaratri', 'Festival', 'Hindu festival');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-03-25', 'Holi', 'Festival', 'Festival of colors');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-03-29', 'Good Friday', 'Festival', 'Christian festival');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-04-11', 'Eid-ul-Fitr', 'Festival', 'Islamic festival');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-08-15', 'Independence Day', 'National', 'Indian Independence Day');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-10-02', 'Gandhi Jayanti', 'National', 'Mahatma Gandhi''s Birthday');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-10-12', 'Dussehra', 'Festival', 'Hindu festival');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-11-01', 'Diwali', 'Festival', 'Festival of lights');

INSERT INTO HOLIDAYS (holiday_id, holiday_date, holiday_name, holiday_type, description) 
VALUES (holiday_seq.NEXTVAL, DATE '2024-12-25', 'Christmas', 'Festival', 'Christian festival');

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Holidays inserted: 10');
END;
/

-- ============================================
-- SECTION 10: INSERT SAMPLE PERFORMANCE REVIEWS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Performance Reviews...');
END;
/

-- Submitted review (waiting for manager feedback)
INSERT INTO PERFORMANCE_REVIEWS (
    review_id, employee_id, review_year, review_period,
    key_deliverables, major_accomplishments, areas_of_improvement,
    self_assessment_rating, self_assessment_comments, status, submitted_date
) VALUES (
    review_seq.NEXTVAL, 'EMP001', 2023, 'Annual-2023',
    'Developed 3 microservices, Led backend team, Mentored 2 junior developers',
    'Completed project migration ahead of schedule, Improved system performance by 40%, Received client appreciation',
    'Need to improve communication skills, Learn cloud technologies (AWS/Azure)',
    4.0, 'Productive year with significant contributions to team success',
    'SUBMITTED', DATE '2024-01-15'
);

-- Completed review (with manager feedback)
INSERT INTO PERFORMANCE_REVIEWS (
    review_id, employee_id, review_year, review_period,
    key_deliverables, major_accomplishments, areas_of_improvement,
    self_assessment_rating, self_assessment_comments,
    manager_feedback, manager_rating, manager_comments,
    final_rating, status, submitted_date, reviewed_by, reviewed_date
) VALUES (
    review_seq.NEXTVAL, 'EMP002', 2023, 'Annual-2023',
    'UI/UX redesign, Responsive web development, Component library creation',
    'Reduced page load time by 60%, Implemented modern design system, Created reusable components',
    'Explore backend technologies, Improve TypeScript skills',
    4.5, 'Excellent year with notable frontend achievements',
    'Outstanding performance! Your design system has become standard across projects. Keep up the excellent work.',
    4.5, 'Highly recommend for promotion to Senior Engineer',
    4.5, 'COMPLETED', DATE '2024-01-10', 'MGR001', DATE '2024-01-20'
);

-- Draft review (not yet submitted)
INSERT INTO PERFORMANCE_REVIEWS (
    review_id, employee_id, review_year, review_period,
    key_deliverables, self_assessment_rating, status
) VALUES (
    review_seq.NEXTVAL, 'EMP003', 2023, 'Annual-2023',
    'Working on leave management module, Database optimization tasks',
    3.5, 'DRAFT'
);

-- Completed review for EMP001 (backend engineer)
INSERT INTO PERFORMANCE_REVIEWS (
    review_id, employee_id, review_year, review_period,
    key_deliverables, major_accomplishments, areas_of_improvement,
    self_assessment_rating, self_assessment_comments,
    manager_feedback, manager_rating, manager_comments,
    final_rating, status, submitted_date, reviewed_by, reviewed_date
) VALUES (
    review_seq.NEXTVAL, 'EMP001', 2022, 'Annual-2022',
    'Developed REST APIs, Integrated CI/CD pipeline, Database schema optimization',
    'Delivered backend APIs 2 weeks early, Reduced DB query time by 35%, Mentored 1 junior dev',
    'Improve public speaking skills, Learn containerization (Docker/Kubernetes)',
    4.0, 'Solid year with impactful backend contributions',
    'Great work on backend infrastructure! The CI/CD improvements have significantly boosted team productivity.',
    4.2, 'Good candidate for a tech lead role in future cycles',
    4.1, 'COMPLETED', DATE '2023-01-15', 'MGR001', DATE '2023-01-25'
);

-- Completed review for EMP003
INSERT INTO PERFORMANCE_REVIEWS (
    review_id, employee_id, review_year, review_period,
    key_deliverables, major_accomplishments, areas_of_improvement,
    self_assessment_rating, self_assessment_comments,
    manager_feedback, manager_rating, manager_comments,
    final_rating, status, submitted_date, reviewed_by, reviewed_date
) VALUES (
    review_seq.NEXTVAL, 'EMP003', 2022, 'Annual-2022',
    'Leave management module, Database optimization, API integration',
    'Successfully delivered leave management UI, Improved DB query performance, Coordinated with 2 cross-functional teams',
    'Need to improve code documentation, Explore unit testing best practices',
    3.5, 'Decent progress with room for improvement',
    'Steady contributor. The leave management module was well-received. Focus on code quality going forward.',
    3.8, 'Continue improving code coverage and documentation standards',
    3.65, 'COMPLETED', DATE '2023-01-18', 'MGR001', DATE '2023-01-28'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Performance Reviews inserted: 5');
END;
/

-- ============================================
-- SECTION 11: INSERT SAMPLE GOALS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Goals...');
END;
/

-- Goal 1: In Progress
INSERT INTO GOALS (
    goal_id, employee_id, goal_title, goal_description, category, 
    deadline, priority, progress, status
) VALUES (
    goal_seq.NEXTVAL, 'EMP001', 
    'Complete AWS Certification', 
    'Prepare and pass AWS Solutions Architect Associate certification exam',
    'Professional Development',
    TRUNC(SYSDATE) + 120, 'HIGH', 60, 'IN_PROGRESS'
);

-- Goal 2: Completed
INSERT INTO GOALS (
    goal_id, employee_id, goal_title, goal_description, category, 
    deadline, priority, progress, status, manager_comments, completed_at
) VALUES (
    goal_seq.NEXTVAL, 'EMP002', 
    'Redesign Company Website',
    'Complete UI/UX redesign of company website with modern design principles',
    'Technical',
    TRUNC(SYSDATE) + 30, 'HIGH', 100, 'COMPLETED',
    'Excellent work! Website traffic increased by 35% after redesign.',
    SYSDATE - 15
);

-- Goal 3: Not Started
INSERT INTO GOALS (
    goal_id, employee_id, goal_title, goal_description, category, 
    deadline, priority, progress, status
) VALUES (
    goal_seq.NEXTVAL, 'EMP003', 
    'Learn Spring Security',
    'Master Spring Security framework for implementing authentication and authorization',
    'Technical',
    TRUNC(SYSDATE) + 180, 'MEDIUM', 0, 'NOT_STARTED'
);

-- Goal 4: In Progress
INSERT INTO GOALS (
    goal_id, employee_id, goal_title, goal_description, category, 
    deadline, priority, progress, status
) VALUES (
    goal_seq.NEXTVAL, 'EMP004', 
    'Improve Code Quality',
    'Achieve 80% code coverage in all modules and reduce technical debt',
    'Technical',
    TRUNC(SYSDATE) + 210, 'HIGH', 45, 'IN_PROGRESS'
);

-- Goal 5: In Progress
INSERT INTO GOALS (
    goal_id, employee_id, goal_title, goal_description, category, 
    deadline, priority, progress, status
) VALUES (
    goal_seq.NEXTVAL, 'EMP005', 
    'Master Test Automation',
    'Learn Selenium and implement automated testing framework',
    'Professional Development',
    TRUNC(SYSDATE) + 150, 'MEDIUM', 30, 'IN_PROGRESS'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Goals inserted: 5');
END;
/

-- ============================================
-- SECTION 12: INSERT SAMPLE NOTIFICATIONS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Notifications...');
END;
/

-- Notification 1: Leave approval notification
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'EMP002', 'LEAVE', 
    'Leave Application Approved',
    'Your leave application for 5 days (Feb 10-14, 2024) has been approved by Priya Sharma.',
    'LEAVE_APPLICATION', 1001, 'Y', 'NORMAL'
);

-- Notification 2: Leave rejection notification
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'EMP003', 'LEAVE',
    'Leave Application Rejected',
    'Your leave application for 5 days (Mar 1-5, 2024) has been rejected. Reason: Project deadline approaching. Please reschedule after March 15.',
    'LEAVE_APPLICATION', 1002, 'N', 'HIGH'
);

-- Notification 3: Performance review feedback
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'EMP002', 'PERFORMANCE',
    'Performance Review Feedback Received',
    'Your manager has provided feedback on your 2023 performance review. Final rating: 4.5/5.0',
    'PERFORMANCE_REVIEW', 2, 'N', 'NORMAL'
);

-- Notification 4: New announcement
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'EMP001', 'ANNOUNCEMENT',
    'Company Town Hall Meeting',
    'Join us for the quarterly town hall meeting on March 30, 2024 at 3:00 PM.',
    'N', 'NORMAL'
);

-- Notification 5: Goal deadline reminder
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'EMP001', 'GOAL',
    'Goal Deadline Approaching',
    'Your goal "Complete AWS Certification" is due in 30 days. Current progress: 60%',
    'GOAL', 1, 'N', 'NORMAL'
);

-- Manager notifications
INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'MGR001', 'LEAVE',
    'New Leave Application',
    'Mastan Sayyad has applied for leave (Mar 15-16, 2024). Please review.',
    'LEAVE_APPLICATION', 1000, 'N', 'NORMAL'
);

INSERT INTO NOTIFICATIONS (
    notification_id, employee_id, notification_type, title, message,
    reference_type, reference_id, is_read, priority
) VALUES (
    notif_seq.NEXTVAL, 'MGR001', 'PERFORMANCE',
    'Performance Review Submitted',
    'Mastan Sayyad has submitted performance review for 2023. Please provide feedback.',
    'PERFORMANCE_REVIEW', 1, 'N', 'HIGH'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Notifications inserted: 7');
END;
/

-- ============================================
-- SECTION 13: INSERT SAMPLE ANNOUNCEMENTS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Announcements...');
END;
/

INSERT INTO ANNOUNCEMENTS (
    announcement_id, title, message, announcement_type, priority,
    target_audience, publish_date, expiry_date, created_by
) VALUES (
    announce_seq.NEXTVAL,
    'Quarterly Town Hall Meeting',
    'Dear Team, We are pleased to invite you to our Q1 2024 Town Hall meeting. Join us to hear updates on company performance, upcoming projects, and strategic initiatives. Date: March 30, 2024, Time: 3:00 PM, Venue: Main Conference Hall. Your participation is highly encouraged!',
    'Event', 'MEDIUM', 'ALL',
    SYSDATE, DATE '2024-03-30', 'ADM001'
);

INSERT INTO ANNOUNCEMENTS (
    announcement_id, title, message, announcement_type, priority,
    target_audience, publish_date, expiry_date, created_by
) VALUES (
    announce_seq.NEXTVAL,
    'Updated Leave Policy - 2024',
    'Important Update: Our leave policy has been updated for 2024. Key changes include: 1) Carry forward limit increased to 10 days for PL, 2) New parental leave policy introduced, 3) Work from home options on casual leave. Please review the updated policy document in the HR portal.',
    'Policy', 'HIGH', 'ALL',
    SYSDATE, DATE '2024-12-31', 'MGR002'
);

INSERT INTO ANNOUNCEMENTS (
    announcement_id, title, message, announcement_type, priority,
    target_audience, publish_date, created_by
) VALUES (
    announce_seq.NEXTVAL,
    'Team Outing - Save the Date!',
    'Mark your calendars! Our annual team outing is scheduled for April 20, 2024. We will be heading to Wonderla Amusement Park for a day of fun and team bonding. More details to follow. Get ready for an exciting day!',
    'Celebration', 'MEDIUM', 'ALL',
    SYSDATE, 'ADM001'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Announcements inserted: 3');
END;
/

-- ============================================
-- SECTION 14: INSERT SAMPLE AUDIT LOGS
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting Sample Audit Logs...');
END;
/

INSERT INTO AUDIT_LOGS (
    audit_id, employee_id, action, table_name, record_id, description
) VALUES (
    audit_seq.NEXTVAL, 'ADM001', 'LOGIN', 'EMPLOYEES', 'ADM001',
    'Admin user logged in successfully'
);

INSERT INTO AUDIT_LOGS (
    audit_id, employee_id, action, table_name, record_id, description
) VALUES (
    audit_seq.NEXTVAL, 'EMP001', 'INSERT', 'LEAVE_APPLICATIONS', '1000',
    'New leave application created for 2 days'
);

INSERT INTO AUDIT_LOGS (
    audit_id, employee_id, action, table_name, record_id, 
    field_name, old_value, new_value, description
) VALUES (
    audit_seq.NEXTVAL, 'MGR001', 'UPDATE', 'LEAVE_APPLICATIONS', '1001',
    'status', 'PENDING', 'APPROVED',
    'Leave application approved by manager'
);

INSERT INTO AUDIT_LOGS (
    audit_id, employee_id, action, table_name, record_id, description
) VALUES (
    audit_seq.NEXTVAL, 'ADM001', 'INSERT', 'EMPLOYEES', 'EMP010',
    'New employee created - Karthik Rao'
);

INSERT INTO AUDIT_LOGS (
    audit_id, employee_id, action, table_name, record_id, description
) VALUES (
    audit_seq.NEXTVAL, 'EMP001', 'INSERT', 'PERFORMANCE_REVIEWS', '1',
    'Performance review submitted for year 2023'
);

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Audit Logs inserted: 5');
END;
/

-- ============================================
-- SECTION 15: VERIFICATION AND SUMMARY
-- ============================================

BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('======================================');
    DBMS_OUTPUT.PUT_LINE('Data Verification Summary');
    DBMS_OUTPUT.PUT_LINE('======================================');
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM DEPARTMENTS;
    DBMS_OUTPUT.PUT_LINE('Departments: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM DESIGNATIONS;
    DBMS_OUTPUT.PUT_LINE('Designations: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM ROLES;
    DBMS_OUTPUT.PUT_LINE('Roles: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM EMPLOYEES;
    DBMS_OUTPUT.PUT_LINE('Employees: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM EMPLOYEE_ROLES;
    DBMS_OUTPUT.PUT_LINE('Employee-Role Mappings: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM LEAVE_TYPES;
    DBMS_OUTPUT.PUT_LINE('Leave Types: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM LEAVE_BALANCES;
    DBMS_OUTPUT.PUT_LINE('Leave Balances: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM LEAVE_APPLICATIONS;
    DBMS_OUTPUT.PUT_LINE('Leave Applications: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM HOLIDAYS;
    DBMS_OUTPUT.PUT_LINE('Holidays: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM PERFORMANCE_REVIEWS;
    DBMS_OUTPUT.PUT_LINE('Performance Reviews: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM GOALS;
    DBMS_OUTPUT.PUT_LINE('Goals: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM NOTIFICATIONS;
    DBMS_OUTPUT.PUT_LINE('Notifications: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM ANNOUNCEMENTS;
    DBMS_OUTPUT.PUT_LINE('Announcements: ' || v_count);
    
    SELECT COUNT(*) INTO v_count FROM AUDIT_LOGS;
    DBMS_OUTPUT.PUT_LINE('Audit Logs: ' || v_count);
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('======================================');
    DBMS_OUTPUT.PUT_LINE('✓ Seed Data Load Complete!');
    DBMS_OUTPUT.PUT_LINE('======================================');
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Test Credentials:');
    DBMS_OUTPUT.PUT_LINE('Admin:    ADM001 / password123');
    DBMS_OUTPUT.PUT_LINE('Manager:  MGR001 / password123');
    DBMS_OUTPUT.PUT_LINE('Employee: EMP001 / password123');
    DBMS_OUTPUT.PUT_LINE('======================================');
END;
/

COMMIT;


-- Set default questions for existing employees
UPDATE EMPLOYEES 
SET security_question_1 = 'What is your favorite teacher''s name?',
    security_question_2 = 'What was your first pet''s name?',
    security_answer_1 = '$2a$12$a3rQGGfHlqU.yG0VcQjSbu7tIehFEbIy0zi9ChQ1Kp8NFzYs8YY6.',  -- Hash of "teacher"
    security_answer_2 = '$2a$12$eqgVRvnUrctdhGunxEU2re4wD8L32uOeJRELDp7GizKjGe1TQmtpu'   -- Hash of "buddy"
WHERE security_question_1 IS NULL;

COMMIT;


-- ============================================
-- END OF SEED DATA SCRIPT
-- ============================================