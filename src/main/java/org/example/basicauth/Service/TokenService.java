package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Jwt.JwtUtil;
import org.example.basicauth.Model.BlackListedToken;
import org.example.basicauth.Model.Token;
import org.example.basicauth.Model.TokenStatus;
import org.example.basicauth.Repository.BlackListTokenRepository;
import org.example.basicauth.Repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final BlackListTokenRepository blackListTokenRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public boolean hasActiveToken(String username) {
        Optional<Token> activeTokenOpt = tokenRepository.findByUsernameAndStatus(username, TokenStatus.ACTIVE);
        return activeTokenOpt.isPresent();
    }

    public void saveToken(String token, int expiryHours , String username) {
        Token newToken = new Token();
        newToken.setUsername(username);
        newToken.setToken(token);
        newToken.setStatus(TokenStatus.ACTIVE);
        newToken.setCreatedAt(LocalDateTime.now());
        newToken.setExpiryHours(expiryHours);

        tokenRepository.save(newToken);
    }

    public void invalidateToken(String token) {
        Optional<Token> savedToken = tokenRepository.findByToken(token);
        savedToken.ifPresent(t -> {
            t.setStatus(TokenStatus.EXPIRED);
            tokenRepository.save(t);

            BlackListedToken blackListedToken = new BlackListedToken();
            blackListedToken.setUsername(t.getUsername());
            blackListedToken.setToken(token);
            blackListedToken.setExpirationDate();
            blackListTokenRepository.save(blackListedToken);
        });
    }

    public boolean isTokenBlacklisted(String token) {
        return blackListTokenRepository.findByToken(token).isPresent();
    }

    public boolean isTokenValid(String token, String username) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isPresent()) {
            Token existingToken = tokenOpt.get();
            return !existingToken.getStatus().equals(TokenStatus.EXPIRED) &&
                    jwtUtil.extractUsername(token).equals(username);
        }
        return false;
    }
    public void checkAndExpireTokens() {
        List<Token> tokens = tokenRepository.findAll();

        for (Token token : tokens) {
            if (token.getExpiresAt().isBefore(LocalDateTime.now()) && token.getStatus() == TokenStatus.ACTIVE) {
                token.setStatus(TokenStatus.EXPIRED);
                tokenRepository.save(token);

                BlackListedToken blackListedToken = new BlackListedToken();
                blackListedToken.setToken(token.getToken());
                blackListTokenRepository.save(blackListedToken);
            }
        }
    }

}
