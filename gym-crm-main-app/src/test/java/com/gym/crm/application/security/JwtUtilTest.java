package com.gym.crm.application.security;


import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String username = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldCreateValidToken() {
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(username);

        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(username);

        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    void shouldReturnFalseForInvalidUsername() {
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.validateToken(token, "otheruser"));
    }

    @Test
    void shouldReturnFalseForExpiredToken() throws NoSuchFieldException, IllegalAccessException {
        Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        Object secretKey = secretKeyField.get(jwtUtil);

        String expiredToken = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith((SecretKey) secretKey)
                .compact();

        assertFalse(jwtUtil.validateToken(expiredToken, username));
    }

    @Test
    void shouldReturnFalseForValidToken() {
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.isTokenExpired(token));
    }

}