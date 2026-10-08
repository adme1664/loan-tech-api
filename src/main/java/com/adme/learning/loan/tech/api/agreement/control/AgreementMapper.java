package com.adme.learning.loan.tech.api.agreement.control;

import com.adme.learning.loan.tech.api.agreement.boundary.AgreementBundleDTO;
import com.adme.learning.loan.tech.api.agreement.boundary.AgreementDTO;
import com.adme.learning.loan.tech.api.agreement.boundary.PartyDTO;
import com.adme.learning.loan.tech.api.agreement.entity.Agreement;
import com.adme.learning.loan.tech.api.agreement.entity.Party;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Maps between Agreement entities and DTOs.
 */
@Mapper(componentModel = "spring", uses = {InterestRateMapper.class, BalanceMapper.class, PartyMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AgreementMapper {

    @Autowired
    protected PartyMapper partyMapper;

    /**
     * Maps Agreement entity to AgreementDTO.
     *
     * @param agreement the Agreement entity
     * @return the AgreementDTO
     */
    public abstract AgreementDTO toDto(Agreement agreement);

    /**
     * Maps AgreementDTO to Agreement entity.
     *
     * @param dto the AgreementDTO
     * @return the Agreement entity
     */
    @Mapping(target = "agreementId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    @Mapping(target = "interestRates", ignore = true)
    @Mapping(target = "balances", ignore = true)
    @Mapping(target = "parties", ignore = true)
    public abstract Agreement toEntity(AgreementDTO dto);

    /**
     * Updates an existing Agreement entity from AgreementDTO.
     *
     * @param dto the AgreementDTO
     * @param agreement the existing Agreement entity to update
     */
    @Mapping(target = "agreementId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    @Mapping(target = "interestRates", ignore = true)
    @Mapping(target = "balances", ignore = true)
    @Mapping(target = "parties", ignore = true)
    public abstract void updateEntityFromDto(AgreementDTO dto, @MappingTarget Agreement agreement);

    /**
     * Maps Agreement entity to AgreementBundleDTO with nested relationships.
     *
     * @param agreement the Agreement entity
     * @return the AgreementBundleDTO
     */
    @Mapping(target = "agreement", source = ".")
    @Mapping(target = "interestRates", source = "interestRates")
    @Mapping(target = "balances", source = "balances")
    @Mapping(target = "party", source = "parties", qualifiedByName = "mapFirstParty")
    public abstract AgreementBundleDTO toBundleDto(Agreement agreement);

    /**
     * Helper method to map the first party from the list.
     *
     * @param parties the list of parties
     * @return the first PartyDTO or null if the list is empty
     */
    @Named("mapFirstParty")
    protected PartyDTO mapFirstParty(List<Party> parties) {
        if (parties == null || parties.isEmpty()) {
            return null;
        }
        return partyMapper.toDto(parties.getFirst());
    }

    /**
     * Maps a list of Agreement entities to a list of AgreementDTOs.
     *
     * @param agreements the list of Agreement entities
     * @return the list of AgreementDTOs
     */
    public abstract List<AgreementDTO> toDtoList(List<Agreement> agreements);

    /**
     * Maps a list of Agreement entities to a list of AgreementBundleDTOs.
     *
     * @param agreements the list of Agreement entities
     * @return the list of AgreementBundleDTOs
     */
    public abstract List<AgreementBundleDTO> toBundleDtoList(List<Agreement> agreements);
}
