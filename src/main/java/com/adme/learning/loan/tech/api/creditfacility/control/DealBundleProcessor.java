package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.AgreementRelationDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.CfPartyDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.CreditFacilityDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.DealBundleDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.AgreementRelation;
import com.adme.learning.loan.tech.api.creditfacility.entity.CfParty;
import com.adme.learning.loan.tech.api.creditfacility.entity.CreditFacility;
import com.adme.learning.loan.tech.api.creditfacility.entity.CreditFacilityRepository;
import com.adme.learning.loan.tech.api.creditfacility.entity.Deal;
import com.adme.learning.loan.tech.api.creditfacility.entity.DealRepository;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Manages creation and updates of CreditFacility bundles with related entities.
 */
@Service
public class DealBundleProcessor {

  private static final Logger logger = LoggerFactory.getLogger(DealBundleProcessor.class);
  private static final String APP_NAME = "UNBNK_LOAN_IQ";

  private final CreditFacilityRepository creditFacilityRepository;
  private final DealRepository dealRepository;
  private final CreditFacilityMapper creditFacilityMapper;
  private final DealMapper dealMapper;
  private final CfPartyMapper cfPartyMapper;
  private final AgreementRelationMapper agreementRelationMapper;

  public DealBundleProcessor(
      CreditFacilityRepository creditFacilityRepository,
      DealRepository dealRepository,
      CreditFacilityMapper creditFacilityMapper,
      DealMapper dealMapper,
      CfPartyMapper cfPartyMapper,
      AgreementRelationMapper agreementRelationMapper) {
    this.creditFacilityRepository = creditFacilityRepository;
    this.dealRepository = dealRepository;
    this.creditFacilityMapper = creditFacilityMapper;
    this.dealMapper = dealMapper;
    this.cfPartyMapper = cfPartyMapper;
    this.agreementRelationMapper = agreementRelationMapper;
  }

  /**
   * Creates a new CreditFacility with all related entities.
   *
   * @param dto the CreditFacilityBundleDTO containing credit facility and related data
   */
  @NewSpan("deal.create")
  @Transactional
  public void prepareForCreate(@SpanTag("dealNo") DealBundleDTO dto) {
    // Create or find Deal
    Deal deal;
    Optional<Deal> existingDeal = dealRepository.findByBorrowingAgreementId(
        dto.getDeal().getBorrowingAgreementId()
    );

    if (existingDeal.isPresent()) {
      deal = existingDeal.get();
      dealMapper.updateEntityFromDto(dto.getDeal(), deal);
      deal.setSystemUpdateProcess(APP_NAME);
    } else {
      deal = dealMapper.toEntity(dto.getDeal());
      deal.setSystemInsertProcess(APP_NAME);

      // Create CreditFacility
      List<CreditFacility> creditFacilities = getCreditFacilities(
          dto, deal);

      //set creditFacilities and agreementRelations
      deal.setCreditFacilities(creditFacilities);

      //set Party
      CfParty cfParty = cfPartyMapper.toEntity(dto.getParties().getFirst());
      cfParty.setSystemInsertProcess(APP_NAME);
      cfParty.setDeal(deal);
      deal.setParty(cfParty);

      dealRepository.save(deal);
      logger.info("Deal = {} created successfully", deal.getBorrowingAgreementId());
    }
  }

  private @NonNull List<CreditFacility> getCreditFacilities(DealBundleDTO dto, Deal deal) {
    List<CreditFacilityDTO> creditFacilityDTOS = dto.getCreditFacilities();

    List<CreditFacility> creditFacilities = new ArrayList<>();

    creditFacilityDTOS.forEach(creditFacilityDTO -> {
      CreditFacility creditFacility = creditFacilityMapper.toEntity(creditFacilityDTO);
      creditFacility.setBorrowingAgreementId(deal.getBorrowingAgreementId());
      creditFacility.setSystemInsertProcess(APP_NAME);
      creditFacility.setDeal(deal);

      //set agreements
      creditFacilityDTO.getAgreementRelations().forEach(agreementRelationDTO -> {
        AgreementRelation agreementRelation = agreementRelationMapper.toEntity(
            agreementRelationDTO);
        agreementRelation.setSystemInsertProcess(APP_NAME);
        creditFacility.addAgreementRelation(agreementRelation);

        creditFacilities.add(creditFacility);
      });
    });
    return creditFacilities;
  }

  /**
   * Updates an existing Deal and related entities with smart merge.
   *
   * @param dto the DealBundleDTO containing updated deal and related data
   * @throws ResponseStatusException with HTTP 422 if deal is not found
   */
  @NewSpan("deal.update")
  @Transactional
  public void prepareForUpdate(@SpanTag("dealNo") DealBundleDTO dto) {
    Optional<Deal> existingDealOpt = dealRepository.findByBorrowingAgreementId(
        dto.getDeal().getBorrowingAgreementId()
    );

    if (existingDealOpt.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_CONTENT,
          "Deal not found with borrowingAgreementId: " + dto.getDeal().getBorrowingAgreementId()
      );
    }

    Deal deal = existingDealOpt.get();
    dealMapper.updateEntityFromDto(dto.getDeal(), deal);
    deal.setSystemUpdateProcess(APP_NAME);

    mergeCreditFacilities(deal, dto.getCreditFacilities());
    mergeParty(deal, dto.getParties());

    dealRepository.save(deal);
    logger.info("Deal = {} updated successfully", deal.getBorrowingAgreementId());
  }

  /**
   * Performs smart merge of credit facilities. - Updates existing credit facilities (matched by
   * creditFacilityNo) - Creates new credit facilities not in the database
   *
   * @param deal               the existing Deal entity
   * @param creditFacilityDtos the list of CreditFacilityDTOs from the request
   */
  @NewSpan("deal.merge-credit-facilities")
  private void mergeCreditFacilities(Deal deal, List<CreditFacilityDTO> creditFacilityDtos) {
    if (creditFacilityDtos == null) {
      creditFacilityDtos = new ArrayList<>();
    }

    for (CreditFacilityDTO creditFacilityDto : creditFacilityDtos) {
      Optional<CreditFacility> existingCreditFacilityOpt = creditFacilityRepository
          .findByCreditFacilityNo(creditFacilityDto.getCreditFacilityNo());

      if (existingCreditFacilityOpt.isPresent()) {
        CreditFacility existingCreditFacility = existingCreditFacilityOpt.get();
        creditFacilityMapper.updateEntityFromDto(creditFacilityDto, existingCreditFacility);
        existingCreditFacility.setSystemUpdateProcess(APP_NAME);

        // Merge agreement relations
        mergeAgreementRelations(existingCreditFacility, creditFacilityDto.getAgreementRelations());
      } else {
        CreditFacility newCreditFacility = creditFacilityMapper.toEntity(creditFacilityDto);
        newCreditFacility.setSystemInsertProcess(APP_NAME);
        newCreditFacility.setDeal(deal);
        deal.addCreditFacility(newCreditFacility);

        // Add agreement relations
        if (creditFacilityDto.getAgreementRelations() != null) {
          for (AgreementRelationDTO agreementRelationDto : creditFacilityDto.getAgreementRelations()) {
            AgreementRelation agreementRelation = agreementRelationMapper.toEntity(
                agreementRelationDto);
            agreementRelation.setSystemInsertProcess(APP_NAME);
            newCreditFacility.addAgreementRelation(agreementRelation);
          }
        }
      }
    }
  }

  /**
   * Performs smart merge of agreement relations. - Clears existing relations and replaces with new
   * ones
   *
   * @param creditFacility        the existing CreditFacility entity
   * @param agreementRelationDtos the list of AgreementRelationDTOs from the request
   */
  @NewSpan("deal.merge-agreement-relations")
  private void mergeAgreementRelations(CreditFacility creditFacility,
      List<AgreementRelationDTO> agreementRelationDtos) {
    if (agreementRelationDtos == null) {
      agreementRelationDtos = new ArrayList<>();
    }

    creditFacility.getAgreementRelations().clear();
    for (AgreementRelationDTO agreementRelationDto : agreementRelationDtos) {
      AgreementRelation agreementRelation = agreementRelationMapper.toEntity(agreementRelationDto);
      agreementRelation.setSystemInsertProcess(APP_NAME);
      creditFacility.addAgreementRelation(agreementRelation);
    }
  }

  /**
   * Performs smart merge of party. - Updates existing party or creates new one
   *
   * @param deal      the existing Deal entity
   * @param partyDtos the list of CfPartyDTOs from the request
   */
  @NewSpan("deal.merge-party")
  private void mergeParty(Deal deal, List<CfPartyDTO> partyDtos) {
    if (partyDtos == null || partyDtos.isEmpty()) {
      return;
    }

    CfPartyDTO partyDto = partyDtos.getFirst();
    CfParty existingParty = deal.getParty();

    if (existingParty != null) {
      cfPartyMapper.updateEntityFromDto(partyDto, existingParty);
      existingParty.setSystemUpdateProcess(APP_NAME);
    } else {
      CfParty newParty = cfPartyMapper.toEntity(partyDto);
      newParty.setDeal(deal);
      newParty.setSystemInsertProcess(APP_NAME);
      deal.setParty(newParty);
    }
  }

  @NewSpan("deal.purge")
  public boolean purgeDeal(@SpanTag("dealNo") String dealNo) {
    var dealToPurge = dealRepository.findByBorrowingAgreementId(dealNo);
    if (dealToPurge.isEmpty()) {
      return false;
    } else {
      Deal deal = dealToPurge.get();
      deal.setDeleteIndicator(true);
      deal.setSystemUpdateProcess("UNBNK_LOAN_IQ");
      deal.setSystemInsertTimestamp(Instant.now());
      dealRepository.save(deal);
      return true;
    }
  }

}