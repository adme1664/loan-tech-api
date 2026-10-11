package com.adme.learning.loan.tech.api.agreement.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for InterestRate entities.
 */
@Repository
public interface InterestRateRepository extends JpaRepository<InterestRate, UUID> {

    List<InterestRate> findByAgreement(Agreement agreement);

    List<InterestRate> findByAgreementAgreementId(UUID agreementId);

    List<InterestRate> findByInterestRateType(String type);

    @Query("SELECT ir FROM InterestRate ir WHERE ir.agreement.agreementId = :agreementId " +
           "AND ir.effectiveDate <= :date ORDER BY ir.effectiveDate DESC")
    List<InterestRate> findEffectiveRatesAsOfDate(
            @Param("agreementId") UUID agreementId,
            @Param("date") LocalDate date
    );

    @Query("SELECT ir FROM InterestRate ir WHERE ir.agreement.agreementId = :agreementId " +
           "ORDER BY ir.effectiveDate DESC")
    List<InterestRate> findByAgreementIdOrderByEffectiveDateDesc(@Param("agreementId") UUID agreementId);

    Optional<InterestRate> findFirstByAgreementAgreementIdOrderByEffectiveDateDesc(UUID agreementId);
}