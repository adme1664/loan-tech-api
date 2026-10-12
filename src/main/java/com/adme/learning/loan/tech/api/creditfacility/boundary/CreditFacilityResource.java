package com.adme.learning.loan.tech.api.creditfacility.boundary;

import com.adme.learning.loan.tech.api.agreement.boundary.MessageResource;
import com.adme.learning.loan.tech.api.agreement.boundary.ResourceNotFoundException;
import com.adme.learning.loan.tech.api.creditfacility.control.DealBundleProcessor;
import java.util.Objects;
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
 * REST endpoints for credit facility management.
 */
@RestController
@RequestMapping("/api/v1/credit-facilities")
@RequiredArgsConstructor
@Slf4j
public class CreditFacilityResource implements MessageResource {

    private final DealBundleProcessor dealBundleProcessor;

    /**
     * Creates a new credit facility bundle with related entities.
     *
     * @param dto the credit facility bundle data
     * @return ResponseEntity with HTTP 201 Created status
     */
    @PostMapping(path = "bundle", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> createDealBundle(@RequestBody DealBundleDTO dto) {
        try {
            if (Objects.isNull(dto.getDeal())) {
                return ResponseEntity.ok().build();
            }
            dealBundleProcessor.prepareForCreate(dto);
            log.info(DEAL_CREATED, dto.getDealNo());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            log.error(ERROR_DEAL_CREATED, dto.getDealNo(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * Updates an existing credit facility bundle with related entities.
     *
     * @param dto the credit facility bundle data
     * @return ResponseEntity with HTTP 200 OK status
     * @throws ResponseStatusException with HTTP 404 if credit facility not found
     */
    @PutMapping(path = "bundle", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateCreditFacilityBundle(@RequestBody DealBundleDTO dto) {
        try {
            dealBundleProcessor.prepareForUpdate(dto);
            log.info(DEAL_UPDATED, dto.getDealNo());
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException ex) {
            log.error(ERROR_DEAL_UPDATED, dto.getDealNo(), ex.getMessage());
            if (ex.getStatusCode().value() == HttpStatus.UNPROCESSABLE_CONTENT.value()) {
                throw new ResourceNotFoundException(ex.getReason());
            }
            throw ex;
        }
    }

    /**
     * Purges a credit facility bundle.
     *
     * @param dto the credit facility bundle data
     * @return ResponseEntity with HTTP 200 OK if purged, HTTP 204 No Content if already purged
     * @throws ResponseStatusException with HTTP 404 if credit facility not found
     */
    @DeleteMapping(path = "/bundle", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> purgeCreditFacilityBundle(@RequestBody DealBundleDTO dto) {
        try {
            boolean wasPurged = dealBundleProcessor.purgeCreditFacility(dto);
            if (wasPurged) {
                log.info(DEAL_PURGED, dto.getDealNo());
                return ResponseEntity.ok().build();
            } else {
                log.warn(DEAL_ALREADY_PURGED, dto.getDealNo());
                return ResponseEntity.noContent().build();
            }
        } catch (ResponseStatusException ex) {
            log.error(DEAL_FOUND, dto.getDealNo(), ex.getMessage());
            throw ex;
        }
    }
}