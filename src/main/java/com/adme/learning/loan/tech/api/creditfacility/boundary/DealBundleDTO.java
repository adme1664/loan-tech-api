package com.adme.learning.loan.tech.api.creditfacility.boundary;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for CreditFacility bundle containing all related entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealBundleDTO {

    private String dealNo;
    private String operation;
    private DealDTO deal;
    private List<CreditFacilityDTO> creditFacilities;
    private List<CfPartyDTO> parties;
}