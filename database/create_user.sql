-- Connect as SYSTEM
sqlplus system/your_sys_password@localhost:1521/XE

-- Create user
CREATE USER "revworkforce-db" IDENTIFIED BY revpassword;

-- Grant privileges
GRANT CONNECT, RESOURCE, DBA TO "revworkforce-db";
GRANT CREATE SESSION TO "revworkforce-db";
GRANT CREATE TABLE TO "revworkforce-db";
GRANT CREATE VIEW TO "revworkforce-db";
GRANT CREATE SEQUENCE TO "revworkforce-db";
GRANT CREATE TRIGGER TO "revworkforce-db";
GRANT UNLIMITED TABLESPACE TO "revworkforce-db";

-- Verify user created
SELECT username FROM dba_users WHERE username = 'revworkforce-db';

-- Exit
EXIT;