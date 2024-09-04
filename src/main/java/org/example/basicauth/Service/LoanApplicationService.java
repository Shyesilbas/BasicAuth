package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.*;
import org.example.basicauth.Repository.AccountRepository;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.Repository.LoanApplicationRepository;
import org.example.basicauth.Repository.TransactionRepository;
import org.example.basicauth.dto.CreateLoanApplication;
import org.example.basicauth.dto.FullPaymentRequest;
import org.example.basicauth.dto.MonthlyPaymentRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    @Transactional
    public LoanApplication createLoanApplication(CreateLoanApplication createLoanApplication) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("You have to log in");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = accountRepository.findByAccountNumber(createLoanApplication.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found or does not belong to the user"));

        if (!account.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("You don't have an account with the provided Account number");
        }

        BigDecimal maxLoanAmount = customer.getSalary().multiply(BigDecimal.valueOf(5));
        if (createLoanApplication.getAmount().compareTo(maxLoanAmount) > 0) {
            throw new RuntimeException("Loan amount cannot exceed five times the customer's income.");
        }

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setCustomer(customer);
        loanApplication.setAccount(account);
        loanApplication.setAmount(createLoanApplication.getAmount());
        loanApplication.setInstallment(createLoanApplication.getInstallment());
        loanApplication.setApplicationStatus(LoanApplicationStatus.APPROVED);
        loanApplication.setAppCounter(loanApplication.getAppCounter()+1);
        loanApplication.setAccountNumber(account.getAccountNumber());
        loanApplication.setPaymentStatus(LoanPaymentStatus.ONGOING);
        loanApplication.calculatePayments();
        loanApplication.setTotalPaid(BigDecimal.ZERO);
        loanApplication.setRemainingAmount(loanApplication.getTotalRepaymentWithInterestRate());
        account.setBalance(account.getBalance().add(createLoanApplication.getAmount()));
        customer.setActiveLoans(customer.getActiveLoans() + 1);

        customerRepository.save(customer);
        accountRepository.save(account);
        return loanApplicationRepository.save(loanApplication);
    }

    @Transactional
    public void deleteLoanApplication(Long loanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("You have to log in!");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LoanApplication loanApplication = loanApplicationRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan application not found"));

        if (!loanApplication.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to delete this loan application");
        }

        customer.setActiveLoans(customer.getActiveLoans() - 1);

        loanApplicationRepository.delete(loanApplication);
        customerRepository.save(customer);
    }

    @Transactional
    public void processMonthlyPayment(MonthlyPaymentRequest monthlyPaymentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("You have to log in!");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LoanApplication loanApplication = loanApplicationRepository.findById(monthlyPaymentRequest.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan application not found"));

        if (!loanApplication.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("This loan application does not belong to you");
        }

        Account paymentAccount = accountRepository.findByAccountNumber(monthlyPaymentRequest.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Payment account not found"));

        if (!paymentAccount.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("The payment account does not belong to you");
        }

        BigDecimal monthlyPayment = loanApplication.getMonthlyPayment();

        if (paymentAccount.getBalance().compareTo(monthlyPayment) < 0) {
            throw new RuntimeException("Insufficient funds for monthly payment.");
        }

        Transactions transactions = new Transactions();
        transactions.setTransactionType(TransactionType.LOAN_PMNT);
        transactions.setAmount(monthlyPayment);
        transactions.setReceiverAccNum(null);
        transactions.setSenderAccNum(paymentAccount.getAccountNumber());
        transactions.setDescription("Loan Monthly Payment");
        transactions.setCustomer(customer);
        transactionRepository.save(transactions);

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(monthlyPayment));
        loanApplication.setTotalPaid(loanApplication.getTotalPaid().add(monthlyPayment));
        loanApplication.setInstallmentLeft(loanApplication.getInstallment()-1);
        loanApplication.setRemainingAmount(loanApplication.getTotalRepaymentWithInterestRate().subtract(loanApplication.getTotalPaid()));


        if (loanApplication.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            loanApplication.setPaymentStatus(LoanPaymentStatus.PAID_OFF);
            loanApplication.setAmount(BigDecimal.ZERO);
            loanApplication.setRemainingAmount(BigDecimal.ZERO);
            loanApplication.getCustomer().setActiveLoans(loanApplication.getCustomer().getActiveLoans() - 1);
            loanApplication.setInstallmentLeft(0);
            customerRepository.save(loanApplication.getCustomer());
        }

        accountRepository.save(paymentAccount);
        loanApplicationRepository.save(loanApplication);
    }

    @Transactional
    public void processFullPayment(FullPaymentRequest fullPaymentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("You have to log in!");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LoanApplication loanApplication = loanApplicationRepository.findById(fullPaymentRequest.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan application not found"));

        if (!loanApplication.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("This loan application does not belong to you");
        }

        Account paymentAccount = accountRepository.findByAccountNumber(fullPaymentRequest.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Payment account not found"));

        if (!paymentAccount.getCustomer().getUsername().equals(username)) {
            throw new RuntimeException("The payment account does not belong to you");
        }

        BigDecimal remainingAmount = loanApplication.getTotalRepaymentWithInterestRate().subtract(loanApplication.getTotalPaid());

        if (paymentAccount.getBalance().compareTo(remainingAmount) < 0) {
            throw new RuntimeException("Insufficient funds for full payment.");
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(remainingAmount));
        loanApplication.setTotalPaid(loanApplication.getTotalRepaymentWithInterestRate());
        loanApplication.setRemainingAmount(BigDecimal.ZERO);
        loanApplication.setPaymentStatus(LoanPaymentStatus.PAID_OFF);
        loanApplication.setInstallment(loanApplication.getInstallment());
        loanApplication.setInstallmentLeft(0);

        customer.setActiveLoans(customer.getActiveLoans() - 1);
        Transactions transactions = new Transactions();
        transactions.setTransactionType(TransactionType.LOAN_PMNT);
        transactions.setAmount(remainingAmount);
        transactions.setReceiverAccNum(null);
        transactions.setSenderAccNum(paymentAccount.getAccountNumber());
        transactions.setDescription("Loan Full payment");
        transactions.setCustomer(customer);
        transactionRepository.save(transactions);

        accountRepository.save(paymentAccount);
        loanApplicationRepository.save(loanApplication);
        customerRepository.save(customer);
    }
}
