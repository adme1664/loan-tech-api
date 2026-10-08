package com.adme.learning.loan.tech.api.agreement.boundary;

import com.adme.learning.loan.tech.api.agreement.entity.InterestRateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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
    private InterestRateType interestRateType;
    private BigDecimal interestRate;
    private BigDecimal baseRate;
    private BigDecimal spreadRate;
    private LocalDate effectiveDate;
    private BigDecimal effectiveRate;
}
