package com.adme.learning.loan.tech.api.agreement.control;

import com.adme.learning.loan.tech.api.agreement.boundary.PartyDTO;
import com.adme.learning.loan.tech.api.agreement.entity.Party;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Maps between Party entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PartyMapper {

    /**
     * Maps Party entity to PartyDTO.
     *
     * @param party the Party entity
     * @return the PartyDTO
     */
    @Mapping(target = "agreementId", source = "agreement.agreementId")
    PartyDTO toDto(Party party);

    /**
     * Maps PartyDTO to Party entity.
     *
     * @param dto the PartyDTO
     * @return the Party entity
     */
    @Mapping(target = "partyId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    Party toEntity(PartyDTO dto);

    /**
     * Updates an existing Party entity from PartyDTO.
     *
     * @param dto the PartyDTO
     * @param party the existing Party entity to update
     */
    @Mapping(target = "partyId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    void updateEntityFromDto(PartyDTO dto, @MappingTarget Party party);

    /**
     * Maps a list of Party entities to a list of PartyDTOs.
     *
     * @param parties the list of Party entities
     * @return the list of PartyDTOs
     */
    List<PartyDTO> toDtoList(List<Party> parties);

    /**
     * Maps a list of PartyDTOs to a list of Party entities.
     *
     * @param dtos the list of PartyDTOs
     * @return the list of Party entities
     */
    List<Party> toEntityList(List<PartyDTO> dtos);
}
