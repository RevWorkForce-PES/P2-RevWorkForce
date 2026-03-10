# Comprehensive Test Strategy

## 1. Core Principles & Philosophy
The RevWorkForce application adheres to a rigorous isolated Unit Testing strategy and a comprehensive Integration Testing coverage mapping. The ultimate philosophy revolves around averting logical regressions, strictly enforcing boundary values, and preventing data leakage across unauthorized roles.
- **Unit Isolation**: Service components (`ServiceImpl`) are tested strictly in isolation. External dependencies and Data Access Layers (`Repositories`) must be totally mocked using Mockito.
- **Endpoint Assurance**: Web Endpoints (`Controllers`) test HTTP layer mapping, exception interceptors, and data binding boundaries utilizing Spring Boot `MockMvc`.
- **Target Line Coverage**: Hard floor of **80% total codebase coverage**. Core business modules (Leave, Performance) demand **100% Branch Coverage**.

---

## 2. Deep Dive: Behavioral Testing Scenarios

### A. Leave Management Logic (`LeaveServiceImplTest`)
The mathematical operations parsing requested days are incredibly susceptible to off-by-one regressions. Focus tests include:
1. **The Overlap Algorithm**:
   - Condition 1: Attempt to insert a leave fully enclosed inside an existing approved leave. -> Assert `Throw Exception`
   - Condition 2: Attempt to insert a leave overlapping the exact Start or End boundaries of an existing leave. -> Assert `Throw Exception`
   - Condition 3: Attempt to insert a leave immediately contiguous, but not overlapping. -> Assert `Success`
2. **Balance Exhaustion Safeguards**:
   - Given a balance of `N`. Attempting to allocate `N+1` days must fail early logic gating.
3. **Weekend Ignore Parsing**:
   - Given a leave requesting 14 calendar days, the mathematical accumulator must subtract the 4 weekend days occurring in the span, requesting only 10 total balance deductions.

### B. Performance Review Logic (`PerformanceReviewServiceImplTest`)
State machines govern the sequential flow of evaluations. Focus tests include:
1. **State Bypassing Preventions**:
   - Assert testing that invoking `finalizeReview()` (a Manager Operation) over a Record holding `PENDING_SELF_ASSESSMENT` status forces an `IllegalStateException` preventing sequence breaking.
2. **Evaluation Metric Mathematical Outputs**:
   - Test floating-point precision on the `BigDecimal` accumulation of all sub-ratings (Technical, Punctual, etc.) to ensure accurate weighted `overallRating` assignments.

### C. Goal State Iterations (`GoalServiceImplTest`)
1. **Variable Input Bounding**:
   - Submitting `progress %` values natively mapped as `< 0` or `> 100` must fail DTO validations.
2. **Automatic Triggers**:
   - Pushing the state via `.updateProgress(..., 100)` must natively assert that the returned state object maps `.getStatus()` as `GoalStatus.COMPLETED` alongside valid `completedAt` timestamps without explicit client instruction.

### D. Security Validations & Endpoints (`Controller` Layer)
All integration tests using `@WebMvcTest` must invoke `@WithMockUser()` contexts.
1. **Unauthorized Endpoint Bleed Check**:
   - Send requests targeting `/admin/reports` acting with `@WithMockUser(roles="EMPLOYEE")`. Assert exact `403 FORBIDDEN` returns.
2. **Authentication Injection Simulation**:
   - Mock Principal contexts to possess specific Employee IDs. Make request against a differing Employee ID's endpoints and verify rejection handling (preventing horizontal privilege escalation).

---

## 3. Technology Stack Requirements
- **Testing Engine**: `JUnit 5` (Jupiter) orchestrating lifecycle setup.
- **Assertion Engine**: `AssertJ` for rich, chaining semantic evaluations (`assertThat(x).isEqualTo(y)`).
- **Service Isolation**: `Mockito` frameworks utilizing `@Mock`, `@InjectMocks`, and `ArgumentCaptor` classes to trace payload shapes moving through repositories.
- **Integration Framework**: Spring Boot Test Contexts (`@WebMvcTest`, `@DataJpaTest`).
- **Telemetry Processing**: `JaCoCo` (Java Code Coverage) Maven plugin.

---

## 4. Execution Configurations and CI/CD Triggers
### Local Invocation
Developers run identical suites via standard Maven commands.
```bash
# Execute entire suite
mvn clean test

# Execute targeted isolated module
mvn test -Dtest=PerformanceReviewServiceImplTest
```

### Build Pipelines
1. **Gating Check**: The continuous integration pipeline is programmed to execute `mvn test jacoco:report` intrinsically.
2. **Failure Mechanics**: 
   - ANY singular test failure results in a Pipeline Hard-Failure.
   - If the outputted JaCoCo `Coverage Ratio` metric demonstrates a reduction from the main-branch baseline, the Pull Request triggers an explicit warning log enforcing review.

---

## 5. Mocking vs Database Realism Protocol
1. **Never mock DTOs or simple Entities**: Always instantiate real domain objects (`new Employee()`) when filling out dummy variables to represent active data. 
2. **Never connect to Databases for Service tests**: Service logic must be deterministic. Invoking real database layers creates brittle tests reliant on environment setups. Always map Repository connections out with `Mockito.when().thenReturn()`.
