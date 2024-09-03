package org.example.basicauth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.basicauth.Model.CreditCardType;

import java.math.BigDecimal;

@Data
public class CreateCreditCard {
    @Column(nullable = false)
    private BigDecimal cardLimit;
    @Enumerated(EnumType.STRING)
    private CreditCardType cardType;
}
