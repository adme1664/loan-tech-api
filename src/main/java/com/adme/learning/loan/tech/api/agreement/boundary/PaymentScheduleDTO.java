package com.adme.learning.loan.tech.api.agreement.boundary;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleDTO {
  private UUID paymentScheduleId;
  private UUID agreementId;
  private BigDecimal paymentAmnt;
  private String paymentDate;
  private String businessDate;

}
