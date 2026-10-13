package com.adme.learning.loan.tech.api.balance.boundary;

import com.adme.learning.loan.tech.api.balance.BalanceBundleDTO;
import com.adme.learning.loan.tech.api.balance.control.BalanceProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceBundleResource {
  private final BalanceProcessor balanceProcessor;

  public BalanceBundleResource(BalanceProcessor balanceProcessor) {
    this.balanceProcessor = balanceProcessor;
  }

  @PostMapping("/bundle")
  public ResponseEntity<Void> saveOrUpdate(@RequestBody BalanceBundleDTO dto) {
    if (dto == null) {
      return ResponseEntity.unprocessableContent().build();
    }

    balanceProcessor.saveOrUpdateBalances(dto);

    return ResponseEntity.ok().build();
  }

}
