# Comprehensive Developer Guide

## 1. System Architecture overview
RevWorkForce is constructed as a Server-Side Rendered (SSR) monolithic Spring Boot Application.
- **Frontend Views**: Thymeleaf templates located in `src/main/resources/templates/`. The CSS/JS assets are served from `static/`.
- **API Interfaces**: Exposes distinct JSON APIs via `@RestController` configurations mostly grouped under `/api/` (for integrations) and standard `@Controller` for MVC routing.
- **Data Persistence**: Uses Spring Data JPA (Hibernate provider) connected to a Relational Database. Entities are tightly mapped.
- **Security**: Secured via Spring Security filter chains utilizing a hybrid approach of classical HTTP session tracking for the MVC views, and JWT capability for the `/api/` routes.

## 2. Project Bootstrapping
### Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL Server 8+ or PostgreSQL (with dialect changes)

### Local properties configuration
Copy `application.properties.example` to `application.properties` and replace variables:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/revworkforce
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.revature=DEBUG
```

## 3. Core Design Patterns
1. **Controller-Service-Repository (N-Tier)**
   - HTTP requests are received by the Controller, which formats the data payloads into DTOs.
   - The DTO is passed down to the Interface-defined Service layer where robust business validation occurs.
   - The Repository is invoked by the Service to handle raw DB IO.
2. **Data Transfer Objects (DTO)**
   - Entities are NEVER passed directly to the Frontend to prevent accidental exposure of sensitive mapping data (e.g., Password Hashes) and to prevent Jackson infinite recursion during JSON serialization.
3. **Global Exception Handling**
   - We utilize `@ControllerAdvice` (`GlobalErrorController.java`) to establish a unified netting for all RuntimeExceptions. When a Service throws `LeaveAllocationException`, the Advice intercepts it and renders a clean HTML error page rather than a Tomcat stack trace.

## 4. Package Explanations
The `com.revature.revworkforce` package is structured by technical domain:
1. `config/`: Application bootstrap settings (`SecurityConfig`, `WebMvcConfig`).
2. `controller/`: REST (`@RestController`) and MVC (`@Controller`) endpoints.
3. `converter/`: JPA `@Converter` classes. Crucial for mapping complex Enums to single characters or strings in MySQL seamlessly instead of relying on brittle ordinal indexes.
4. `dto/`: Immutable records/classes for payload transfer.
5. `enums/`: Immutable constants driving application state mapping (`LeaveStatus`, `GoalStatus`).
6. `exception/`: Sub-classed Custom Exceptions extending `RuntimeException` or `Exception`.
7. `model/`: Real Table representations via Hibernate.
8. `repository/`: Spring Data Interfaces heavily utilizing query derivation (e.g., `findByEmployeeAndStatus()`).
9. `scheduler/`: Scheduled background chron jobs mapping `@Scheduled`.
10. `security/`: Custom UserDetailServices and Filters.
11. `service/` & `service/impl/`: Business validation engines.

## 5. Development Standards & Conventions
- **Fat Services, Thin Controllers**: 
  - Controllers should rarely exceed 30 lines per method. If they do, logic belongs in the Service.
  - Controllers map inputs to DTOs. Services execute logic on DTOs and deal with Entities.
- **Authentication Checking**:
  - Always verify `Authentication.getName()` matches the `id` requested in payloads. For example, validating an employee isn't fetching the leave history of their coworker horizontally.
- **Data Validation**:
  - Liberally apply `jakarta.validation.constraints` (`@NotNull`, `@Past`, `@Size`) on DTO fields. 
  - Trap these validations in the Controller using `@Valid` and checking `BindingResult`.
- **Naming Conventions**:
  - Interfaces in `service/` are named cleanly (e.g., `EmployeeService`).
  - Implementations in `service/impl/` always carry the suffix (e.g., `EmployeeServiceImpl`).

## 6. How to Add a New Feature Module (Example: Training Path)
1. **Entity Models (`model/`)**: Create `TrainingPath.java`. Define fields, Getters/Setters. Use `@ManyToOne` referencing `Employee`.
2. **JPA Repo (`repository/`)**: Create `TrainingPathRepository extends JpaRepository<TrainingPath, Long>`.
3. **DTO Serialization (`dto/`)**: Create `TrainingPathDTO.java` to act as the form payload interface.
4. **Service Contract (`service/`)**: Define the interface `TrainingPathService`.
5. **Service Impl (`service/impl/`)**: Create `TrainingPathServiceImpl`. Inject Repo. Code the business logic. Add `@Transactional` on all mutating (create/update) functions.
6. **Controller Mapping (`controller/`)**: Create `TrainingController`. Map `@GetMapping` for the html pages and `@PostMapping` for the forms intercepting the DTOs.
7. **Frontend Mapping (`templates/`)**: Write the Thymeleaf representations binding to the DTO attributes via `th:object`.

## 7. Frontend Integration Guidelines (Thymeleaf)
- All structural layouts extend from a base template utilizing Thymeleaf fragments `th:replace="fragments/layout :: html"`.
- Use `th:field="*{propertyName}"` closely tied to the DTO provided by the Model in the controller.
- Leverage Spring Security context directly in the DOM using `sec:authorize="hasRole('MANAGER')"` to dynamically hide buttons representing forbidden operations.

## 8. Common Troubleshooting during Development
- **LazyInitializationException**: Occurs when attempting to read child properties (`employee.getDepartment().getName()`) on a model after the Hibernate transaction has closed in the Service. Fix this by fetching eagerly explicitly using `@Query("JOIN FETCH")` or defining it in the service via Hibernate.initialize.
- **TemplateInputException**: A Thymeleaf processing error, 90% of the time caused by a typo in a `th:field` name that doesn't match the backing model.
- **403 Forbidden**: Your current test user lacks the authorities configured in `SecurityConfig`. Check `EmployeeRole` mapping.
