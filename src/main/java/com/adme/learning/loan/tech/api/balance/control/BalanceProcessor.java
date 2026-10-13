package com.adme.learning.loan.tech.api.balance.control;

import com.adme.learning.loan.tech.api.agreement.boundary.BalanceDTO;
import com.adme.learning.loan.tech.api.agreement.entity.Agreement;
import com.adme.learning.loan.tech.api.agreement.entity.AgreementRepository;
import com.adme.learning.loan.tech.api.agreement.entity.Balance;
import com.adme.learning.loan.tech.api.agreement.entity.BalanceRepository;
import com.adme.learning.loan.tech.api.balance.BalanceBundleDTO;
import com.adme.learning.loan.tech.api.commons.BalanceMapper;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BalanceProcessor {

  private final BalanceRepository balanceRepository;
  private final AgreementRepository agreementRepository;
  private final BalanceMapper balanceMapper;

  public BalanceProcessor(BalanceRepository balanceRepository,
      AgreementRepository agreementRepository, BalanceMapper balanceMapper) {
    this.balanceRepository = balanceRepository;
    this.agreementRepository = agreementRepository;
    this.balanceMapper = balanceMapper;
  }

  public void saveOrUpdateBalances(BalanceBundleDTO balanceBundleDTO) {
    Agreement agreement = agreementRepository.findByAgreementNo(balanceBundleDTO.getAgreementNo())
        .orElseThrow();

    if (!balanceBundleDTO.getBalances().isEmpty()) {
      log.info("Saving or updating balances for agreement {}", agreement.getAgreementNo());
      var balances = balanceBundleDTO.getBalances();
      for (BalanceDTO dto : balances) {
        Balance balance = balanceMapper.toEntity(dto);
        var existingBalance = balanceRepository.findByAgreementAndBalanceType(
            agreement,
            balance.getBalanceType()
        ).orElse(null);

        if (Objects.isNull(existingBalance)) {
          balance.setAgreement(agreement);
          balanceRepository.save(balance);
        } else {
          balanceMapper.updateEntityFromDto(dto, existingBalance);
          existingBalance.setSystemUpdateProcess("UNBNK_LOAN_IQ");
          existingBalance.setSystemUpdateTimestamp(Instant.now());
          balanceRepository.save(existingBalance);
        }
      }

    }
    agreement.setSystemUpdateTimestamp(ZonedDateTime.now());
    agreementRepository.save(agreement);

  }

}
