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
 * Repository for Agreement entities.
 */
@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    Optional<Agreement> findByAgreementNo(String agreementNo);

    List<Agreement> findByCreditFacilityNo(String creditFacilityNo);

    List<Agreement> findByStatusCode(String statusCode);

    List<Agreement> findByStatusCodeAndDeletedIndicator(String statusCode, Boolean deletedIndicator);

    @Query("SELECT a FROM Agreement a WHERE a.deletedIndicator = false")
    List<Agreement> findAllActive();

    @Query("SELECT a FROM Agreement a WHERE a.expiryDate < :date AND a.deletedIndicator = false")
    List<Agreement> findExpiredAgreements(@Param("date") LocalDate date);

    @Query("SELECT a FROM Agreement a WHERE a.maturityDate < :date AND a.deletedIndicator = false")
    List<Agreement> findMatureAgreements(@Param("date") LocalDate date);

    @Query("SELECT a FROM Agreement a WHERE a.statusCode = :status AND a.expiryDate BETWEEN :startDate AND :endDate")
    List<Agreement> findByStatusAndExpiryDateBetween(
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    boolean existsByAgreementNo(String agreementNo);
}