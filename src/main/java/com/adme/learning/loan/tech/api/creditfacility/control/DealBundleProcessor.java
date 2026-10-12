package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.AgreementRelationDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.CfPartyDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.CreditFacilityDTO;
import com.adme.learning.loan.tech.api.creditfacility.boundary.DealBundleDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.AgreementRelation;
import com.adme.learning.loan.tech.api.creditfacility.entity.AgreementRelationRepository;
import com.adme.learning.loan.tech.api.creditfacility.entity.CfParty;
import com.adme.learning.loan.tech.api.creditfacility.entity.CfPartyRepository;
import com.adme.learning.loan.tech.api.creditfacility.entity.CreditFacility;
import com.adme.learning.loan.tech.api.creditfacility.entity.CreditFacilityRepository;
import com.adme.learning.loan.tech.api.creditfacility.entity.Deal;
import com.adme.learning.loan.tech.api.creditfacility.entity.DealRepository;
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
    private final CfPartyRepository cfPartyRepository;
    private final AgreementRelationRepository agreementRelationRepository;
    private final CreditFacilityMapper creditFacilityMapper;
    private final DealMapper dealMapper;
    private final CfPartyMapper cfPartyMapper;
    private final AgreementRelationMapper agreementRelationMapper;

    public DealBundleProcessor(
            CreditFacilityRepository creditFacilityRepository,
            DealRepository dealRepository,
            CfPartyRepository cfPartyRepository,
            AgreementRelationRepository agreementRelationRepository,
            CreditFacilityMapper creditFacilityMapper,
            DealMapper dealMapper,
            CfPartyMapper cfPartyMapper,
            AgreementRelationMapper agreementRelationMapper) {
        this.creditFacilityRepository = creditFacilityRepository;
        this.dealRepository = dealRepository;
        this.cfPartyRepository = cfPartyRepository;
        this.agreementRelationRepository = agreementRelationRepository;
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
    @Transactional
    public void prepareForCreate(DealBundleDTO dto) {
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
        }
    }

    private @NonNull List<CreditFacility> getCreditFacilities(DealBundleDTO dto, Deal deal) {
        List<CreditFacilityDTO> creditFacilityDTOS = dto.getCreditFacilities();

        List<CreditFacility> creditFacilities = new ArrayList<>();

        creditFacilityDTOS.forEach(creditFacilityDTO -> {
            CreditFacility creditFacility = creditFacilityMapper.toEntity(creditFacilityDTO);
            creditFacility.setSystemInsertProcess(APP_NAME);
            creditFacility.setDeal(deal);
            creditFacilities.add(creditFacility);

            //set agreements
            creditFacilityDTO.getAgreementRelations().forEach(agreementRelationDTO -> {
                AgreementRelation agreementRelation = agreementRelationMapper.toEntity(agreementRelationDTO);
                agreementRelation.setCreditFacility(creditFacility);
                agreementRelation.setSystemInsertProcess(APP_NAME);
                creditFacility.addAgreementRelation(agreementRelation);
            });
        });
        return creditFacilities;
    }


}