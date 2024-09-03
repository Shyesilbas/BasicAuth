package org.example.basicauth.dto;

import lombok.Data;

@Data
public class MonthlyPaymentRequest {

    private Long loanId;
    private Long accountNumber;

}
