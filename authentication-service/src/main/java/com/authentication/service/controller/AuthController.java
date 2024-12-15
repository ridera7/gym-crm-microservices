package com.authentication.service.controller;

import com.authentication.service.dto.AuthenticationInfo;
import com.authentication.service.security.UserAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthenticator userAuthenticator;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationInfo auth) {
        String token = userAuthenticator.authenticate(auth);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization")
                                           String authorization) {

        return ResponseEntity.ok("Token is valid");
    }
}

