package com.adme.learning.loan.tech.api.creditfacility.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for AgreementRelation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementRelationDTO {

    private String agreementId;
}