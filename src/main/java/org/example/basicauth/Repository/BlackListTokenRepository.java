package org.example.basicauth.Repository;

import org.example.basicauth.Model.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListTokenRepository extends JpaRepository<BlackListedToken,Long> {
    Optional<BlackListedToken> findByToken(String token);
}
