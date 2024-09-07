package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Account;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Repository.AccountRepository;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.dto.CreateAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void createAccount(CreateAccount createAccount) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new RuntimeException("You have to be logged in");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = new Account();
        account.setAccountNumber(createAccount.getAccountNumber());
        account.setAccountName(createAccount.getAccountName());
        account.setBalance(createAccount.getBalance());
        account.setCurrency(createAccount.getCurrency());
        account.setCustomer(customer);
        account.setAccountType(createAccount.getAccountType());
        account.createdAt();
        customer.setActiveAccounts(customer.getActiveAccounts()+1);
        customerRepository.save(customer);
        accountRepository.save(account);
    }
 @Transactional
 public void  deleteAccount (Long accountNumber){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

     if (username == null) {
         throw new RuntimeException("You have to be logged in");
     }

     Customer customer = customerRepository.findByUsername(username)
             .orElseThrow(() -> new RuntimeException("Customer not found"));

        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(account.isPresent()){
            accountRepository.deleteByAccountNumber(accountNumber);
            customer.setActiveAccounts(customer.getActiveAccounts()-1);
        }else{
            throw new RuntimeException("Account not found");
        }
 }


}
