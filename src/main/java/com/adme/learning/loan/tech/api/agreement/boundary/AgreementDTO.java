package com.adme.learning.loan.tech.api.agreement.boundary;

import com.adme.learning.loan.tech.api.agreement.entity.AgreementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private AgreementStatus statusCode;
    private Boolean unbankLetterIssuerIndicator;
    private BigDecimal originalAmount;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private LocalDate maturityDate;
    private String currency;
    private String sourceSystem;
    private Boolean deletedIndicator;
}
