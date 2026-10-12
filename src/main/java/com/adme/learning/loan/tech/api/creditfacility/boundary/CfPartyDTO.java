package com.adme.learning.loan.tech.api.creditfacility.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for CfParty.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfPartyDTO {

    private String unbankId;
    private String memberIdNo;
}