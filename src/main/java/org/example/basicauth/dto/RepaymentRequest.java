package org.example.basicauth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentRequest {
    private BigDecimal repaymentAmount;
}
