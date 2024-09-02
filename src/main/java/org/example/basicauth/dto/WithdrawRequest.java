package org.example.basicauth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    private Long senderAccNum;
    private BigDecimal amount;
    private String description;

}
