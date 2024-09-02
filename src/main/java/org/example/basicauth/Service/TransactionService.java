package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Account;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Model.TransactionType;
import org.example.basicauth.Model.Transactions;
import org.example.basicauth.Repository.AccountRepository;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.Repository.TransactionRepository;
import org.example.basicauth.dto.DepositRequest;
import org.example.basicauth.dto.TransferRequest;
import org.example.basicauth.dto.WithdrawRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Transactions deposit(DepositRequest depositRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username == null){
            throw new RuntimeException("You have to be logged in");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Customer not found"));

        Account account = accountRepository.findByAccountNumber(depositRequest.getReceiverAccNum())
                .orElseThrow(()-> new RuntimeException("Receiver account num not found"));

        if(account.getCustomer() != customer){
            throw new RuntimeException("Account does not belong to the logged-in customer");
        }
        if(depositRequest.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new RuntimeException("Deposit cannot be negative");
        }
        account.setBalance(account.getBalance().add(depositRequest.getAmount()));
        accountRepository.save(account);


        Transactions deposit = new Transactions();
        deposit.setSenderAccNum(null);
        deposit.setReceiverAccNum(depositRequest.getReceiverAccNum());
        deposit.setAmount(depositRequest.getAmount());
        deposit.setDescription(depositRequest.getDescription());
        deposit.setTransactionType(TransactionType.DEPOSIT);
        deposit.setCustomer(customer);

        return transactionRepository.save(deposit);
    }
    @Transactional
    public Transactions withdraw(WithdrawRequest withdrawRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username == null){
            throw new RuntimeException("You have to be logged in");
        }
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Customer not found"));

        Account account = accountRepository.findByAccountNumber(withdrawRequest.getSenderAccNum())
                .orElseThrow(()-> new RuntimeException("Receiver account num not found"));

        if(account.getCustomer() != customer){
            throw new RuntimeException("Account does not belong to the logged-in customer");
        }

        if(account.getBalance().compareTo(withdrawRequest.getAmount())<0){
            throw new RuntimeException("Insufficient Funds!");
        }
        if(withdrawRequest.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new RuntimeException("Withdraw request cannot be negative!");
        }

        account.setBalance(account.getBalance().subtract(withdrawRequest.getAmount()));
        accountRepository.save(account);

        Transactions withdraw = new Transactions();
        withdraw.setSenderAccNum(withdrawRequest.getSenderAccNum());
        withdraw.setReceiverAccNum(null);
        withdraw.setAmount(withdrawRequest.getAmount());
        withdraw.setDescription(withdrawRequest.getDescription());
        withdraw.setTransactionType(TransactionType.WITHDRAW);
        withdraw.setCustomer(customer);

        return transactionRepository.save(withdraw);
    }

@Transactional
    public Transactions transfer(TransferRequest transferRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username == null){
            throw new RuntimeException("not logged in");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Customer not found"));

            Account senderAccount = accountRepository.findByAccountNumber(transferRequest.getSenderAccNum())
                    .orElseThrow(()-> new RuntimeException("Sender Account not found"));

            Account receiverAccount = accountRepository.findByAccountNumber(transferRequest.getReceiverAccNum())
                    .orElseThrow(()-> new RuntimeException("Receiver Account not found"));

            if(senderAccount.getCustomer() != customer){
                throw new RuntimeException("Customer does not have account with given number");
            }
    if(receiverAccount.getCustomer() != customer){
        throw new RuntimeException("Customer does not have account with given number");
    }
            if(senderAccount.getBalance().compareTo(transferRequest.getAmount())<0){
                throw new RuntimeException("Insufficient funds!");
            }
            if(transferRequest.getAmount().compareTo(BigDecimal.ZERO)<0){
                throw new RuntimeException("Transfer amount must be positive");
            }
            if(!senderAccount.getCurrency().equals(receiverAccount.getCurrency())){
                throw new RuntimeException("Currency Mismatch between accounts!");
            }
            senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequest.getAmount()));
            receiverAccount.setBalance(receiverAccount.getBalance().add(transferRequest.getAmount()));
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            Transactions transfer = new Transactions();
            transfer.setSenderAccNum(transferRequest.getSenderAccNum());
            transfer.setReceiverAccNum(transferRequest.getReceiverAccNum());
            transfer.setAmount(transferRequest.getAmount());
            transfer.setDescription(transferRequest.getDescription());
            transfer.setTransactionType(TransactionType.TRANSFER);
            transfer.setCustomer(customer);

            return transactionRepository.save(transfer);
}
}
