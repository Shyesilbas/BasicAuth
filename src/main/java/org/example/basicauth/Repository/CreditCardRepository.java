package org.example.basicauth.Repository;

import org.example.basicauth.Model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard , Long> {
    Optional<CreditCard> findByCardNumber(String cardNumber);
    Optional<CreditCard> findByCvv(String cvv);
}
