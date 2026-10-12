package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.CreditFacilityDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.CreditFacility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Maps between CreditFacility entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditFacilityMapper {

    /**
     * Maps CreditFacility entity to CreditFacilityDTO.
     *
     * @param creditFacility the CreditFacility entity
     * @return the CreditFacilityDTO
     */
    CreditFacilityDTO toDto(CreditFacility creditFacility);

    /**
     * Maps CreditFacilityDTO to CreditFacility entity.
     *
     * @param dto the CreditFacilityDTO
     * @return the CreditFacility entity
     */
    @Mapping(target = "creditFacilityId", ignore = true)
    @Mapping(target = "deal", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)

    @Mapping(target = "agreementRelations", ignore = true)
    CreditFacility toEntity(CreditFacilityDTO dto);

    /**
     * Updates an existing CreditFacility entity from CreditFacilityDTO.
     *
     * @param dto the CreditFacilityDTO
     * @param creditFacility the existing CreditFacility entity to update
     */
    @Mapping(target = "creditFacilityId", ignore = true)
    @Mapping(target = "deal", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    @Mapping(target = "agreementRelations", ignore = true)
    void updateEntityFromDto(CreditFacilityDTO dto, @MappingTarget CreditFacility creditFacility);
}