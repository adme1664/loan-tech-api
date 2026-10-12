package com.adme.learning.loan.tech.api.creditfacility.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for AgreementRelation entities.
 */
@Repository
public interface AgreementRelationRepository extends JpaRepository<AgreementRelation, UUID> {

    List<AgreementRelation> findByAgreementId(String agreementId);

}