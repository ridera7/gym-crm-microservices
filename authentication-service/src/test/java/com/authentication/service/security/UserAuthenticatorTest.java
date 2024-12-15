package com.authentication.service.security;

import com.authentication.service.dto.AuthenticationInfo;
import com.authentication.service.exception.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticatorTest {

    private static final String BEARER_ = "Bearer ";

    @Mock
    private CustomAuthenticationProvider authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private UserAuthenticator userAuthenticator;

    @Test
    void shouldSuccessAuthenticate() {
        AuthenticationInfo authInfo = new AuthenticationInfo("testUser", "testPassword");
        String expectedToken = "jwt-token";
        when(jwtUtil.generateToken(authInfo.getUsername())).thenReturn(expectedToken);

        String actualToken = userAuthenticator.authenticate(authInfo);

        assertEquals(expectedToken, actualToken);
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(authInfo.getUsername(), authInfo.getPassword())
        );
        verify(jwtUtil).generateToken(authInfo.getUsername());
    }

    @Test
    void shouldFailureAuthenticate() {
        AuthenticationInfo authInfo = new AuthenticationInfo("testUser", "wrongPassword");
        UsernamePasswordAuthenticationToken expectedToken = new UsernamePasswordAuthenticationToken(authInfo.getUsername(), authInfo.getPassword());
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(expectedToken);

        assertThrows(AuthenticationException.class, () -> userAuthenticator.authenticate(authInfo));

        verify(authenticationManager).authenticate(expectedToken);
        verify(jwtUtil, never()).generateToken(anyString());
    }

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