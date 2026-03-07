


sql-- Check all tables
SELECT table_name FROM user_tables ORDER BY table_name;

-- Check employee data
SELECT employee_id, first_name, last_name, email, status 
FROM EMPLOYEES 
ORDER BY employee_id;

-- Check leave balances
SELECT e.first_name, e.last_name, lt.leave_name, lb.balance
FROM LEAVE_BALANCES lb
JOIN EMPLOYEES e ON lb.employee_id = e.employee_id
JOIN LEAVE_TYPES lt ON lb.leave_type_id = lt.leave_type_id
WHERE e.employee_id = 'EMP001';

-- Test login credentials
SELECT employee_id, first_name, email, password_hash 
FROM EMPLOYEES 
WHERE employee_id = 'EMP001';


-- 1. Count all tables (should be 14)
SELECT COUNT(*) FROM user_tables;

-- 2. Count all employees (should be 10)
SELECT COUNT(*) FROM EMPLOYEES;

-- 3. Count all leave balances (should be 40 = 10 employees × 4 leave types)
SELECT COUNT(*) FROM LEAVE_BALANCES;

-- 4. Test a login
SELECT employee_id, first_name, email 
FROM EMPLOYEES 
WHERE employee_id = 'EMP001' 
AND password_hash = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYDpLABB5dW';

-- 5. Check leave applications
SELECT la.application_id, e.first_name, lt.leave_name, la.status
FROM LEAVE_APPLICATIONS la
JOIN EMPLOYEES e ON la.employee_id = e.employee_id
JOIN LEAVE_TYPES lt ON la.leave_type_id = lt.leave_type_id;


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


-- Verify security questions
SELECT employee_id, first_name, security_question_1, security_question_2
FROM EMPLOYEES WHERE ROWNUM <= 3;
commit;