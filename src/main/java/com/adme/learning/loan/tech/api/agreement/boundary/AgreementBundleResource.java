package com.adme.learning.loan.tech.api.agreement.boundary;

import com.adme.learning.loan.tech.api.agreement.control.AgreementBundleProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST endpoints for agreement management.
 */
@RestController
@RequestMapping("/api/v1/agreements")
@RequiredArgsConstructor
@Slf4j
public class AgreementBundleResource implements MessageResource {

    private final AgreementBundleProcessor agreementBundleProcessor;

    /**
     * Creates a new agreement bundle with related entities.
     *
     * @param dto the agreement bundle data
     * @return ResponseEntity with HTTP 201 Created status
     */
    @PostMapping("/bundle")
    public ResponseEntity<Void> createAgreementBundle( @RequestBody AgreementBundleDTO dto) {
        try {
            agreementBundleProcessor.prepareForCreate(dto);
            log.info(AGREEMENT_CREATED, dto.getAgreement().getAgreementNo());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException ex) {
            log.error(AGREEMENT_NOT_CREATED, dto.getAgreement().getAgreementNo(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * Updates an existing agreement bundle with related entities.
     *
     * @param dto the agreement bundle data
     * @return ResponseEntity with HTTP 200 OK status
     * @throws ResponseStatusException with HTTP 404 if agreement not found
     */
    @PutMapping("/bundle")
    public ResponseEntity<Void> updateAgreementBundle( @RequestBody AgreementBundleDTO dto) {
        try {
            agreementBundleProcessor.prepareForUpdate(dto);
            log.info(AGREEMENT_UPDATED, dto.getAgreement().getAgreementNo());
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException ex) {
            log.error(AGREEMENT_NOT_UPDATED, dto.getAgreement().getAgreementNo(), ex.getMessage());
            if (ex.getStatusCode().value() == HttpStatus.UNPROCESSABLE_CONTENT.value()) {
                throw new ResourceNotFoundException(ex.getReason());
            }
            throw ex;
        }
    }

    /**
     * Soft deletes an agreement bundle by setting its deletedIndicator to true.
     *
     * @param dto the agreement bundle data
     * @return ResponseEntity with HTTP 200 OK if newly purged, HTTP 204 No Content if already purged
     * @throws ResponseStatusException with HTTP 404 if agreement not found
     */
    @DeleteMapping("/bundle")
    public ResponseEntity<Void> purgeAgreementBundle( @RequestBody AgreementBundleDTO dto) {
        try {
            boolean wasPurged = agreementBundleProcessor.purgeAgreement(dto);
            if (wasPurged) {
                log.info(AGREEMENT_PURGED, dto.getAgreement().getAgreementNo());
                return ResponseEntity.ok().build();
            } else {
                log.warn(AGREEMENT_ALREADY_PURGED, dto.getAgreement().getAgreementNo());
                return ResponseEntity.noContent().build();
            }
        } catch (ResponseStatusException ex) {
            log.error(AGREEMENT_NOT_FOUND, dto.getAgreement().getAgreementNo());
            throw ex;
        }
    }
}
