package com.adme.learning.loan.tech.api.creditfacility.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a deal (borrowing agreement).
 * Maps to tbl_deal in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_deal", schema = "commercialloan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deal_id", nullable = false)
    private UUID dealId;

    @Column(name = "borrowing_agmt_id", nullable = false, unique = true, length = 20)
    private String borrowingAgreementId;

    @Column(name = "borrowing_agmt_name", nullable = false, length = 255)
    private String borrowingAgreementName;

    @Column(name = "effctv_dt")
    private String effectiveDate;

    @Column(name = "classification_cd", length = 10)
    private String classificationCode;

    @Column(name = "next_review_dt")
    private String nextReviewDate;

    @Column(name = "sys_ins_ts", nullable = false, updatable = false)
    private Instant systemInsertTimestamp;

    @Column(name = "sys_upd_ts")
    private Instant systemUpdateTimestamp;

    @Column(name = "sys_ins_process", nullable = false, length = 100)
    private String systemInsertProcess;

    @Column(name = "delete_indicator")
    private boolean deleteIndicator;

    @Column(name = "sys_upd_process", length = 100)
    private String systemUpdateProcess;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CreditFacility> creditFacilities = new ArrayList<>();

    @OneToOne(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CfParty party;

    @PrePersist
    protected void onCreate() {
        systemInsertTimestamp = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = Instant.now();
    }

    public void addCreditFacility(CreditFacility creditFacility) {
        creditFacilities.add(creditFacility);
        creditFacility.setDeal(this);
    }

    public void removeCreditFacility(CreditFacility creditFacility) {
        creditFacilities.remove(creditFacility);
        creditFacility.setDeal(null);
    }

    public void setParty(CfParty party) {
        this.party = party;
        if (party != null) {
            party.setDeal(this);
        }
    }
}