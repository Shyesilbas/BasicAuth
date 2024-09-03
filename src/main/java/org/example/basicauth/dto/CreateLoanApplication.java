package org.example.basicauth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLoanApplication {

    private BigDecimal amount;
    private int installment;
    private Long accountNumber;

}
