package com.adme.learning.loan.tech.api.creditfacility.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for CreditFacility entities.
 */
@Repository
public interface CreditFacilityRepository extends JpaRepository<CreditFacility, UUID> {

    Optional<CreditFacility> findByCreditFacilityNo(String creditFacilityNo);

    List<CreditFacility> findByBorrowingAgreementId(String borrowingAgreementId);

    List<CreditFacility> findByStatusCode(String statusCode);

    boolean existsByCreditFacilityNo(String creditFacilityNo);
}