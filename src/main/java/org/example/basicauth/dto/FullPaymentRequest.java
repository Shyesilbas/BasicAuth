package org.example.basicauth.dto;

import lombok.Data;

@Data
public class FullPaymentRequest {
    private Long loanId;
    private Long accountNumber;

}
