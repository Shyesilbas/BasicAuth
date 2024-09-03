package org.example.basicauth.Repository;

import org.example.basicauth.Model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication , Long> {

    Optional<LoanApplication> findByAccountNumber(Long accountNumber);

}
