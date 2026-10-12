package com.adme.learning.loan.tech.api.creditfacility.control;

import com.adme.learning.loan.tech.api.creditfacility.boundary.AgreementRelationDTO;
import com.adme.learning.loan.tech.api.creditfacility.entity.AgreementRelation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Maps between AgreementRelation entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgreementRelationMapper {

    /**
     * Maps AgreementRelation entity to AgreementRelationDTO.
     *
     * @param relation the AgreementRelation entity
     * @return the AgreementRelationDTO
     */
    AgreementRelationDTO toDto(AgreementRelation relation);

    /**
     * Maps AgreementRelationDTO to AgreementRelation entity.
     *
     * @param dto the AgreementRelationDTO
     * @return the AgreementRelation entity
     */
    @Mapping(target = "creditFacilityId", ignore = true)
    @Mapping(target = "creditFacility", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    AgreementRelation toEntity(AgreementRelationDTO dto);

    /**
     * Maps a list of AgreementRelation entities to a list of AgreementRelationDTOs.
     *
     * @param relations the list of AgreementRelation entities
     * @return the list of AgreementRelationDTOs
     */
    List<AgreementRelationDTO> toDtoList(List<AgreementRelation> relations);

    /**
     * Maps a list of AgreementRelationDTOs to a list of AgreementRelation entities.
     *
     * @param dtos the list of AgreementRelationDTOs
     * @return the list of AgreementRelation entities
     */
    List<AgreementRelation> toEntityList(List<AgreementRelationDTO> dtos);
}