package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Account;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Repository.AccountRepository;
import org.example.basicauth.Repository.CurrentUser;
import org.example.basicauth.Service.AccountService;
import org.example.basicauth.dto.CreateAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService  accountService;
    private final AccountRepository accountRepository;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccount createAccount) {
        Account account = accountService.createAccount(createAccount);
        return ResponseEntity.ok(account);
    }
    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<?> deleteAccount( @PathVariable  Long accountNumber){
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(account.isPresent()){
            accountService.deleteAccount(accountNumber);
        }else {
            throw new RuntimeException("Account not found");
        }
        return ResponseEntity.ok("Account deleted successfully");
    }

}
