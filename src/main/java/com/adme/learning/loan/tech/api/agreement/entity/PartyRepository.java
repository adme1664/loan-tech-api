package com.adme.learning.loan.tech.api.agreement.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Party entities.
 */
@Repository
public interface PartyRepository extends JpaRepository<Party, UUID> {

    List<Party> findByAgreement(Agreement agreement);

    List<Party> findByAgreementAgreementId(UUID agreementId);

    Optional<Party> findByMemberIdNo(String memberIdNo);

    Optional<Party> findByUnbankId(String unbankId);

    List<Party> findByAgreementAgreementIdAndMemberIdNo(UUID agreementId, String memberIdNo);

    @Query("SELECT p FROM Party p WHERE p.agreement.agreementId = :agreementId")
    List<Party> findAllByAgreementId(@Param("agreementId") UUID agreementId);

    @Query("SELECT p FROM Party p WHERE p.unbankId IS NOT NULL AND p.unbankId <> ''")
    List<Party> findAllWithUnbankId();

    boolean existsByAgreementAgreementIdAndMemberIdNo(UUID agreementId, String memberIdNo);
}
