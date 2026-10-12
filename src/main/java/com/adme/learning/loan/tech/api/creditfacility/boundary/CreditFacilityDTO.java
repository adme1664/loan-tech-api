package com.adme.learning.loan.tech.api.creditfacility.boundary;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for CreditFacility.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditFacilityDTO {

    private String creditFacilityNo;
    private String borrowingAgreementId;
    private String creditFacilityName;
    private String statusCode;
    private String statusCodeSource;
    private String productCode;
    private String lenderRoleCode;
    private String effectiveDate;
    private String expiryDate;
    private String maturityDate;
    private String currency;
    List<AgreementRelationDTO> agreementRelations;
}