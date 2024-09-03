package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.LoanApplication;
import org.example.basicauth.Service.LoanApplicationService;
import org.example.basicauth.dto.CreateLoanApplication;
import org.example.basicauth.dto.FullPaymentRequest;
import org.example.basicauth.dto.MonthlyPaymentRequest;
import org.example.basicauth.dto.RepaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/loanApplication")
@RestController
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplication> applyForLoan(@RequestBody CreateLoanApplication createLoanApplication) {
        LoanApplication loanApplication = loanApplicationService.createLoanApplication(createLoanApplication);
        return ResponseEntity.ok(loanApplication);
    }
    @DeleteMapping("/delete/{loanId}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long loanId){
        loanApplicationService.deleteLoanApplication(loanId);
       return ResponseEntity.ok("Loan deleted Successfully");
    }

    @PostMapping("/monthly-payment")
    public ResponseEntity<String> processMonthlyPayment(@RequestBody MonthlyPaymentRequest monthlyPaymentRequest) {
        loanApplicationService.processMonthlyPayment(monthlyPaymentRequest);
        return ResponseEntity.ok("Monthly payment processed successfully.");
    }

    @PostMapping("/full-payment")
    public ResponseEntity<String> processFullPayment(@RequestBody FullPaymentRequest fullPaymentRequest) {
        loanApplicationService.processFullPayment(fullPaymentRequest);
        return ResponseEntity.ok("Full payment processed successfully.");
    }


}
