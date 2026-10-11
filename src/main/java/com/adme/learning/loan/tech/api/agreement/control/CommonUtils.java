package com.adme.learning.loan.tech.api.agreement.control;

import java.time.Instant;

public class CommonUtils {

  private CommonUtils() {
  }

  public static Instant convertStringToInstant(String date) {
    return Instant.parse(date);
  }

}
