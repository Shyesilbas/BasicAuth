package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.CreditCard;
import org.example.basicauth.Service.CreditCardService;
import org.example.basicauth.dto.CreateCreditCard;
import org.example.basicauth.dto.CreditCardDto;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/creditCard")
@RestController
public class CreditCardController {

    private final CreditCardService creditCardService;

    @PostMapping("/create")
    public CreditCardDto createCreditCard(@RequestBody CreateCreditCard createCreditCard){
        return creditCardService.createCreditCard(createCreditCard);
    }
    @DeleteMapping("/delete/{cardNumber}")
    public void deleteCard(@PathVariable String cardNumber){
        creditCardService.deleteCreditCard(cardNumber);
    }

}
