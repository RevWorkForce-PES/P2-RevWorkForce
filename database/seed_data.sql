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
C:\Users\user\.jdks\temurin-17.0.18\bin\java.exe -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\lib\idea_rt.jar=55012" -Dfile.encoding=UTF-8 -classpath D:\Learning\Project\P2-RevWorkForce\target\classes;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-web\3.2.1\spring-boot-starter-web-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter\3.2.1\spring-boot-starter-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-logging\3.2.1\spring-boot-starter-logging-3.2.1.jar;C:\Users\user\.m2\repository\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar;C:\Users\user\.m2\repository\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar;C:\Users\user\.m2\repository\org\apache\logging\log4j\log4j-to-slf4j\2.21.1\log4j-to-slf4j-2.21.1.jar;C:\Users\user\.m2\repository\org\apache\logging\log4j\log4j-api\2.21.1\log4j-api-2.21.1.jar;C:\Users\user\.m2\repository\org\slf4j\jul-to-slf4j\2.0.9\jul-to-slf4j-2.0.9.jar;C:\Users\user\.m2\repository\jakarta\annotation\jakarta.annotation-api\2.1.1\jakarta.annotation-api-2.1.1.jar;C:\Users\user\.m2\repository\org\yaml\snakeyaml\2.2\snakeyaml-2.2.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-json\3.2.1\spring-boot-starter-json-3.2.1.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.15.3\jackson-databind-2.15.3.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.15.3\jackson-annotations-2.15.3.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.15.3\jackson-core-2.15.3.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.15.3\jackson-datatype-jdk8-2.15.3.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.15.3\jackson-datatype-jsr310-2.15.3.jar;C:\Users\user\.m2\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.15.3\jackson-module-parameter-names-2.15.3.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-tomcat\3.2.1\spring-boot-starter-tomcat-3.2.1.jar;C:\Users\user\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\10.1.17\tomcat-embed-core-10.1.17.jar;C:\Users\user\.m2\repository\org\apache\tomcat\embed\tomcat-embed-websocket\10.1.17\tomcat-embed-websocket-10.1.17.jar;C:\Users\user\.m2\repository\org\springframework\spring-web\6.1.2\spring-web-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-beans\6.1.2\spring-beans-6.1.2.jar;C:\Users\user\.m2\repository\io\micrometer\micrometer-observation\1.12.1\micrometer-observation-1.12.1.jar;C:\Users\user\.m2\repository\io\micrometer\micrometer-commons\1.12.1\micrometer-commons-1.12.1.jar;C:\Users\user\.m2\repository\org\springframework\spring-webmvc\6.1.2\spring-webmvc-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-context\6.1.2\spring-context-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-expression\6.1.2\spring-expression-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-thymeleaf\3.2.1\spring-boot-starter-thymeleaf-3.2.1.jar;C:\Users\user\.m2\repository\org\thymeleaf\thymeleaf-spring6\3.1.2.RELEASE\thymeleaf-spring6-3.1.2.RELEASE.jar;C:\Users\user\.m2\repository\org\thymeleaf\thymeleaf\3.1.2.RELEASE\thymeleaf-3.1.2.RELEASE.jar;C:\Users\user\.m2\repository\org\attoparser\attoparser\2.0.7.RELEASE\attoparser-2.0.7.RELEASE.jar;C:\Users\user\.m2\repository\org\unbescape\unbescape\1.1.6.RELEASE\unbescape-1.1.6.RELEASE.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-data-jpa\3.2.1\spring-boot-starter-data-jpa-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-aop\3.2.1\spring-boot-starter-aop-3.2.1.jar;C:\Users\user\.m2\repository\org\aspectj\aspectjweaver\1.9.21\aspectjweaver-1.9.21.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-jdbc\3.2.1\spring-boot-starter-jdbc-3.2.1.jar;C:\Users\user\.m2\repository\com\zaxxer\HikariCP\5.0.1\HikariCP-5.0.1.jar;C:\Users\user\.m2\repository\org\springframework\spring-jdbc\6.1.2\spring-jdbc-6.1.2.jar;C:\Users\user\.m2\repository\org\hibernate\orm\hibernate-core\6.4.1.Final\hibernate-core-6.4.1.Final.jar;C:\Users\user\.m2\repository\jakarta\persistence\jakarta.persistence-api\3.1.0\jakarta.persistence-api-3.1.0.jar;C:\Users\user\.m2\repository\jakarta\transaction\jakarta.transaction-api\2.0.1\jakarta.transaction-api-2.0.1.jar;C:\Users\user\.m2\repository\org\jboss\logging\jboss-logging\3.5.3.Final\jboss-logging-3.5.3.Final.jar;C:\Users\user\.m2\repository\org\hibernate\common\hibernate-commons-annotations\6.0.6.Final\hibernate-commons-annotations-6.0.6.Final.jar;C:\Users\user\.m2\repository\io\smallrye\jandex\3.1.2\jandex-3.1.2.jar;C:\Users\user\.m2\repository\com\fasterxml\classmate\1.6.0\classmate-1.6.0.jar;C:\Users\user\.m2\repository\net\bytebuddy\byte-buddy\1.14.10\byte-buddy-1.14.10.jar;C:\Users\user\.m2\repository\org\glassfish\jaxb\jaxb-runtime\4.0.4\jaxb-runtime-4.0.4.jar;C:\Users\user\.m2\repository\org\glassfish\jaxb\jaxb-core\4.0.4\jaxb-core-4.0.4.jar;C:\Users\user\.m2\repository\org\eclipse\angus\angus-activation\2.0.1\angus-activation-2.0.1.jar;C:\Users\user\.m2\repository\org\glassfish\jaxb\txw2\4.0.4\txw2-4.0.4.jar;C:\Users\user\.m2\repository\com\sun\istack\istack-commons-runtime\4.1.2\istack-commons-runtime-4.1.2.jar;C:\Users\user\.m2\repository\jakarta\inject\jakarta.inject-api\2.0.1\jakarta.inject-api-2.0.1.jar;C:\Users\user\.m2\repository\org\antlr\antlr4-runtime\4.13.0\antlr4-runtime-4.13.0.jar;C:\Users\user\.m2\repository\org\springframework\data\spring-data-jpa\3.2.1\spring-data-jpa-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\data\spring-data-commons\3.2.1\spring-data-commons-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\spring-orm\6.1.2\spring-orm-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-tx\6.1.2\spring-tx-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-aspects\6.1.2\spring-aspects-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-security\3.2.1\spring-boot-starter-security-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\spring-aop\6.1.2\spring-aop-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\security\spring-security-config\6.2.1\spring-security-config-6.2.1.jar;C:\Users\user\.m2\repository\org\springframework\security\spring-security-web\6.2.1\spring-security-web-6.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-starter-validation\3.2.1\spring-boot-starter-validation-3.2.1.jar;C:\Users\user\.m2\repository\org\apache\tomcat\embed\tomcat-embed-el\10.1.17\tomcat-embed-el-10.1.17.jar;C:\Users\user\.m2\repository\org\hibernate\validator\hibernate-validator\8.0.1.Final\hibernate-validator-8.0.1.Final.jar;C:\Users\user\.m2\repository\jakarta\validation\jakarta.validation-api\3.0.2\jakarta.validation-api-3.0.2.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-devtools\3.2.1\spring-boot-devtools-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot\3.2.1\spring-boot-3.2.1.jar;C:\Users\user\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\3.2.1\spring-boot-autoconfigure-3.2.1.jar;C:\Users\user\.m2\repository\org\thymeleaf\extras\thymeleaf-extras-springsecurity6\3.1.2.RELEASE\thymeleaf-extras-springsecurity6-3.1.2.RELEASE.jar;C:\Users\user\.m2\repository\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar;C:\Users\user\.m2\repository\com\oracle\database\jdbc\ojdbc11\23.3.0.23.09\ojdbc11-23.3.0.23.09.jar;C:\Users\user\.m2\repository\org\springframework\security\spring-security-crypto\6.2.1\spring-security-crypto-6.2.1.jar;C:\Users\user\.m2\repository\org\projectlombok\lombok\1.18.30\lombok-1.18.30.jar;C:\Users\user\.m2\repository\jakarta\xml\bind\jakarta.xml.bind-api\4.0.1\jakarta.xml.bind-api-4.0.1.jar;C:\Users\user\.m2\repository\jakarta\activation\jakarta.activation-api\2.1.2\jakarta.activation-api-2.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-core\6.1.2\spring-core-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\spring-jcl\6.1.2\spring-jcl-6.1.2.jar;C:\Users\user\.m2\repository\org\springframework\security\spring-security-core\6.2.1\spring-security-core-6.2.1.jar com.revature.revworkforce.RevWorkForceApplication

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)

2026-03-07T11:20:54.897+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] c.r.r.RevWorkForceApplication            : Starting RevWorkForceApplication using Java 17.0.18 with PID 9576 (D:\Learning\Project\P2-RevWorkForce\target\classes started by user in D:\Learning\Project\P2-RevWorkForce)
2026-03-07T11:20:54.899+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] c.r.r.RevWorkForceApplication            : Running with Spring Boot v3.2.1, Spring v6.1.2
2026-03-07T11:20:54.900+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] c.r.r.RevWorkForceApplication            : No active profile set, falling back to 1 default profile: "default"
2026-03-07T11:20:54.968+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.s.b.devtools.restart.ChangeableUrls    : The Class-Path manifest attribute in C:\Users\user\.m2\repository\com\oracle\database\jdbc\ojdbc11\23.3.0.23.09\ojdbc11-23.3.0.23.09.jar referenced one or more files that do not exist: file:/C:/Users/user/.m2/repository/com/oracle/database/jdbc/ojdbc11/23.3.0.23.09/oraclepki.jar
2026-03-07T11:20:54.969+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-03-07T11:20:54.969+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-03-07T11:20:56.103+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2026-03-07T11:20:56.295+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 180 ms. Found 14 JPA repository interfaces.
2026-03-07T11:20:57.210+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8082 (http)
2026-03-07T11:20:57.223+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-03-07T11:20:57.224+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.17]
2026-03-07T11:20:57.292+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2026-03-07T11:20:57.293+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2322 ms
2026-03-07T11:20:57.441+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : Driver class oracle.jdbc.OracleDriver found in Thread context class loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader@7af00a1
2026-03-07T11:20:57.643+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2026-03-07T11:20:57.730+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.4.1.Final
2026-03-07T11:20:57.774+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2026-03-07T11:20:58.061+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2026-03-07T11:20:58.092+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : RevWorkForceHikariCP - configuration:
2026-03-07T11:20:58.095+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : allowPoolSuspension.............false
2026-03-07T11:20:58.096+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : autoCommit......................true
2026-03-07T11:20:58.096+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : catalog.........................none
2026-03-07T11:20:58.096+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : connectionInitSql...............none
2026-03-07T11:20:58.096+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : connectionTestQuery............."SELECT 1 FROM DUAL"
2026-03-07T11:20:58.096+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : connectionTimeout...............30000
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : dataSource......................none
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : dataSourceClassName.............none
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : dataSourceJNDI..................none
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : dataSourceProperties............{password=<masked>}
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : driverClassName................."oracle.jdbc.OracleDriver"
2026-03-07T11:20:58.097+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : exceptionOverrideClassName......none
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : healthCheckProperties...........{}
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : healthCheckRegistry.............none
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : idleTimeout.....................600000
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : initializationFailTimeout.......1
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : isolateInternalQueries..........false
2026-03-07T11:20:58.098+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : jdbcUrl.........................jdbc:oracle:thin:@//localhost:1521/XEPDB1
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : keepaliveTime...................0
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : leakDetectionThreshold..........0
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : maxLifetime.....................1800000
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : maximumPoolSize.................10
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : metricRegistry..................none
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : metricsTrackerFactory...........none
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : minimumIdle.....................5
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : password........................<masked>
2026-03-07T11:20:58.099+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : poolName........................"RevWorkForceHikariCP"
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : readOnly........................false
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : registerMbeans..................false
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : scheduledExecutor...............none
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : schema..........................none
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : threadFactory...................internal
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : transactionIsolation............default
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : username........................""revworkforce-db""
2026-03-07T11:20:58.100+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariConfig           : validationTimeout...............5000
2026-03-07T11:20:58.100+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.HikariDataSource       : RevWorkForceHikariCP - Starting...
2026-03-07T11:21:00.030+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.pool.PoolBase          : RevWorkForceHikariCP - Failed to create/setup connection: ORA-01017: invalid username/password; logon denied

https://docs.oracle.com/error-help/db/ora-01017/
2026-03-07T11:21:00.030+05:30 DEBUG 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : RevWorkForceHikariCP - Cannot acquire connection from data source

java.sql.SQLException: ORA-01017: invalid username/password; logon denied

https://docs.oracle.com/error-help/db/ora-01017/
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:702) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:603) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:598) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.processError(T4CTTIfun.java:1795) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.processError(T4CTTIoauthenticate.java:866) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.receive(T4CTTIfun.java:1102) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.doRPC(T4CTTIfun.java:456) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:508) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTHWithO5Logon(T4CTTIoauthenticate.java:1676) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:1421) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:1368) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.authenticateWithPassword(T4CConnection.java:1821) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.authenticateUserForLogon(T4CConnection.java:1764) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:976) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.PhysicalConnection.connect(PhysicalConnection.java:1157) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CDriverExtension.getConnection(T4CDriverExtension.java:104) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:825) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:651) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
	at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:428) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:61) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:276) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:107) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:68) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:170) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1820) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1769) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:599) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:521) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1232) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:950) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:762) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:464) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1358) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1347) ~[spring-boot-3.2.1.jar:3.2.1]
	at com.revature.revworkforce.RevWorkForceApplication.main(RevWorkForceApplication.java:49) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:569) ~[na:na]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.1.jar:3.2.1]

2026-03-07T11:21:01.036+05:30 ERROR 9576 --- [RevWorkForce] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : RevWorkForceHikariCP - Exception during pool initialization.

java.sql.SQLException: ORA-01017: invalid username/password; logon denied

https://docs.oracle.com/error-help/db/ora-01017/
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:702) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:603) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:598) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.processError(T4CTTIfun.java:1795) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.processError(T4CTTIoauthenticate.java:866) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.receive(T4CTTIfun.java:1102) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIfun.doRPC(T4CTTIfun.java:456) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:508) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTHWithO5Logon(T4CTTIoauthenticate.java:1676) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:1421) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CTTIoauthenticate.doOAUTH(T4CTTIoauthenticate.java:1368) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.authenticateWithPassword(T4CConnection.java:1821) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.authenticateUserForLogon(T4CConnection.java:1764) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:976) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.PhysicalConnection.connect(PhysicalConnection.java:1157) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.T4CDriverExtension.getConnection(T4CDriverExtension.java:104) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:825) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:651) ~[ojdbc11-23.3.0.23.09.jar:23.3.0.23.09]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:359) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:201) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:470) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:561) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:100) ~[HikariCP-5.0.1.jar:na]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-5.0.1.jar:na]
	at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:428) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:61) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:276) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:107) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:68) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:170) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1820) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1769) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:599) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:521) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1232) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:950) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:762) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:464) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1358) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1347) ~[spring-boot-3.2.1.jar:3.2.1]
	at com.revature.revworkforce.RevWorkForceApplication.main(RevWorkForceApplication.java:49) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:569) ~[na:na]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.1.jar:3.2.1]

2026-03-07T11:21:01.038+05:30  WARN 9576 --- [RevWorkForce] [  restartedMain] o.h.e.j.e.i.JdbcEnvironmentInitiator     : HHH000342: Could not obtain connection to query metadata

java.lang.NullPointerException: Cannot invoke "org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(java.sql.SQLException, String)" because the return value of "org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.sqlExceptionHelper()" is null
	at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:116) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:276) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:107) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:68) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:170) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1820) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1769) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:599) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:521) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1232) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:950) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:762) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:464) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1358) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1347) ~[spring-boot-3.2.1.jar:3.2.1]
	at com.revature.revworkforce.RevWorkForceApplication.main(RevWorkForceApplication.java:49) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:569) ~[na:na]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.1.jar:3.2.1]

2026-03-07T11:21:01.040+05:30 ERROR 9576 --- [RevWorkForce] [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2026-03-07T11:21:01.041+05:30  WARN 9576 --- [RevWorkForce] [  restartedMain] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2026-03-07T11:21:01.045+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2026-03-07T11:21:01.069+05:30  INFO 9576 --- [RevWorkForce] [  restartedMain] .s.b.a.l.ConditionEvaluationReportLogger : 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-03-07T11:21:01.089+05:30 ERROR 9576 --- [RevWorkForce] [  restartedMain] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1773) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:599) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:521) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1232) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:950) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.2.jar:6.1.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:762) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:464) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1358) ~[spring-boot-3.2.1.jar:3.2.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1347) ~[spring-boot-3.2.1.jar:3.2.1]
	at com.revature.revworkforce.RevWorkForceApplication.main(RevWorkForceApplication.java:49) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:569) ~[na:na]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:50) ~[spring-boot-devtools-3.2.1.jar:3.2.1]
Caused by: org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:276) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:238) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:215) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.relational.Database.<init>(Database.java:45) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:223) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:191) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:170) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1432) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1503) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:376) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:352) ~[spring-orm-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1820) ~[spring-beans-6.1.2.jar:6.1.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1769) ~[spring-beans-6.1.2.jar:6.1.2]
	... 21 common frames omitted
Caused by: org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
	at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.determineDialect(DialectFactoryImpl.java:191) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.buildDialect(DialectFactoryImpl.java:87) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentWithDefaults(JdbcEnvironmentInitiator.java:143) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:348) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:107) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:68) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:129) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:263) ~[hibernate-core-6.4.1.Final.jar:6.4.1.Final]
	... 36 common frames omitted


Process finished with exit code 0

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

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Performance Reviews inserted: 3');
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
    'Event', 'NORMAL', 'ALL',
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
    'Celebration', 'NORMAL', 'ALL',
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

-- ============================================
-- END OF SEED DATA SCRIPT
-- ============================================