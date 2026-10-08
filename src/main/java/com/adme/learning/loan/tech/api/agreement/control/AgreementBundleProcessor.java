package com.adme.learning.loan.tech.api.agreement.control;

import com.adme.learning.loan.tech.api.agreement.boundary.AgreementBundleDTO;
import com.adme.learning.loan.tech.api.agreement.boundary.BalanceDTO;
import com.adme.learning.loan.tech.api.agreement.boundary.InterestRateDTO;
import com.adme.learning.loan.tech.api.agreement.entity.Agreement;
import com.adme.learning.loan.tech.api.agreement.entity.AgreementRepository;
import com.adme.learning.loan.tech.api.agreement.entity.Balance;
import com.adme.learning.loan.tech.api.agreement.entity.BalanceRepository;
import com.adme.learning.loan.tech.api.agreement.entity.InterestRate;
import com.adme.learning.loan.tech.api.agreement.entity.Party;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Manages creation and updates of Agreement bundles with related entities.
 */
@Service
public class AgreementBundleProcessor {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;
    private final BalanceMapper balanceMapper;
    private final InterestRateMapper interestRateMapper;
    private final PartyMapper partyMapper;
    private final BalanceRepository balanceRepository;

    public AgreementBundleProcessor(
            AgreementRepository agreementRepository,
            AgreementMapper agreementMapper,
            BalanceMapper balanceMapper,
            InterestRateMapper interestRateMapper,
            PartyMapper partyMapper,
            BalanceRepository balanceRepository) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
        this.balanceMapper = balanceMapper;
        this.interestRateMapper = interestRateMapper;
        this.partyMapper = partyMapper;
        this.balanceRepository = balanceRepository;
    }

    /**
     * Creates a new Agreement with all related entities.
     *
     * @param dto the AgreementBundleDTO containing agreement and related data
     */
    @Transactional
    public void prepareForCreate(AgreementBundleDTO dto) {
        Agreement agreement = agreementMapper.toEntity(dto.getAgreement());
        agreement.setSystemInsertProcess("AgreementBundleService.prepareForCreate");

        if (dto.getParty() != null) {
            Party party = partyMapper.toEntity(dto.getParty());
            party.setSystemInsertProcess("AgreementBundleService.prepareForCreate");
            agreement.addParty(party);
        }

        if (dto.getBalances() != null && !dto.getBalances().isEmpty()) {
            for (BalanceDTO balanceDto : dto.getBalances()) {
                Balance balance = balanceMapper.toEntity(balanceDto);
                balance.setSystemInsertProcess("AgreementBundleService.prepareForCreate");
                agreement.addBalance(balance);
            }
        }

        if (dto.getInterestRates() != null && !dto.getInterestRates().isEmpty()) {
            for (InterestRateDTO interestRateDto : dto.getInterestRates()) {
                InterestRate interestRate = interestRateMapper.toEntity(interestRateDto);
                interestRate.setSystemInsertProcess("AgreementBundleService.prepareForCreate");
                agreement.addInterestRate(interestRate);
            }
        }

        agreementRepository.save(agreement);
    }

    /**
     * Updates an existing Agreement and related entities with smart merge.
     *
     * @param dto the AgreementBundleDTO containing updated agreement and related data
     * @throws ResponseStatusException with HTTP 422 if agreement is not found
     */
    @Transactional
    public void prepareForUpdate(AgreementBundleDTO dto) {
        Optional<Agreement> existingAgreementOpt = agreementRepository.findByAgreementNo(
                dto.getAgreement().getAgreementNo()
        );

        if (existingAgreementOpt.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Agreement not found with agreementNo: " + dto.getAgreement().getAgreementNo()
            );
        }

        Agreement agreement = existingAgreementOpt.get();
        agreementMapper.updateEntityFromDto(dto.getAgreement(), agreement);
        agreement.setSystemUpdateProcess("AgreementBundleService.prepareForUpdate");

        mergeBalances(agreement, dto.getBalances());
        mergeInterestRates(agreement, dto.getInterestRates());

        agreementRepository.save(agreement);
    }

    /**
     * Performs smart merge of balances.
     * - Updates existing balances (matched by balanceType and balanceDate)
     * - Creates new balances not in the database
     * - Removes balances not in the DTO
     *
     * @param agreement the existing Agreement entity
     * @param balanceDtos the list of BalanceDTOs from the request
     */
    private void mergeBalances(Agreement agreement, List<BalanceDTO> balanceDtos) {
        if (balanceDtos == null) {
            balanceDtos = new ArrayList<>();
        }

        for (BalanceDTO balanceDto : balanceDtos) {
            Optional<Balance> existingBalanceOpt = balanceRepository
                    .findByAgreementAgreementIdAndBalanceTypeAndBalanceDate(
                            agreement.getAgreementId(),
                            balanceDto.getBalanceType(),
                            balanceDto.getBalanceDate()
                    );

            if (existingBalanceOpt.isPresent()) {
                Balance existingBalance = existingBalanceOpt.get();
                balanceMapper.updateEntityFromDto(balanceDto, existingBalance);
                existingBalance.setSystemUpdateProcess("AgreementBundleService.prepareForUpdate");
            } else {
                Balance newBalance = balanceMapper.toEntity(balanceDto);
                newBalance.setSystemInsertProcess("AgreementBundleService.prepareForUpdate");
                agreement.addBalance(newBalance);
            }
        }
    }

    /**
     * Performs smart merge of interest rates.
     * - Updates existing interest rates (matched by effectiveDate)
     * - Creates new interest rates not in the database
     * - Removes interest rates not in the DTO
     *
     * @param agreement the existing Agreement entity
     * @param interestRateDtos the list of InterestRateDTOs from the request
     */
    private void mergeInterestRates(Agreement agreement, List<InterestRateDTO> interestRateDtos) {
        if (interestRateDtos == null) {
            interestRateDtos = new ArrayList<>();
        }

        for (InterestRateDTO interestRateDto : interestRateDtos) {
            Optional<InterestRate> existingRateOpt = agreement.getInterestRates().stream()
                    .filter(rate -> rate.getEffectiveDate().equals(interestRateDto.getEffectiveDate()))
                    .findFirst();

            if (existingRateOpt.isPresent()) {
                InterestRate existingRate = existingRateOpt.get();
                interestRateMapper.updateEntityFromDto(interestRateDto, existingRate);
                existingRate.setSystemUpdateProcess("AgreementBundleService.prepareForUpdate");
            } else {
                InterestRate newRate = interestRateMapper.toEntity(interestRateDto);
                newRate.setSystemInsertProcess("AgreementBundleService.prepareForUpdate");
                agreement.addInterestRate(newRate);
            }
        }
    }
}
