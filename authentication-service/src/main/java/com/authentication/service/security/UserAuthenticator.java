package com.authentication.service.security;

import lombok.RequiredArgsConstructor;
import com.authentication.service.dto.AuthenticationInfo;
import com.authentication.service.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticator {

    public static final String BEARER_PREFIX = "Bearer ";

    private final CustomAuthenticationProvider authenticationProvider;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public String authenticate(AuthenticationInfo auth) {
        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
            );

            return jwtUtil.generateToken(auth.getUsername());
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    public ResponseEntity<String> userLogout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {

            return ResponseEntity.badRequest().body("No token provided");
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        tokenBlacklistService.addToBlacklist(token);

        return ResponseEntity.ok("Logout successful");
    }

}