package com.adme.learning.loan.tech.api.agreement.entity;

/**
 * Represents the type of balance or accrued interest.
 */
public enum BalanceType {
    ACCRUED_INT_DAILY("Daily Accrued Interest"),
    ACCRUED_INT_CUMULATED("Cumulated Accrued Interest");

    private final String displayName;

    BalanceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}