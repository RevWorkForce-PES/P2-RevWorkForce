# DEPENDENCIES

## 1) `spring-boot-starter-web`

**Purpose:**
Provides everything needed to build REST APIs and MVC web applications.

**Includes internally:**

* Spring MVC
* Embedded Tomcat
* Jackson (JSON serialization)
* Validation integration

**Why we need it:**

* For `@Controller` / `@RestController`
* To handle HTTP requests
* To expose REST endpoints


## 2) `spring-boot-starter-thymeleaf`

**Purpose:**
Adds Thymeleaf template engine support.

**Why we need it:**

* Server-side rendered HTML pages
* Form handling
* View templates for HR management UI

## 3) `spring-boot-starter-data-jpa`

**Purpose:**
Provides JPA + Hibernate support.

**Includes:**

* Hibernate ORM
* Spring Data JPA
* Transaction management

**Why we need it:**

* Database interaction
* Repository layer implementation
* Entity mapping
* CRUD operations

## 4) `spring-boot-starter-security`

**Purpose:**
Provides authentication & authorization framework.

**Includes:**

* Spring Security core
* Filter chain
* Password encoders
* Role-based access control

**Why we need it:**

* Login system
* Role-based permissions (Admin / HR / Employee)
* Secured endpoints


## 5) `spring-boot-starter-validation`

**Purpose:**
Provides Bean Validation (Jakarta Validation).

**Why we need it:**

* DTO validation
* `@NotNull`
* `@Email`
* `@Size`
* Form validation


## 6) `spring-boot-devtools`

**Purpose:**
Developer productivity tool.

**Features:**

* Auto restart on code change
* Live reload

**Scope:** runtime only
Not included in production build.


## 7) `thymeleaf-extras-springsecurity6`

**Purpose:**
Integrates Spring Security with Thymeleaf.

**Why we need it:**

* Show/hide UI based on roles:

```html
sec:authorize="hasRole('ADMIN')"
```


## 8) `ojdbc11` (Oracle Driver)

**Purpose:**
Allows application to connect to Oracle database.

**Scope:** runtime
Not needed at compile time.

## 9) `spring-security-crypto`

**Purpose:**
Provides password encryption utilities.

**Why we need it:**

* BCrypt hashing
* Secure password storage

## 10) `lombok`

**Purpose:**
Reduces boilerplate code.

**Replaces:**

* Getters
* Setters
* Constructors
* toString
* Builder pattern

**Important:**
Compile-time only. Not packaged in final JAR.


##  Testing Dependencies

### `spring-boot-starter-test`

Includes:

* JUnit 5
* Mockito
* AssertJ
* Spring Test

Used for:

* Unit testing
* Integration testing

### `spring-security-test`

Used for:

* Testing secured endpoints
* Mock authentication
* Role-based testing

#  BUILD PLUGINS 


## 1) `spring-boot-maven-plugin`

**Purpose:**

* Builds executable JAR
* Embeds Tomcat
* Enables `mvn spring-boot:run`

Without this → app will not start as a Spring Boot application.


## 2) `jacoco-maven-plugin`

**Purpose:**
Code coverage analysis.

**What it does:**

* Tracks test coverage
* Generates HTML report
* Fails build if coverage < 60%

**Why important:**
Enforces test quality standards across team.


## 3) `maven-pmd-plugin`

**Purpose:**
Static code analysis.

**Detects:**

* Code smells
* Dead code
* Bad practices
* Design issues

Improves maintainability.


## 4) `maven-checkstyle-plugin`

**Purpose:**
Enforces code style rules.

**Ensures:**

* Consistent formatting
* Naming conventions
* Clean code standards

Important for team collaboration.

## 5)  `maven-surefire-plugin`

**Purpose:**
Runs unit tests during `mvn test`.

Handles:

* Test execution
* Reporting
* Failure detection

