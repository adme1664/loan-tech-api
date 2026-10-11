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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents balance and accrued interest information for an agreement.
 * Maps to tbl_balance in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_balance", schema = "commercialloan")
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

    @Column(name = "balance_type", nullable = false, length = 30)
    private String balanceType;

    @Column(name = "credit_amnt", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "debit_amnt", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "balance_date", nullable = false)
    private Instant balanceDate;

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
        validateAmounts();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = Instant.now();
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