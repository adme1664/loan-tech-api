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
 * Represents balance and accrued interest information for an agreement.
 * Maps to tbl_balance in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_balance", schema = "commercialloan",
        uniqueConstraints = @UniqueConstraint(columnNames = {"agreement_id", "balance_type", "balance_date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "balance_id", nullable = false)
    private UUID balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @Enumerated(EnumType.STRING)
    @Column(name = "balance_type", nullable = false, length = 30)
    private BalanceType balanceType;

    @Column(name = "credit_amnt", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "debit_amnt", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "balance_date", nullable = false)
    private LocalDate balanceDate;

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
        validateAmounts();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = ZonedDateTime.now();
        validateAmounts();
    }

    private void validateAmounts() {
        if (creditAmount == null) {
            creditAmount = BigDecimal.ZERO;
        }
        if (debitAmount == null) {
            debitAmount = BigDecimal.ZERO;
        }
        if (creditAmount.compareTo(BigDecimal.ZERO) < 0 || debitAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Credit and debit amounts must be non-negative");
        }
    }

    public BigDecimal getNetBalance() {
        return creditAmount.subtract(debitAmount);
    }

    public boolean hasDebit() {
        return debitAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasCredit() {
        return creditAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}