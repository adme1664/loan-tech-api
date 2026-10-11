package com.adme.learning.loan.tech.api.agreement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents interest rate information for an agreement.
 * Maps to tbl_interest_rate in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_interest_rate", schema = "commercialloan")
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

    @Column(name = "interest_rate_type", nullable = false, length = 10)
    private String interestRateType;

    @Column(name = "interest_rate", nullable = false, precision = 9, scale = 6)
    private BigDecimal interestRate;

    @Column(name = "base_rate", precision = 9, scale = 6)
    private BigDecimal baseRate;

    @Column(name = "spread_rate", precision = 9, scale = 6)
    private BigDecimal spreadRate;

    @Column(name = "effective_dt", nullable = false)
    private Instant effectiveDate;

    @Column(name = "sys_ins_ts", nullable = false, updatable = false)
    private Instant systemInsertTimestamp;

    @Column(name = "sys_upd_ts")
    private Instant systemUpdateTimestamp;

    @Column(name = "sys_ins_process", nullable = false, length = 100)
    private String systemInsertProcess;

    @Column(name = "sys_upd_process", length = 100)
    private String systemUpdateProcess;

    @PrePersist
    protected void onCreate() {
        systemInsertTimestamp = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = Instant.now();
    }

}