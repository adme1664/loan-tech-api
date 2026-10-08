package com.adme.learning.loan.tech.api.agreement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents interest rate information for an agreement.
 * Maps to tbl_interest_rate in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_interest_rate", schema = "commercialloan",
        uniqueConstraints = @UniqueConstraint(columnNames = {"agreement_id", "effective_dt"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "interest_rate_id", nullable = false)
    private UUID interestRateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_rate_type", nullable = false, length = 10)
    private InterestRateType interestRateType;

    @Column(name = "interest_rate", nullable = false, precision = 9, scale = 6)
    private BigDecimal interestRate;

    @Column(name = "base_rate", precision = 9, scale = 6)
    private BigDecimal baseRate;

    @Column(name = "spread_rate", precision = 9, scale = 6)
    private BigDecimal spreadRate;

    @Column(name = "effective_dt", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "sys_ins_ts", nullable = false, updatable = false)
    private ZonedDateTime systemInsertTimestamp;

    @Column(name = "sys_upd_ts")
    private ZonedDateTime systemUpdateTimestamp;

    @Column(name = "sys_ins_process", nullable = false, length = 100)
    private String systemInsertProcess;

    @Column(name = "sys_upd_process", length = 100)
    private String systemUpdateProcess;

    @PrePersist
    protected void onCreate() {
        systemInsertTimestamp = ZonedDateTime.now();
        validateRateType();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = ZonedDateTime.now();
        validateRateType();
    }

    private void validateRateType() {
        if (interestRateType == InterestRateType.VARIABLE) {
            if (baseRate == null || spreadRate == null) {
                throw new IllegalStateException("Variable interest rate must have both base rate and spread rate");
            }
        }
    }

    public boolean isFixed() {
        return InterestRateType.FIXED.equals(interestRateType);
    }

    public boolean isVariable() {
        return InterestRateType.VARIABLE.equals(interestRateType);
    }

    public BigDecimal calculateEffectiveRate() {
        if (isFixed()) {
            return interestRate;
        }
        return baseRate.add(spreadRate);
    }
}