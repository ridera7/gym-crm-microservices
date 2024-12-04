package com.gym.crm.application.service.impl.serviceutils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PasswordHandlerTest {

    @InjectMocks
    private PasswordHandler passwordHandler;

    @Test
    @DisplayName("Should generate hashed random password with right format")
    public void shouldGenerateHashedRandomPasswordWithRightFormat() {
        String hashedPassword = passwordHandler.generateRandomHashedPassword();

        assertNotNull(hashedPassword, "Password should not be null");
        assertEquals(97, hashedPassword.length(), "Password length should match the configured value");
        assertTrue(hashedPassword.contains(":"), "Hashed password should contain salt and hash separated by a colon");

        String[] parts = hashedPassword.split(":");
        assertEquals(32, parts[0].length());
    }

    @Test
    @DisplayName("Should verify password success")
    public void shouldVerifyPasswordCheckSuccess() {
        String password = "myPassword123";
        String hashedPassword = passwordHandler.hashPassword(password);

        boolean mustBeTrue = passwordHandler.isPasswordMatch(password, hashedPassword);

        assertTrue(mustBeTrue, "Password verification should pass for correct password");
    }

    @Test
    @DisplayName("Should verify password check failure")
    public void shouldVerifyPasswordCheckFailure() {
        String password = "myPassword123";
        String wrongPassword = "wrongPassword";
        String hashedPassword = passwordHandler.hashPassword(password);

        boolean mustBeFalse = passwordHandler.isPasswordMatch(wrongPassword, hashedPassword);

        assertFalse(mustBeFalse, "Password verification should fail for incorrect password");
    }

}