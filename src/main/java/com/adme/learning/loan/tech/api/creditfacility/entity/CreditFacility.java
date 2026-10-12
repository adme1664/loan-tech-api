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
 * Represents a credit facility.
 * Maps to tbl_credit_facility in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_credit_facility", schema = "commercialloan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_facility_id", nullable = false)
    private UUID creditFacilityId;

    @Column(name = "credit_facility_no", nullable = false, unique = true, length = 20)
    private String creditFacilityNo;

    @Column(name = "borrowing_agmt_id", nullable = false, length = 20)
    private String borrowingAgreementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowing_agmt_id", referencedColumnName = "borrowing_agmt_id", insertable = false, updatable = false)
    private Deal deal;

    @Column(name = "credit_facility_name", nullable = false, length = 255)
    private String creditFacilityName;

    @Column(name = "status_cd", length = 20)
    private String statusCode;

    @Column(name = "status_cd_src", length = 20)
    private String statusCodeSource;

    @Column(name = "product_cd", length = 20)
    private String productCode;

    @Column(name = "lender_role_cd", length = 20)
    private String lenderRoleCode;

    @Column(name = "effctv_dt")
    private String effectiveDate;

    @Column(name = "expiry_dt")
    private String expiryDate;

    @Column(name = "maturity_dt")
    private String maturityDate;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "sys_ins_ts", nullable = false, updatable = false)
    private Instant systemInsertTimestamp;

    @Column(name = "sys_upd_ts")
    private Instant systemUpdateTimestamp;

    @Column(name = "sys_ins_process", nullable = false, length = 100)
    private String systemInsertProcess;

    @Column(name = "sys_upd_process", length = 100)
    private String systemUpdateProcess;

    @OneToMany(mappedBy = "creditFacility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AgreementRelation> agreementRelations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        systemInsertTimestamp = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        systemUpdateTimestamp = Instant.now();
    }

    public void addParty(CfParty party) {
        parties.add(party);
        party.setCreditFacility(this);
    }

    public void removeParty(CfParty party) {
        parties.remove(party);
        party.setCreditFacility(null);
    }

    public void addAgreementRelation(AgreementRelation agreementRelation) {
        agreementRelations.add(agreementRelation);
        agreementRelation.setCreditFacility(this);
    }

    public void removeAgreementRelation(AgreementRelation agreementRelation) {
        agreementRelations.remove(agreementRelation);
        agreementRelation.setCreditFacility(null);
    }
}