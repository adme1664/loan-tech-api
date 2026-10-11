package com.adme.learning.loan.tech.api.agreement.boundary;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object for Balance responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceDTO {

    private UUID balanceId;
    private UUID agreementId;
    private String balanceType;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
    private BigDecimal netBalance;
    private String balanceDate;
}
