package org.example.basicauth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Long accountNumber;
    private String accountName;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountHolder", nullable = false , referencedColumnName = "username")
    private Customer customer;
}
