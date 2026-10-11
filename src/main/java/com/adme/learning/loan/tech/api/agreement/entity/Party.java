package com.adme.learning.loan.tech.api.agreement.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a party associated with an agreement.
 * Maps to tbl_party in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_party", schema = "commercialloan",
        uniqueConstraints = @UniqueConstraint(columnNames = {"agreement_id", "member_id_no"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "party_id", nullable = false)
    private UUID partyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @Column(name = "unbnk_id", length = 64)
    private String unbankId;

    @Column(name = "member_id_no", nullable = false, length = 20)
    private String memberIdNo;

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

    public boolean hasUnbankId() {
        return unbankId != null && !unbankId.trim().isEmpty();
    }
}