package com.revature.revworkforce.exception;

import com.revature.revworkforce.model.Employee;
import com.revature.revworkforce.repository.EmployeeRepository;
import com.revature.revworkforce.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webRequest.getDescription(false)).thenReturn("test path");
    }

    @Test
    void handleResourceNotFoundException_Returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleResourceNotFoundException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Not found");
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
    }

    @Test
    void handleEmployeeNotFoundException_Returns404() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("Employee not found");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleEmployeeNotFoundException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("Employee Not Found");
    }

    @Test
    void handleValidationException_Returns400() {
        ValidationException ex = new ValidationException("Validation failed");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleValidationException(ex,
                webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Validation Error");
    }

    @Test
    void handleAuthenticationException_Returns401() {
        AuthenticationException ex = new AuthenticationException("Auth failed");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleAuthenticationException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError()).isEqualTo("Authentication Failed");
    }

    @Test
    void handleAccountLockedException_Returns401() {
        AccountLockedException ex = new AccountLockedException(java.time.LocalDateTime.now().plusHours(1));
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleAccountLockedException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError()).isEqualTo("Account Locked");
    }

    @Test
    void handleUnauthorizedException_Returns403() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleUnauthorizedException(ex,
                webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().getError()).isEqualTo("Access Denied");
    }

    @Test
    void handleDuplicateResourceException_Returns409() {
        DuplicateResourceException ex = new DuplicateResourceException("exists");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleDuplicateResourceException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("Resource Already Exists");
    }

    @Test
    void handleDatabaseException_Returns500() {
        DatabaseException ex = new DatabaseException("DB error");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleDatabaseException(ex,
                webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError()).isEqualTo("Database Error");
    }

    @Test
    void handleInsufficientLeaveBalanceException_Returns400() {
        InsufficientLeaveBalanceException ex = new InsufficientLeaveBalanceException("CL", 5, 2);
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleInsufficientLeaveBalanceException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Insufficient Leave Balance");
    }

    @Test
    void handleLeaveOverlapException_Returns400() {
        java.time.LocalDate d = java.time.LocalDate.now();
        LeaveOverlapException ex = new LeaveOverlapException(d, d.plusDays(1), d, d.plusDays(1));
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleLeaveOverlapException(ex,
                webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Leave Overlap");
    }

    @Test
    void handleInvalidStatusTransitionException_Returns400() {
        InvalidStatusTransitionException ex = new InvalidStatusTransitionException("Leave", "APPROVED", "PENDING");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler
                .handleInvalidStatusTransitionException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Invalid Status Transition");
    }

    @Test
    void handleGlobalException_Returns500() {
        Exception ex = new Exception("Internal error");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleGlobalException(ex,
                webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
    }
}
