package com.adme.learning.loan.tech.api.creditfacility.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for CfParty entities.
 */
@Repository
public interface CfPartyRepository extends JpaRepository<CfParty, UUID> {

    List<CfParty> findByCreditFacility_CreditFacilityId(UUID creditFacilityId);

    Optional<CfParty> findByMemberIdNo(String memberIdNo);

    boolean existsByMemberIdNo(String memberIdNo);
}