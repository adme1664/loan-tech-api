package com.adme.learning.loan.tech.api.creditfacility.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a party associated with a credit facility.
 * Maps to tbl_cf_party in the commercialloan schema.
 */
@Entity
@Table(name = "tbl_cf_party", schema = "commercialloan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfParty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cf_party_id", nullable = false)
    private UUID cfPartyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false, unique = true)
    private Deal deal;

    @Column(name = "unbnk_id", nullable = false, length = 20)
    private String unbankId;

    @Column(name = "member_id_no", nullable = false, unique = true, length = 20)
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
}