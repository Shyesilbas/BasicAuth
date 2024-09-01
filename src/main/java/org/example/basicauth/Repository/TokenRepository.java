package org.example.basicauth.Repository;

import org.example.basicauth.Model.Token;
import org.example.basicauth.Model.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByToken(String token);

    Optional<Token> findByUsernameAndStatus(String username, TokenStatus tokenStatus);
}
