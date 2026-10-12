package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.DealDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Maps between Deal entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DealMapper {

    /**
     * Maps Deal entity to DealDTO.
     *
     * @param deal the Deal entity
     * @return the DealDTO
     */
    DealDTO toDto(Deal deal);

    /**
     * Maps DealDTO to Deal entity.
     *
     * @param dto the DealDTO
     * @return the Deal entity
     */
    @Mapping(target = "dealId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    @Mapping(target = "creditFacilities", ignore = true)
    Deal toEntity(DealDTO dto);

    /**
     * Updates an existing Deal entity from DealDTO.
     *
     * @param dto the DealDTO
     * @param deal the existing Deal entity to update
     */
    @Mapping(target = "dealId", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    @Mapping(target = "creditFacilities", ignore = true)
    void updateEntityFromDto(DealDTO dto, @MappingTarget Deal deal);
}