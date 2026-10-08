package com.adme.learning.loan.tech.api.agreement.boundary;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementBundleDTO {
  private AgreementDTO agreement;
  private List<InterestRateDTO> interestRates;
  private List<BalanceDTO> balances;
  private PartyDTO party;

}
