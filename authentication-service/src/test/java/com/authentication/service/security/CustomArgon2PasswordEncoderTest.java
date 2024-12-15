package com.authentication.service.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomArgon2PasswordEncoderTest {

    @Mock
    private PasswordHandler passwordHandler;

    @InjectMocks
    private CustomArgon2PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnHashedPassword() {
        String rawPassword = "password123";
        String hashedPassword = "$argon2hashedPassword";
        when(passwordHandler.hashPassword(rawPassword)).thenReturn(hashedPassword);

        String actual = passwordEncoder.encode(rawPassword);

        assertEquals(hashedPassword, actual);
        verify(passwordHandler).hashPassword(rawPassword);
    }

    @Test
    void shouldReturnTrueWhenPasswordMatches() {
        String rawPassword = "password123";
        String storedPassword = "$argon2hashedPassword";
        when(passwordHandler.isPasswordMatch(rawPassword, storedPassword)).thenReturn(true);

        boolean actual = passwordEncoder.matches(rawPassword, storedPassword);

        assertTrue(actual);
        verify(passwordHandler).isPasswordMatch(rawPassword, storedPassword);
    }

    @Test
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "password123";
        String storedPassword = "$argon2hashedPassword";
        when(passwordHandler.isPasswordMatch(rawPassword, storedPassword)).thenReturn(false);

        boolean actual = passwordEncoder.matches(rawPassword, storedPassword);

        assertFalse(actual);
        verify(passwordHandler).isPasswordMatch(rawPassword, storedPassword);
    }

}