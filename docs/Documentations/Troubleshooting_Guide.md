# Error Troubleshooting Guide

## Common Issues & Resolutions

### 1. Database Connectivity Failures
**Error Log**: `com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`
- **Cause**: The application is booting, but the target database is offline or unreachable via configured ports.
- **Resolution**:
  - Verify `SPRING_DATASOURCE_URL` correctly targets the database host IP.
  - Verify that your MySQL daemon active process is running on the host system using `systemctl status mysql`.

### 2. Missing Column / Schema Validation Failures
**Error Log**: `org.hibernate.tool.schema.spi.SchemaManagementException: Schema-validation: missing column [xyz] in table [abc]`
- **Cause**: The application properties enforce `spring.jpa.hibernate.ddl-auto=validate`, but a developer added a new field inside a Java `@Entity` without writing migration SQL logic adding the matching column to the mapped DB instance.
- **Resolution**: Let the framework handle manual modifications in Development via `ddl-auto=update`, or execute manual `ALTER TABLE` operations against the database to catch the column up.

### 3. Thymeleaf Template Input Parsing
**Error Log**: `org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing`
- **Cause**: 95% of the time, this is caused by a malformed HTML property reference mismatching the attached backend DTO. Specifically errors originating from `th:field="*{invalidField}"`.
- **Resolution**: Navigate directly to the indicated `.html` line number in the log. Cross check the `*{value}` explicitly against the Java `DTO` object assigned within the `@Controller` method.

### 4. Controller Ambiguous Mappings
**Error Log**: `java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'xyzController' method`
- **Cause**: Boot fails explicitly if two distinct methods utilize the absolute exact same HTTP Router configuration (`@GetMapping("/manager/test")`).
- **Resolution**: Track down the duplicate route mappings inside `controller/` logic and rename or adjust the route definitions uniquely.

### 5. Persistent 403 Forbidden Errors
**Error Log**: No exception array. User receives a standard `403 Access Denied` blank payload navigating valid logic boundaries.
- **Cause**: The `SecurityConfig.java` defines route level authorizations that block standard roles. E.g., `.requestMatchers("/admin/**").hasRole("ADMIN")`. 
- **Resolution**: Check the `EmployeeRole` bindings inside the target Database User. Spring prepends `ROLE_` intrinsically. The entity needs the role assigned natively within `employee_roles` mapping tables.
