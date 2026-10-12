package com.adme.learning.loan.tech.api.creditfacility.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Deal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealDTO {

    private String borrowingAgreementId;
    private String borrowingAgreementName;
    private String effectiveDate;
    private String classificationCode;
    private String nextReviewDate;
}