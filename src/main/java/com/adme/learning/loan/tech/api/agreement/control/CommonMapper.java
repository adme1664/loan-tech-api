package com.adme.learning.loan.tech.api.agreement.control;

import java.time.Instant;

public class CommonMapper {

  private CommonMapper() {
  }
  public static Instant convertInstantToString(String date) {
    if (date == null) {
      return null;
    }
    //patter is 2031-09-30T00:00:00Z
    return Instant.parse(date);
  }

}
