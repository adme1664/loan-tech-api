package com.adme.learning.loan.tech.api.agreement.boundary;

/**
 * Exception thrown when business validation rules are violated.
 */
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
