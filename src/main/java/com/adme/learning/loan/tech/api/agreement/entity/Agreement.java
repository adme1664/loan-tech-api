package com.adme.learning.loan.tech.api.agreement.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a commercial loan agreement entity.
 * Maps to tbl_agreement in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_agreement", schema = "commercialloan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "agreement_id", nullable = false)
    private UUID agreementId;

    @Column(name = "agreement_no", nullable = false, unique = true, length = 20)
    private String agreementNo;

    @Column(name = "credit_facility_no", nullable = false, length = 20)
    private String creditFacilityNo;

    @Column(name = "agreement_type", nullable = false, length = 20)
    private String agreementType;

    @Column(name = "product_cd", nullable = false, length = 30)
    private String productCode;

    @Column(name = "status_cd_src", nullable = false, length = 20)
    private String statusCodeSource;

    @Column(name = "status_cd", nullable = false, length = 20)
    private String statusCode;

    @Column(name = "unbnk_letter_issuer_ind", nullable = false)
    private Boolean unbankLetterIssuerIndicator;

    @Column(name = "original_amt", nullable = false, precision = 18, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "effctv_dt", nullable = false)
    private Instant effectiveDate;

    @Column(name = "expiry_dt", nullable = false)
    private Instant expiryDate;

    @Column(name = "maturity_dt", nullable = false)
    private Instant maturityDate;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "source_system", nullable = false, length = 30)
    private String sourceSystem;

    @Column(name = "deleted_ind", nullable = false)
    @Builder.Default
    private Boolean deletedIndicator = false;

    @Column(name = "sys_ins_ts", nullable = false, updatable = false)
    private ZonedDateTime systemInsertTimestamp;

    @Column(name = "sys_upd_ts")
    private ZonedDateTime systemUpdateTimestamp;

    @Column(name = "sys_ins_process", nullable = false, length = 100)
    private String systemInsertProcess;

    @Column(name = "sys_upd_process", length = 100)
    private String systemUpdateProcess;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InterestRate> interestRates = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Balance> balances = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Party> parties = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        systemInsertTimestamp = ZonedDateTime.now();
        if (deletedIndicator == null) {
            deletedIndicator = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = ZonedDateTime.now();
    }

    public void addInterestRate(InterestRate interestRate) {
        interestRates.add(interestRate);
        interestRate.setAgreement(this);
    }

    public void removeInterestRate(InterestRate interestRate) {
        interestRates.remove(interestRate);
        interestRate.setAgreement(null);
    }

    public void addBalance(Balance balance) {
        balances.add(balance);
        balance.setAgreement(this);
    }

    public void removeBalance(Balance balance) {
        balances.remove(balance);
        balance.setAgreement(null);
    }

    public void addParty(Party party) {
        parties.add(party);
        party.setAgreement(this);
    }

    public void removeParty(Party party) {
        parties.remove(party);
        party.setAgreement(null);
    }

}