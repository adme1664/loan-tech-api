package com.adme.learning.loan.tech.api.agreement.boundary;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, String identifier) {
        super(String.format("%s already exists with identifier: %s", resourceType, identifier));
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
