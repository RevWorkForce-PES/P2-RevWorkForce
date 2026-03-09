package com.revature.revworkforce.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    // ============================================
    // APPLICATION CONSTANTS
    // ============================================

    @Test
    void applicationConstants_ShouldMatchExpectedValues() {

        assertEquals("RevWorkForce", Constants.APPLICATION_NAME);
        assertEquals("1.0.0", Constants.APPLICATION_VERSION);
        assertEquals("Revature", Constants.COMPANY_NAME);
    }

    // ============================================
    // ROLE CONSTANTS
    // ============================================

    @Test
    void roleConstants_ShouldMatchExpectedValues() {

        assertEquals("ADMIN", Constants.ROLE_ADMIN);
        assertEquals("MANAGER", Constants.ROLE_MANAGER);
        assertEquals("EMPLOYEE", Constants.ROLE_EMPLOYEE);
    }

    @Test
    void springRoleConstants_ShouldContainPrefix() {

        assertEquals("ROLE_ADMIN", Constants.SPRING_ROLE_ADMIN);
        assertEquals("ROLE_MANAGER", Constants.SPRING_ROLE_MANAGER);
        assertEquals("ROLE_EMPLOYEE", Constants.SPRING_ROLE_EMPLOYEE);
    }

    // ============================================
    // LEAVE CONSTANTS
    // ============================================

    @Test
    void leaveTypeConstants_ShouldMatchExpectedValues() {

        assertEquals("CL", Constants.LEAVE_TYPE_CL);
        assertEquals("SL", Constants.LEAVE_TYPE_SL);
        assertEquals("PL", Constants.LEAVE_TYPE_PL);
        assertEquals("PRIV", Constants.LEAVE_TYPE_PRIV);
    }

    @Test
    void leaveBalanceDefaults_ShouldBeValid() {

        assertEquals(12, Constants.DEFAULT_CL_DAYS);
        assertEquals(12, Constants.DEFAULT_SL_DAYS);
        assertEquals(18, Constants.DEFAULT_PL_DAYS);
        assertEquals(15, Constants.DEFAULT_PRIV_DAYS);
    }

    // ============================================
    // EMPLOYEE CONSTANTS
    // ============================================

    @Test
    void employeeStatusConstants_ShouldMatchExpectedValues() {

        assertEquals("ACTIVE", Constants.STATUS_ACTIVE);
        assertEquals("INACTIVE", Constants.STATUS_INACTIVE);
        assertEquals("ON_LEAVE", Constants.STATUS_ON_LEAVE);
        assertEquals("TERMINATED", Constants.STATUS_TERMINATED);
    }

    @Test
    void employeeConstraints_ShouldBeValid() {

        assertEquals(18, Constants.MIN_AGE);
        assertEquals(65, Constants.MAX_AGE);
        assertEquals(8, Constants.MIN_PASSWORD_LENGTH);
    }

    // ============================================
    // PERFORMANCE REVIEW CONSTANTS
    // ============================================

    @Test
    void ratingConstants_ShouldBeValid() {

        assertEquals(1.0, Constants.MIN_RATING);
        assertEquals(5.0, Constants.MAX_RATING);
        assertEquals(0.5, Constants.RATING_INCREMENT);
    }

    // ============================================
    // GOAL CONSTANTS
    // ============================================

    @Test
    void goalProgressConstants_ShouldBeValid() {

        assertEquals(0, Constants.MIN_PROGRESS);
        assertEquals(100, Constants.MAX_PROGRESS);
        assertEquals(50, Constants.PROGRESS_IN_PROGRESS);
    }

    // ============================================
    // SECURITY CONSTANTS
    // ============================================

    @Test
    void securityConstants_ShouldBeValid() {

        assertEquals(12, Constants.BCRYPT_STRENGTH);
        assertEquals(5, Constants.MAX_FAILED_LOGIN_ATTEMPTS);
        assertEquals(30, Constants.SESSION_TIMEOUT_MINUTES);
    }

    // ============================================
    // FILE UPLOAD CONSTANTS
    // ============================================

    @Test
    void fileUploadConstants_ShouldBeValid() {

        assertEquals(10, Constants.MAX_FILE_SIZE_MB);
        assertTrue(Constants.MAX_FILE_SIZE_BYTES > 0);
        assertNotNull(Constants.ALLOWED_IMAGE_TYPES);
        assertNotNull(Constants.ALLOWED_DOCUMENT_TYPES);
    }

    // ============================================
    // PRIVATE CONSTRUCTOR TEST
    // ============================================

    @Test
    void constructor_ShouldThrowException() throws Exception {

        Constructor<Constants> constructor =
                Constants.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        Exception exception = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                constructor::newInstance
        );

        assertTrue(exception.getCause() instanceof UnsupportedOperationException);

        assertEquals(
                "This is a utility class and cannot be instantiated",
                exception.getCause().getMessage()
        );
    }
}
