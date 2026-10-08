package com.adme.learning.loan.tech.api.agreement.control;

import com.adme.learning.loan.tech.api.agreement.boundary.InterestRateDTO;
import com.adme.learning.loan.tech.api.agreement.entity.InterestRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Maps between InterestRate entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InterestRateMapper {

    /**
     * Maps InterestRate entity to InterestRateDTO.
     *
     * @param interestRate the InterestRate entity
     * @return the InterestRateDTO
     */
    @Mapping(target = "agreementId", source = "agreement.agreementId")
    @Mapping(target = "effectiveRate", expression = "java(interestRate.calculateEffectiveRate())")
    InterestRateDTO toDto(InterestRate interestRate);

    /**
     * Maps InterestRateDTO to InterestRate entity.
     *
     * @param dto the InterestRateDTO
     * @return the InterestRate entity
     */
    @Mapping(target = "interestRateId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    InterestRate toEntity(InterestRateDTO dto);

    /**
     * Updates an existing InterestRate entity from InterestRateDTO.
     *
     * @param dto the InterestRateDTO
     * @param interestRate the existing InterestRate entity to update
     */
    @Mapping(target = "interestRateId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    void updateEntityFromDto(InterestRateDTO dto, @MappingTarget InterestRate interestRate);

    /**
     * Maps a list of InterestRate entities to a list of InterestRateDTOs.
     *
     * @param interestRates the list of InterestRate entities
     * @return the list of InterestRateDTOs
     */
    List<InterestRateDTO> toDtoList(List<InterestRate> interestRates);

    /**
     * Maps a list of InterestRateDTOs to a list of InterestRate entities.
     *
     * @param dtos the list of InterestRateDTOs
     * @return the list of InterestRate entities
     */
    List<InterestRate> toEntityList(List<InterestRateDTO> dtos);
}
