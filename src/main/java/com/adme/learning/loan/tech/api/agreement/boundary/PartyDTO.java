package com.adme.learning.loan.tech.api.agreement.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data transfer object for Party responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyDTO {

    private UUID partyId;
    private UUID agreementId;
    private String unbankId;
    private String memberIdNo;
}
