package org.example.basicauth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private Long receiverAccNum;
    private BigDecimal amount;
    private String description;
}
