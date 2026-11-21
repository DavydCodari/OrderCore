package org.example.ordercore.common.util;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

public final class Validation {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private Validation() {
    }

    public static <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        return value;
    }

    public static <T> T validateNull(T value, String fieldName) {
        if (value != null) {
            throw new IllegalArgumentException(fieldName + " must be null");
        }
        return value;
    }

    public static String validateNotBlank(String value, String fieldName) {
        validateNotNull(value, fieldName);

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }

        return value;
    }

    public static String validateNotBlankAndNotExceedingLength(String value, int maxLength, String fieldName){
        validateNotBlank(value, fieldName);

        if (value.length() > maxLength){
            throw new IllegalArgumentException(fieldName + " exceeds maximum length");
        }

        return value;
    }

    public static String validateNotExceedingLength(String value, int maxLength, String fieldName){
        if (value != null) {
            if (value.length() > maxLength) {
                throw new IllegalArgumentException(fieldName + " exceeds maximum length");
            }
        }

        return value;
    }

    public static String validateEmail(String email) {
        validateNotBlankAndNotExceedingLength(email,255,"Email");

        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return email;
    }

    public static BigDecimal validateBigDecimal(BigDecimal value, String fieldName){
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " must be nonnegative");
        }

        if (value.scale() > 2) {
            throw new IllegalArgumentException(
                    fieldName + " cannot have more than 2 decimal places"
            );
        }

        int integerDigits = value.precision() - value.scale();

        if (integerDigits > 10) {
            throw new IllegalArgumentException(
                    fieldName + " exceeds the supported precision"
            );
        }

        return value;
    }

    public static boolean isConstraintViolation(
            DataIntegrityViolationException exception,
            String constraintName
    ) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException cve
                    && constraintName.equals(cve.getConstraintName())) {
                return true;
            }

            cause = cause.getCause();
        }

        return false;
    }
}
