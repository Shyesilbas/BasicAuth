package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.*;
import org.example.basicauth.Repository.AccountRepository;
import org.example.basicauth.Repository.CreditCardRepository;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.dto.CreateCreditCard;
import org.example.basicauth.dto.CreditCardDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CreditCardDto createCreditCard(CreateCreditCard createCreditCard){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username == null){
            throw new RuntimeException("You have to log in");
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Customer not found"));


        String cardNumber = generateUniqueCardNumber();
        String cvv = generateUniqueCVV();

        validateCardLimit(createCreditCard.getCardLimit(), customer.getSalary());

        CreditCard newCard = new CreditCard();
        newCard.setCardNumber(cardNumber);
        newCard.setCvv(cvv);
        newCard.setCardLimit(createCreditCard.getCardLimit());
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setCardType(createCreditCard.getCardType());
        newCard.setCustomer(customer);
        customer.setActiveCreditCards(customer.getActiveCreditCards()+1);
         creditCardRepository.save(newCard);

        return new CreditCardDto(
                newCard.getCardNumber(),
                newCard.getCvv(),
                newCard.getCardLimit(),
                newCard.getExpirationDate(),
                newCard.getCardType(),
                customer.getCustomerId(),
                customer.getPersonalId(),
                customer.getUsername(),
                STR."Credit Card Created successfully with the given Info , for \{customer.getUsername()}"
        );
    }

    public void deleteCreditCard(String cardNumber){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(username == null){
            throw new RuntimeException("You have to log in");
        }

        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(()-> new RuntimeException("Card not found"));

        creditCardRepository.delete(creditCard);
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                sb.append(random.nextInt(10));
            }
            cardNumber = sb.toString();
        } while (creditCardRepository.findByCardNumber(cardNumber).isPresent());
        return cardNumber;
    }


    private String generateUniqueCVV() {
        String cvv;
        Random random = new Random();
        do {
            cvv = String.valueOf(random.nextInt(900) + 100);
        } while (creditCardRepository.findByCvv(cvv).isPresent());
        return cvv;
    }

    private void validateCardLimit(BigDecimal cardLimit, BigDecimal customerSalary) {
        if (cardLimit.compareTo(customerSalary.multiply(BigDecimal.valueOf(4))) > 0) {
            throw new IllegalArgumentException("Card limit cannot exceed four times the customer's salary.");
        }
    }


}



