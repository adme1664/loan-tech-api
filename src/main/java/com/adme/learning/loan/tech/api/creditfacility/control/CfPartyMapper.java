package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.CfPartyDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.CfParty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Maps between CfParty entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CfPartyMapper {

    /**
     * Maps CfParty entity to CfPartyDTO.
     *
     * @param party the CfParty entity
     * @return the CfPartyDTO
     */
    CfPartyDTO toDto(CfParty party);

    /**
     * Maps CfPartyDTO to CfParty entity.
     *
     * @param dto the CfPartyDTO
     * @return the CfParty entity
     */
    @Mapping(target = "cfPartyId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    CfParty toEntity(CfPartyDTO dto);

    /**
     * Updates an existing CfParty entity from CfPartyDTO.
     *
     * @param dto the CfPartyDTO
     * @param party the existing CfParty entity to update
     */
    @Mapping(target = "cfPartyId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    void updateEntityFromDto(CfPartyDTO dto, @MappingTarget CfParty party);

    /**
     * Maps a list of CfParty entities to a list of CfPartyDTOs.
     *
     * @param parties the list of CfParty entities
     * @return the list of CfPartyDTOs
     */
    List<CfPartyDTO> toDtoList(List<CfParty> parties);

    /**
     * Maps a list of CfPartyDTOs to a list of CfParty entities.
     *
     * @param dtos the list of CfPartyDTOs
     * @return the list of CfParty entities
     */
    List<CfParty> toEntityList(List<CfPartyDTO> dtos);
}