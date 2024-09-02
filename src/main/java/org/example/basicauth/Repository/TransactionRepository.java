package org.example.basicauth.Repository;

import org.example.basicauth.Model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {

    Optional<Transactions> findByTransactionId(Long transactionId);
    Optional<Transactions> findBySenderAccNum(Long senderAccNum);
    Optional<Transactions> findByReceiverAccNum(Long receiverAccNum);

}
