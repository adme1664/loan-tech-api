package com.adme.learning.loan.tech.api.agreement.boundary;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for InterestRate responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestRateDTO {

    private UUID interestRateId;
    private UUID agreementId;
    private String interestRateType;
    private BigDecimal interestRate;
    private BigDecimal baseRate;
    private BigDecimal spreadRate;
    private String effectiveDate;
    private BigDecimal effectiveRate;
}
