package org.example.basicauth.Repository;

import org.example.basicauth.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(Long accountNumber);
    Optional<Account> findByAccountName(String accountName);
    void deleteByAccountNumber(Long accountNumber);

}
