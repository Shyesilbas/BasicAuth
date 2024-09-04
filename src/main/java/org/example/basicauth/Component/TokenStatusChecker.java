package org.example.basicauth.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.basicauth.Service.TokenService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStatusChecker {

    private final TokenService tokenService;

    @PostConstruct
    public void onStartup() {
        tokenService.checkAndExpireTokens();
    }

}
