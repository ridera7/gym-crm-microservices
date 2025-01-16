package com.gym.crm.application.security;

import com.gym.crm.application.service.impl.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticator {

    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenBlacklistService tokenBlacklistService;

    public ResponseEntity<String> userLogout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {

            return ResponseEntity.badRequest().body("No token provided");
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        tokenBlacklistService.addToBlacklist(token);

        return ResponseEntity.ok("Logout successful");
    }

}