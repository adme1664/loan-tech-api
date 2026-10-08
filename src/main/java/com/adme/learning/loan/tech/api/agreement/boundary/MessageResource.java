package com.adme.learning.loan.tech.api.agreement.boundary;

public interface MessageResource {
  static final String AGREEMENT_NOT_FOUND = "Agreement {} not found";
  static final String AGREEMENT_ALREADY_EXISTS = "Agreement {} already exists";
  static final String AGREEMENT_CREATED = "Agreement {} created successfully";
  static final String AGREEMENT_UPDATED = "Agreement {} updated successfully";
  static final String AGREEMENT_PURGED = "Agreement {} purged successfully";
  static final String AGREEMENT_ALREADY_PURGED = "Agreement {} already purged...";
  static final String AGREEMENT_NOT_UPDATED = "Error while updating Agreement {}. Message: {}";
  static final String AGREEMENT_NOT_CREATED = "Error while creating Agreement {}. Message: {}";

}
