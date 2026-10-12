package com.adme.learning.loan.tech.api.agreement.control;

import com.adme.learning.loan.tech.api.agreement.boundary.PaymentScheduleDTO;
import com.adme.learning.loan.tech.api.agreement.entity.PaymentSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.util.List;

/**
 * Maps between PaymentSchedule entities and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentScheduleMapper {

    /**
     * Maps PaymentSchedule entity to PaymentScheduleDTO.
     *
     * @param paymentSchedule the PaymentSchedule entity
     * @return the PaymentScheduleDTO
     */
    @Mapping(target = "agreementId", source = "agreement.agreementId")
    @Mapping(target = "paymentDate", source = "paymentDate", qualifiedByName = "instantToString")
    @Mapping(target = "businessDate", source = "scheduleDate", qualifiedByName = "instantToString")
    @Mapping(target = "paymentAmnt", source = "paymentAmount")
    PaymentScheduleDTO toDto(PaymentSchedule paymentSchedule);

    /**
     * Maps PaymentScheduleDTO to PaymentSchedule entity.
     *
     * @param dto the PaymentScheduleDTO
     * @return the PaymentSchedule entity
     */
    @Mapping(target = "paymentScheduleId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "paymentDate", source = "paymentDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "scheduleDate", source = "businessDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "paymentAmount", source = "paymentAmnt")
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    PaymentSchedule toEntity(PaymentScheduleDTO dto);

    /**
     * Updates an existing PaymentSchedule entity from PaymentScheduleDTO.
     *
     * @param dto the PaymentScheduleDTO
     * @param paymentSchedule the existing PaymentSchedule entity to update
     */
    @Mapping(target = "paymentScheduleId", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "paymentDate", source = "paymentDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "scheduleDate", source = "businessDate", qualifiedByName = "stringToInstant")
    @Mapping(target = "paymentAmount", source = "paymentAmnt")
    @Mapping(target = "systemInsertTimestamp", ignore = true)
    @Mapping(target = "systemUpdateTimestamp", ignore = true)
    @Mapping(target = "systemInsertProcess", ignore = true)
    @Mapping(target = "systemUpdateProcess", ignore = true)
    void updateEntityFromDto(PaymentScheduleDTO dto, @MappingTarget PaymentSchedule paymentSchedule);

    /**
     * Maps a list of PaymentSchedule entities to a list of PaymentScheduleDTOs.
     *
     * @param paymentSchedules the list of PaymentSchedule entities
     * @return the list of PaymentScheduleDTOs
     */
    List<PaymentScheduleDTO> toDtoList(List<PaymentSchedule> paymentSchedules);

    /**
     * Maps a list of PaymentScheduleDTOs to a list of PaymentSchedule entities.
     *
     * @param dtos the list of PaymentScheduleDTOs
     * @return the list of PaymentSchedule entities
     */
    List<PaymentSchedule> toEntityList(List<PaymentScheduleDTO> dtos);

    /**
     * Converts Instant to String (ISO-8601 format).
     *
     * @param instant the Instant to convert
     * @return String representation of the Instant
     */
    @Named("instantToString")
    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    /**
     * Converts String (ISO-8601 format) to Instant.
     *
     * @param dateString the String to convert
     * @return Instant representation of the date
     */
    @Named("stringToInstant")
    default Instant stringToInstant(String dateString) {
        return dateString != null ? CommonMapper.convertInstantToString(dateString) : null;
    }
}