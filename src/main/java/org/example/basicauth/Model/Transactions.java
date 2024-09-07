package org.example.basicauth.Model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long senderAccNum;
    private Long receiverAccNum;
    private BigDecimal amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionBy", nullable = false , referencedColumnName = "username")
    private Customer customer;

    private LocalDate date;

    @PostConstruct
    public void setDate(){
        this.date=LocalDate.now();
    }

}
