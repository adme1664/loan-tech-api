package com.adme.learning.loan.tech.api.creditfacility.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Deal entities.
 */
@Repository
public interface DealRepository extends JpaRepository<Deal, UUID> {

    Optional<Deal> findByBorrowingAgreementId(String borrowingAgreementId);

    boolean existsByBorrowingAgreementId(String borrowingAgreementId);
}