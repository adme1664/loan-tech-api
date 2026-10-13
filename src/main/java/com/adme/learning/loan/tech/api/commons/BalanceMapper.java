package com.adme.learning.loan.tech.api.commons;

import com.adme.learning.loan.tech.api.agreement.boundary.BalanceDTO;
import com.adme.learning.loan.tech.api.agreement.entity.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Maps between Balance entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BalanceMapper {

    /**
     * Maps Balance entity to BalanceDTO.
     *
     * @param balance the Balance entity
     * @return the BalanceDTO
     */
    @Mapping(target = "agreementId", source = "agreement.agreementId")
    @Mapping(target = "netBalance", expression = "java(balance.getNetBalance())")
    BalanceDTO toDto(Balance balance);

    /**
     * Maps BalanceDTO to Balance entity.
     *
     * @param dto the BalanceDTO
     * @return the Balance entity
     */
    @Mapping(target = "balanceId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    Balance toEntity(BalanceDTO dto);

    /**
     * Updates an existing Balance entity from BalanceDTO.
     *
     * @param dto the BalanceDTO
     * @param balance the existing Balance entity to update
     */
    @Mapping(target = "balanceId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    void updateEntityFromDto(BalanceDTO dto, @MappingTarget Balance balance);

    /**
     * Maps a list of Balance entities to a list of BalanceDTOs.
     *
     * @param balances the list of Balance entities
     * @return the list of BalanceDTOs
     */
    List<BalanceDTO> toDtoList(List<Balance> balances);

    /**
     * Maps a list of BalanceDTOs to a list of Balance entities.
     *
     * @param dtos the list of BalanceDTOs
     * @return the list of Balance entities
     */
    List<Balance> toEntityList(List<BalanceDTO> dtos);
}
