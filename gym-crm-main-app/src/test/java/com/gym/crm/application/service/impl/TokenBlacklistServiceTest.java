package com.gym.crm.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    public void setUp() {
        tokenBlacklistService = new TokenBlacklistService();
    }

    @Test
    public void shouldAddToBlacklist() {
        String token = "testToken";

        assertFalse(tokenBlacklistService.isTokenBlacklisted(token));

        tokenBlacklistService.addToBlacklist(token);

        assertTrue(tokenBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    public void shouldReturnFalseWhenTokenIsNotInBlacklist() {
        String token = "nonBlacklistedToken";

        assertFalse(tokenBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    public void shouldReturnTrueWhenTokenInBlacklist() {
        String token = "blacklistedToken";

        tokenBlacklistService.addToBlacklist(token);

        assertTrue(tokenBlacklistService.isTokenBlacklisted(token));
    }

}