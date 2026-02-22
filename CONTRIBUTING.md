# Contributing to RevWorkForce

This document contains the guidelines that all team members must follow for development of RevWorkForce Phase 2 Project.
## Getting Started

1. **Fork the repository**
2. **Clone your fork**
```bash
   git clone https://github.com/RevWorkForce-PES/P2-RevWorkForce.git
```
3. **Add upstream remote**
```bash
   git remote add upstream https://github.com/RevWorkForce-PES/P2-RevWorkForce.git
```

## Development Workflow

### 1. Create a Feature Branch
```bash
git checkout main
git pull upstream main
git checkout -b feature/add-your-feature-name
```

Branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `hotfix/` - Urgent production fixes
- `test/` - Test additions
- `docs/` - Documentation

### 2. Make Changes

- Write clean, readable code
- Follow Java coding standards
- Add JavaDoc comments
- Write unit tests (aim for 60%+ coverage)
- Test your changes locally

### 3. Commit Changes
```bash
git add .
git commit -m "feat: add employee search functionality"
```

Commit message format:
```
<type>: <description>
```

Types:
- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation
- `style` - Formatting
- `refactor` - Code restructuring
- `test` - Tests
- `chore` - Build/config

### 4. Keep Your Branch Updated
```bash
git fetch upstream
```
### 5. Rebase Before Creating PR

Always rebase your branch with latest main before creating a Pull Request.

Why?
To avoid merge conflicts and messy commit history.

```bash
git checkout main
git pull upstream main
git checkout feature/your-feature-name
git rebase main
```

### 5. Push Changes
```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

1. Go to your fork GitHub repository
2. Click "Contribute/New Pull Request"
3. Select your branch
4. Add PR title and PR description with your changes
5. Request review from team members

## Pull Request Guidelines
### Code Review Policy

- Minimum 1 approval required before merge
- All tests must pass
- No direct push to main branch
- Resolve all review comments before merging

### PR Title Format
```
[Module] Brief description

Examples:
[Auth] Implement login functionality
[Leave] Fix balance calculation bug
[UI] Update dashboard layout
```

### PR Description Template
```markdown
## Description
Brief description of changes

## Type of Change
- Bug fix
- New feature
- Breaking change
- Documentation update

## Add Related Issue
- Closes #[issue-number]

## Checklist
- Code follows style guidelines
- Self-review completed
- Comments added to complex code
- Documentation updated
- No new warnings
```

## Code Style Guidelines

### Java Code Style

#### 1) Code Formatting Rules

- Remove unused imports
- No wildcard imports (*)
- Format code before commit (Ctrl + Alt + L in IntelliJ)
- Do not leave commented dead code

#### 2) Variables
- Use meaningful variable names in camelCase
- Avoid single-letter names
```java
String employeeId = "EMP001";  //  Good
String e = "EMP001";           //  Bad
```

#### 3) Constants
- Use UPPER_SNAKE_CASE
- Declare as `public static final`
```java
public static final int MAX_LEAVE_DAYS = 30;
public static final String DEFAULT_ROLE = "EMPLOYEE";
```

#### 4) Methods
- Use camelCase
- Method names should start with a verb
- Keep methods small and focused (SRP - Single Responsibility Principle)

```java

public void calculateLeaveBalance() { }
```

#### 5) Classes in PascalCase
- Use PascalCase
- Use nouns
- Class names should be meaningful
```java

public class EmployeeService { }

public class LeaveRequestController { }
```

- Controller → `EmployeeController`
- Service → `EmployeeService`
- Repository → `EmployeeRepository`
- DTO → `EmployeeRequest`, `EmployeeResponse`

#### 6) Packages
- Use lowercase
- Follow reverse domain naming convention
```java
com.revature.revworkforce.model
com.revature.revworkforce.repository
com.revature.revworkforce.controller
com.revature.revworkforce.exceptions
```

#### 7) JavaDocS and Comments

- Mandatory JavaDoc for:
  - All public classes
  - All public methods
  - Complex business logic
- Use clear and concise inline comments
- Avoid unnecessary or obvious comments
- Remove commented-out dead code

```java
/**
 * Calculates working days between two dates.
 * 
 * @param startDate the start date
 * @param endDate the end date
 * @return number of working days
 */
public int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
    // implementation
}
```

#### 8) Logging Guidelines (Log4j2)
- Use parameterized logging
- Log meaningful context
- Do NOT use `System.out.println()`
```java
logger.info("Creating employee with ID: {}", employee.getEmployeeId());
logger.warn("Employee not found with ID: {}", id);
logger.error("Error while creating employee", ex);
```
| Level | When to Use                       |
| ----- | --------------------------------- |
| TRACE | Detailed internal debugging       |
| DEBUG | Development debugging             |
| INFO  | Important business events         |
| WARN  | Unexpected but handled situations |
| ERROR | Failures and exceptions           |


#### 9) API Layering Rule 
- Follow strict layering: Controller → Service → Repository
- Controller and Repository should NOT contain business logic
- Business logic must be inside Service layer

**HTTP Request Types:**

| HTTP Method | Purpose                | Example Use Case             | Expected Status Code |
|------------|------------------------|------------------------------|----------------------|
| **GET**    | Retrieve data          | Fetch employee details       | 200 OK               |
| **POST**   | Create new resource    | Create new employee          | 201 CREATED          |
| **PUT**    | Update entire resource | Update full employee details | 200 OK               |
| **PATCH**  | Partial update         | Update employee email only   | 200 OK               |
| **DELETE** | Remove resource        | Delete employee record       | 204 NO CONTENT       |

**Proper HTTP Status Codes:**
| Operation    | Status         |
| ------------ | -------------- |
| Create       | 201 CREATED    |
| Fetch        | 200 OK         |
| Update       | 200 OK         |
| Delete       | 204 NO CONTENT |
| Bad Request  | 400            |
| Unauthorized | 401            |
| Forbidden    | 403            |
| Not Found    | 404            |
| Conflict     | 409            |

#### 10) DTO Rule

- Do NOT return Entity objects directly from Controller
- Always use Request and Response DTOs
- Convert Entity ↔ DTO inside Service layer

Example:
Controller → EmployeeRequest  
Service → Converts to Employee entity  
Controller returns → EmployeeResponse

#### 11) Exception Handling
- Use custom exceptions
- Use Global Exception Handler (@ControllerAdvice)
- Do NOT use try-catch in every controller
```java
public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
```

#### 12) Global Exception Handling

Do NOT write try-catch in every controller.

Use a Global Exception Handler:

- Create custom exceptions
- Handle them using @ControllerAdvice
- Return proper HTTP status codes

## Testing Guidelines

### Unit Tests
- Follow AAA Approach
```java
@Test
public void testCalculateWorkingDays_ExcludesWeekends() {
    // Arrange
    LocalDate start = LocalDate.of(2024, 2, 5);  // Monday
    LocalDate end = LocalDate.of(2024, 2, 9);    // Friday
    
    // Act
    int days = dateUtil.calculateWorkingDays(start, end);
    
    // Assert
    assertEquals(5, days);
}
```

### Test Coverage

- Aim for 60%+ overall coverage
- Critical business logic should have 80%+ coverage
- Run `mvn jacoco:report` to check coverage

### Definition of Done

A task is complete only when:

- Code implemented
- Unit tests written
- All tests passing
- Coverage requirement met
- Code reviewed
- No warnings/errors

## Responding to Reviews

- Be open to feedback
- Respond to all comments
- Make requested changes promptly
- Thank reviewers for their time

## Communication

### Daily Standup (Async)
Post in Whatsapp/Teams/Slack/Discord:
```
Yesterday: [What you completed]
Today: [What you're working on]
Blockers: [Any issues]
```

### Asking for Help
```
Problem: [Clear description]
What I tried: [Steps taken]
Expected: [What should happen]
Actual: [What actually happens]
Code: [Relevant code snippet or link]
```

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

## Questions?

- Create an issue with the `question` label
- Ask in team Whatsapp/Slack/Discord channel
- Contact team lead


**Thank you for efforts!**
