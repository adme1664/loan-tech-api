package com.adme.learning.loan.tech.api.creditfacility.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a relation between credit facility and agreement.
 * Maps to tblr_agreement_relation in the commercialloan schema.
 */
@Entity
@Table(name = "tblr_agreement_relation", schema = "commercialloan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AgreementRelation.AgreementRelationId.class)
public class AgreementRelation {

    @Id
    @Column(name = "credit_facility_id", nullable = false)
    private UUID creditFacilityId;

    @Id
    @Column(name = "agmt_id", nullable = false, length = 20)
    private String agreementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_facility_id", insertable = false, updatable = false)
    private CreditFacility creditFacility;

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

    /**
     * Composite primary key class for AgreementRelation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreementRelationId implements Serializable {
        private UUID creditFacilityId;
        private String agreementId;
    }
}