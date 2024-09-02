package org.example.basicauth.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.basicauth.Model.AccountType;
import org.example.basicauth.Model.Currency;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Repository.CurrentUser;

import java.math.BigDecimal;

@Data
public class CreateAccount {

    private Long accountNumber;
    private String accountName;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private Currency currency;



}
