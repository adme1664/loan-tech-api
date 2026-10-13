package com.adme.learning.loan.tech.api.balance;


import com.adme.learning.loan.tech.api.agreement.boundary.BalanceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceBundleDTO {
  private String agreementNo;
  private List<BalanceDTO> balances;

}
