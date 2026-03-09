package com.revature.revworkforce.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    // ===============================
    // EMPTY STRING TEST
    // ===============================

    @Test
    void isEmpty_Null_ReturnTrue() {
        assertTrue(ValidationUtil.isEmpty(null));
    }

    @Test
    void isEmpty_Blank_ReturnTrue() {
        assertTrue(ValidationUtil.isEmpty("   "));
    }

    @Test
    void isNotEmpty_Value_ReturnTrue() {
        assertTrue(ValidationUtil.isNotEmpty("hello"));
    }

    // ===============================
    // EMAIL VALIDATION
    // ===============================

    @Test
    void isValidEmail_Valid_ReturnTrue() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
    }

    @Test
    void isValidEmail_Invalid_ReturnFalse() {
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
    }

    // ===============================
    // PHONE VALIDATION
    // ===============================

    @Test
    void isValidPhone_ValidIndianNumber() {
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
    }

    @Test
    void isValidPhone_InvalidNumber() {
        assertFalse(ValidationUtil.isValidPhone("12345"));
    }

    // ===============================
    // EMPLOYEE ID VALIDATION
    // ===============================

    @Test
    void isValidEmployeeId_Valid() {
        assertTrue(ValidationUtil.isValidEmployeeId("EMP001"));
    }

    @Test
    void isValidEmployeeId_Invalid() {
        assertFalse(ValidationUtil.isValidEmployeeId("emp1"));
    }

    // ===============================
    // PASSWORD VALIDATION
    // ===============================

    @Test
    void isValidPassword_StrongPassword() {
        assertTrue(ValidationUtil.isValidPassword("Password1"));
    }

    @Test
    void isValidPassword_WeakPassword() {
        assertFalse(ValidationUtil.isValidPassword("password"));
    }

    // ===============================
    // POSTAL CODE
    // ===============================

    @Test
    void isValidPostalCode_Valid() {
        assertTrue(ValidationUtil.isValidPostalCode("500001"));
    }

    @Test
    void isValidPostalCode_Invalid() {
        assertFalse(ValidationUtil.isValidPostalCode("000123"));
    }

    // ===============================
    // NUMBER VALIDATION
    // ===============================

    @Test
    void isPositive_PositiveNumber() {
        assertTrue(ValidationUtil.isPositive(10));
    }

    @Test
    void isPositive_NegativeNumber() {
        assertFalse(ValidationUtil.isPositive(-5));
    }

    @Test
    void isNonNegative_Zero() {
        assertTrue(ValidationUtil.isNonNegative(0));
    }

    @Test
    void isInRange_IntegerValid() {
        assertTrue(ValidationUtil.isInRange(5, 1, 10));
    }

    @Test
    void isInRange_DoubleValid() {
        assertTrue(ValidationUtil.isInRange(3.5, 1.0, 5.0));
    }

    // ===============================
    // DATE VALIDATION
    // ===============================

    @Test
    void isValidDateRange_Valid() {

        LocalDate start = LocalDate.of(2024,1,1);
        LocalDate end = LocalDate.of(2024,1,10);

        assertTrue(ValidationUtil.isValidDateRange(start,end));
    }

    @Test
    void isValidDateRange_Invalid() {

        LocalDate start = LocalDate.of(2024,1,10);
        LocalDate end = LocalDate.of(2024,1,1);

        assertFalse(ValidationUtil.isValidDateRange(start,end));
    }

    @Test
    void isFutureDate_ReturnTrue() {

        LocalDate future = LocalDate.now().plusDays(1);

        assertTrue(ValidationUtil.isFutureDate(future));
    }

    @Test
    void isNotPastDate_ReturnTrue() {

        LocalDate today = LocalDate.now();

        assertTrue(ValidationUtil.isNotPastDate(today));
    }

    // ===============================
    // AGE VALIDATION
    // ===============================

    @Test
    void isValidAge_Adult() {

        LocalDate dob = LocalDate.now().minusYears(20);

        assertTrue(ValidationUtil.isValidAge(dob));
    }

    @Test
    void isValidAge_Minor() {

        LocalDate dob = LocalDate.now().minusYears(10);

        assertFalse(ValidationUtil.isValidAge(dob));
    }

    // ===============================
    // STRING LENGTH
    // ===============================

    @Test
    void isValidLength_Valid() {

        assertTrue(ValidationUtil.isValidLength("Hello",2,10));
    }

    @Test
    void isValidLength_Invalid() {

        assertFalse(ValidationUtil.isValidLength("Hi",5,10));
    }

    // ===============================
    // CHARACTER VALIDATION
    // ===============================

    @Test
    void isAlphabetic_Valid() {

        assertTrue(ValidationUtil.isAlphabetic("Hello"));
    }

    @Test
    void isAlphabeticWithSpaces_Valid() {

        assertTrue(ValidationUtil.isAlphabeticWithSpaces("Hello World"));
    }

    @Test
    void isNumeric_Valid() {

        assertTrue(ValidationUtil.isNumeric("123456"));
    }

    @Test
    void isAlphanumeric_Valid() {

        assertTrue(ValidationUtil.isAlphanumeric("Hello123"));
    }

    // ===============================
    // SANITIZE
    // ===============================

    @Test
    void sanitize_RemovesSpaces() {

        assertEquals("hello", ValidationUtil.sanitize("  hello  "));
    }

    // ===============================
    // BUSINESS VALIDATIONS
    // ===============================

    @Test
    void isValidRating_Valid() {

        assertTrue(ValidationUtil.isValidRating(4.5));
    }

    @Test
    void isValidProgress_Valid() {

        assertTrue(ValidationUtil.isValidProgress(80));
    }

    @Test
    void isValidSalary_Valid() {

        assertTrue(ValidationUtil.isValidSalary(50000.0));
    }

    @Test
    void isValidLeaveDays_Valid() {

        assertTrue(ValidationUtil.isValidLeaveDays(5,10));
    }

    @Test
    void isValidLeaveDays_Invalid() {

        assertFalse(ValidationUtil.isValidLeaveDays(15,10));
    }

}
