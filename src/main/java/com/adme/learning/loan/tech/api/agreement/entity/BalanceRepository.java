package com.adme.learning.loan.tech.api.agreement.entity;

import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Balance entities.
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    List<Balance> findByAgreement(Agreement agreement);

    List<Balance> findByAgreementAgreementId(UUID agreementId);

    List<Balance> findByBalanceType(String balanceType);

    List<Balance> findByBalanceDate(Instant balanceDate);

    Optional<Balance> findByAgreementAgreementIdAndBalanceTypeAndBalanceDate(
            UUID agreementId,
            String balanceType,
            Instant balanceDate
    );

    @Query("SELECT b FROM Balance b WHERE b.agreement.agreementId = :agreementId " +
           "AND b.balanceType = :balanceType ORDER BY b.balanceDate DESC")
    List<Balance> findByAgreementAndTypeOrderByDateDesc(
            @Param("agreementId") UUID agreementId,
            @Param("balanceType") String balanceType
    );

    @Query("SELECT b FROM Balance b WHERE b.agreement.agreementId = :agreementId " +
           "AND b.balanceDate BETWEEN :startDate AND :endDate")
    List<Balance> findByAgreementAndDateRange(
            @Param("agreementId") UUID agreementId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<Balance> findFirstByAgreementAgreementIdAndBalanceTypeOrderByBalanceDateDesc(
            UUID agreementId,
            String balanceType
    );
}