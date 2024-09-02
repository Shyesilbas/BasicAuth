package org.example.basicauth.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCreditCard {
    @Column(nullable = false)
    private BigDecimal cardLimit;
}
