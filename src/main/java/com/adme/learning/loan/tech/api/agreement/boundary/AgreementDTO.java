package com.adme.learning.loan.tech.api.agreement.boundary;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Agreement responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementDTO {

    private String agreementNo;
    private String creditFacilityNo;
    private String agreementType;
    private String productCode;
    private String statusCodeSource;
    private String statusCode;
    private Boolean unbankLetterIssuerIndicator;
    private BigDecimal originalAmount;
    private String effectiveDate;
    private String expiryDate;
    private String maturityDate;
    private String currency;
    private String sourceSystem;
    private Boolean deletedIndicator;
}
