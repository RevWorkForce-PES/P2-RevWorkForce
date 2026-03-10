# Comprehensive Test Strategy

## 1. Core Principles
The application adopts an isolated testing strategy prioritizing validation of complex inter-service logic arrays over simple getter/setter verification, aiming to prevent logical regressions against corner-data scenarios.
- Cover all branches inside `ServiceImpl` components mapped against boundaries using strict JUnit + Mockito behaviors.
- Use Integration tests via `MockMvc` mapped across Data Transfer Object binding edge cases.

## 2. Deep Focus Testing Scenarios

### A. Leave Service Edge Cases (`LeaveServiceImplTest`)
1. **Overlap Preventions**: Must simulate existing approved lists spanning Dates X to Z, and test submitting arrays of requests: Valid requests strictly before X, strictly after Z. And error-throwing requests crossing X or Z.
2. **Balance Exhaustions**: Inject a `LeaveBalance` of 2. Verify requesting 2 days hits `.save()`. Verify requesting 3 days strictly throws `LeaveAllocationException`.
3. **Cross Role Approvals**: Validating that employee X cannot approve a leave assigned to themselves, nor can employee Y approve team members of employee Z.
4. **Weekend Math**: Leave length calculators must correctly ignore standard weekends against requested holiday lengths.

### B. Performance Review Logic (`PerformanceReviewServiceImplTest`)
1. **Status Linear Progressions**: Simulate attempts to skip statuses (e.g., A manager attempting to finalize a review that is currently `PENDING_SELF_ASSESSMENT` before the employee fills it). Must throw `IllegalStateException`.
2. **Evaluation Metrics Computation**: Test the math boundaries of `overallRating` calculations based on the aggregate weighting of sub-scores (Punctuality vs Technical capability) under `finalizeReview()`.

### C. Goal State Iterations (`GoalServiceImplTest`)
1. **Progress Bounds**: Validate that providing `progress %` < 0 or > 100 via HTTP payloads is reliably trapped by DTO constraints before ever striking the DB layer.
2. **Automatic Completion Parsing**: Validate that when a Manager inputs integer `100` via update methods, the state enum immediately shifts from `IN_PROGRESS` to `COMPLETED` and timestamp values log correctly.

### D. Security Validations (`Controller` layer limits)
1. **Unauthorized Endpoint Bleed**: Confirm via Integration Test configuration that sending `/manager/reviews` routes as a standard `Employee` role yields unyielding HTTP `403 FORBIDDEN` regardless of JWT/session validity.
2. **JWT Parameter Scrambling**: Confirm payload tokens injected with valid IDs but mismatched signatures fail authorization layers.

## 3. Recommended Tools
- **Unit Assertion**: `org.junit.jupiter.api.*`, `org.assertj.core.api.Assertions.*`
- **Mocking**: `@Mock`, `@InjectMocks`, `ArgumentMatchers`
- **Controller Test Bootstrapping**: `@WebMvcTest(...)`, `MockMvcRequestBuilders.*`, `MockMvcResultMatchers.*`
- **Coverage Tool**: Jacoco Maven Plugin.

## 4. Automation and CI Criteria
1. The developer workflow is defined by `mvn test`. The build lifecycle explicitly fails if ANY unit test fails. 
2. A commit that negatively impacts the total coverage percentage line compared against the Main branch benchmark should be aborted locally via Git-hook configuration.
