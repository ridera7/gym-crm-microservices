package com.authentication.service.controller;

import com.authentication.service.dto.AuthenticationInfo;
import com.authentication.service.security.UserAuthenticator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserAuthenticator userAuthenticator;

    @Test
    void testLogin_Success() {
        AuthenticationInfo authInfo = new AuthenticationInfo("user", "password");
        String expectedToken = "sampleToken";

        when(userAuthenticator.authenticate(authInfo)).thenReturn(expectedToken);

        ResponseEntity<String> response = authController.login(authInfo);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedToken, response.getBody());

        verify(userAuthenticator).authenticate(authInfo);
    }

    @Test
    void testValidateToken_Success() {
        String authorizationHeader = "Bearer sampleToken";

        ResponseEntity<?> response = authController.validateToken(authorizationHeader);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Token is valid", response.getBody());
    }
}