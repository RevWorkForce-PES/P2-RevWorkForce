-- =================================================
-- Create User for Phase 2 RevWorkForce Application
-- =================================================

-- Drop user if exists (for fresh setup)
-- DROP USER revworkforce CASCADE;

-- Create user
CREATE USER revworkforce IDENTIFIED BY revpassword;

-- Grant necessary privileges
GRANT CONNECT, RESOURCE TO revworkforce;
GRANT CREATE SESSION TO revworkforce;
GRANT CREATE TABLE TO revworkforce;
GRANT CREATE VIEW TO revworkforce;
GRANT CREATE SEQUENCE TO revworkforce;
GRANT CREATE TRIGGER TO revworkforce;
GRANT UNLIMITED TABLESPACE TO revworkforce;

-- Confirm user creation
SELECT username FROM dba_users WHERE username = 'REVWORKFORCE';

COMMIT;