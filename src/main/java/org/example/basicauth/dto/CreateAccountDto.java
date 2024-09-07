package org.example.basicauth.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.basicauth.Model.AccountType;
import org.example.basicauth.Model.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateAccountDto {
    private String username;
    private Long personalId;
    private Long customerId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true)
    private Long accountNumber;

    private String accountName;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String message;
}
