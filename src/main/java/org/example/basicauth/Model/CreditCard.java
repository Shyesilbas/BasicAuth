package org.example.basicauth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.basicauth.Repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    @Column(nullable = false , unique = true)
    private String cvv;

    @Column(nullable = false)
    private BigDecimal cardLimit;

    @Column(nullable = false)
    private LocalDate expirationDate;

    private BigDecimal usedLimit = new BigDecimal("0");

    @Enumerated(EnumType.STRING)
    private CreditCardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardHolder", referencedColumnName = "username", nullable = false)
    private Customer customer;


}
