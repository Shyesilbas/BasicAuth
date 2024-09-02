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
    public ResponseEntity<?> createAccount(@RequestBody CreateAccount createAccount) {
        accountService.createAccount(createAccount);
        return ResponseEntity.ok("Account created Successfully");
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
