package com.adme.learning.loan.tech.api.agreement.entity;

/**
 * Represents the type of interest rate.
 */
public enum InterestRateType {
    FIXED("Fixed Interest Rate"),
    VARIABLE("Variable Interest Rate");

    private final String displayName;

    InterestRateType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}