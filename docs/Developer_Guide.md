# Comprehensive Developer Guide

## 1. System Architecture
RevWorkForce is constructed as a Server-Side Rendered (SSR) monolithic Spring Boot Application.
- **Frontend Views**: Thymeleaf templates located in `src/main/resources/templates/`. The CSS/JS assets are served from `static/`.
- **API Interfaces**: Exposes distinct JSON APIs via `@RestController` configurations mostly grouped under `/api/` (for integrations) and standard `@Controller` for MVC routing.
- **Data Persistence**: Uses Spring Data JPA (Hibernate provider) connected to a Relational Database. Entities are tightly mapped.

## 2. Project Bootstrapping
### Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL Server 8+ or PostgreSQL

### Local properties configuration
Copy `application.properties.example` to `application.properties` and replace variables:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/revworkforce
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 3. Package Explanations
The `com.revature.revworkforce` package is split distinctly:
1. `config/`: Configuration classes (`SecurityConfig`, `WebMvcConfig`).
2. `controller/`: Web endpoints and API routers. Note that `ApiController` appended files return JSON, otherwise HTML.
3. `converter/`: `AttributeConverter` implementations that translate Database Enums into Java Strings/Custom wrappers explicitly, bypassing default ordinal mapping.
4. `dto/`: Immutable records/classes for transferring form data safely from Controllers to Services without exposing Model relationships.
5. `enums/`: Standard Enums for Application states (`LeaveStatus`, `GoalStatus`, `NotificationType`).
6. `exception/`: Sub-classed RuntimeExceptions (e.g. `LeaveOverlapException`). Controlled by `@ControllerAdvice` in `GlobalErrorController`.
7. `model/`: Hibernate mapped Tables (`@Entity`, `@Table`, `@Id`).
8. `repository/`: Spring Data JPA interfaces (`JpaRepository<Entity, Id>`).
9. `scheduler/`: Spring `@Scheduled` cron jobs (e.g., executing nightly leave balance resets).
10. `security/`: JWT implementations, `CustomUserDetailsService` resolving against `Employee` repository.
11. `service/` & `service/impl/`: Business rule validation logic.

## 4. Development Standards
- **Fat Services, Thin Controllers**: Always inject `DTO` objects deep to the `Service` layer, rather than converting forms into Entities immediately in the controller. Let `ServiceImpl` query DB state and enforce business rules (like overlapping leaves) before invoking `.save()`.
- **Authentication Checks**: Methods processing restricted tasks must verify `Authentication` parameters. Compare `Authentication.getName()` (usually Employee ID) against requested payload IDs to prevent horizontal privilege escalation.
- **Validation**: Utilize `jakarta.validation.constraints.*` (`@NotNull`, `@Size`, etc.) on all DTOs and trap them with `@Valid` inside the controller method signatures. Check `BindingResult.hasErrors()`.

## 5. Adding New Entities
1. Create Model in `model/`. Add standard fields. Use Lombok or generate Getters/Setters. Keep relation loading `LAZY` unless explicitly needed `EAGER` to prevent N+1 issues.
2. Define a repository interface in `repository/`.
3. Add a Service interface mapping basic CRUD constraints in `service/`.
4. Create the impl in `service/impl/` ensuring to `@Override` and annotate with `@Service`. Add `@Transactional` to complex update mutations.
5. Define corresponding views in `templates/` and map via `controller/`.
