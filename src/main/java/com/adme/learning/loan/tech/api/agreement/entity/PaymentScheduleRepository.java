package com.adme.learning.loan.tech.api.agreement.entity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for PaymentSchedule entity operations.
 */
@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {

    /**
     * Finds all payment schedules for a given agreement.
     *
     * @param agreementId the agreement UUID
     * @return list of payment schedules
     */
    List<PaymentSchedule> findByAgreementAgreementId(UUID agreementId);

    /**
     * Finds a payment schedule by agreement ID, payment date, and schedule date.
     *
     * @param agreementId the agreement UUID
     * @param paymentDate the payment date
     * @param scheduleDate the schedule date
     * @return Optional containing the payment schedule if found
     */
    Optional<PaymentSchedule> findByAgreementAgreementIdAndPaymentDateAndScheduleDate(
            UUID agreementId,
            Instant paymentDate,
            Instant scheduleDate
    );

    /**
     * Finds payment schedules by agreement ID and payment date.
     *
     * @param agreementId the agreement UUID
     * @param paymentDate the payment date
     * @return list of payment schedules
     */
    List<PaymentSchedule> findByAgreementAgreementIdAndPaymentDate(
            UUID agreementId,
            Instant paymentDate
    );

    /**
     * Deletes all payment schedules for a given agreement.
     *
     * @param agreementId the agreement UUID
     */
    void deleteByAgreementAgreementId(UUID agreementId);
}