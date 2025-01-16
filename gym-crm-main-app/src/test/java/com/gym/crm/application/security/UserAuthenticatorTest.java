package com.gym.crm.application.security;

import com.gym.crm.application.service.impl.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAuthenticatorTest {

    private static final String BEARER_ = "Bearer ";

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private UserAuthenticator userAuthenticator;

    @Test
    public void shouldLogoutSuccess() {
        String authorizationHeader = BEARER_ + "testToken";

        ResponseEntity<String> response = userAuthenticator.userLogout(authorizationHeader);

        assertEquals(ResponseEntity.ok("Logout successful"), response);

        verify(tokenBlacklistService).addToBlacklist("testToken");
    }

    @Test
    public void shouldReturnNoTokenProvided() {
        String authorizationHeader = null;

        ResponseEntity<String> response = userAuthenticator.userLogout(authorizationHeader);

        assertEquals(ResponseEntity.badRequest().body("No token provided"), response);
    }

    @Test
    public void shouldReturnNoTokenProvidedWhenInvalidTokenFormat() {
        String authorizationHeader = "InvalidTokenFormat";

        ResponseEntity<String> response = userAuthenticator.userLogout(authorizationHeader);

        assertEquals(ResponseEntity.badRequest().body("No token provided"), response);
    }

}