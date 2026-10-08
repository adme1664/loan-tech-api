package com.adme.learning.loan.tech.api.agreement.entity;

/**
 * Represents the status of a loan agreement.
 */
public enum AgreementStatus {
    EN_ATTENTE("Pending"),
    OUVERT("Open"),
    SUSPENDU("Suspended"),
    FERME("Closed");

    private final String displayName;

    AgreementStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}