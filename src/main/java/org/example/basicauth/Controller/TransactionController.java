package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Transactions;
import org.example.basicauth.Service.TransactionService;
import org.example.basicauth.dto.DepositRequest;
import org.example.basicauth.dto.TransferRequest;
import org.example.basicauth.dto.WithdrawRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public Transactions deposit(@RequestBody DepositRequest depositRequest){
        return transactionService.deposit(depositRequest);
    }
    @PostMapping("/withdraw")
    public Transactions withdraw(@RequestBody WithdrawRequest withdrawRequest){
        return transactionService.withdraw(withdrawRequest);
    }
    @PostMapping("/transfer")
    public Transactions transfer(@RequestBody TransferRequest transferRequest){
        return transactionService.transfer(transferRequest);
    }




}
