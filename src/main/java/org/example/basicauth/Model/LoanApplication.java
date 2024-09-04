package org.example.basicauth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicatedBy", nullable = false , referencedColumnName = "username")
    private Customer customer;

    private BigDecimal amount;
    private int installment;
    private Long accountNumber;
    private int appCounter = 0;

    @Transient
    private final BigDecimal interestRate = new BigDecimal("0.05");

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountNum", nullable = false, referencedColumnName = "accountNumber")
    private Account account;

    private BigDecimal totalRepaymentWithInterestRate;
    private BigDecimal monthlyPayment;
    private int installmentLeft = installment;

    @Enumerated(EnumType.STRING)
    private LoanPaymentStatus paymentStatus;

    private BigDecimal totalPaid = BigDecimal.ZERO;
    private BigDecimal remainingAmount;

    @PostLoad
    public void calculatePayments() {
        BigDecimal totalRepayment = amount.multiply(BigDecimal.ONE.add(interestRate).pow(installment));
        this.totalRepaymentWithInterestRate = totalRepayment;
        this.monthlyPayment = totalRepayment.divide(BigDecimal.valueOf(installment), RoundingMode.HALF_UP);
        this.remainingAmount = totalRepayment;
        if (this.installmentLeft == 0) {
            this.installmentLeft = this.installment;
        }
    }

}

