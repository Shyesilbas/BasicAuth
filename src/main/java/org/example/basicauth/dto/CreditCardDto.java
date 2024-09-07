package org.example.basicauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.basicauth.Model.CreditCardType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreditCardDto {

    private String cardNumber;
    private String cvv;
    private BigDecimal cardLimit;
    private LocalDate expirationDate;
    private CreditCardType type;

    // Customer information
    private Long customerId;
    private Long personalId;
    private String username;
    private String message;

}
