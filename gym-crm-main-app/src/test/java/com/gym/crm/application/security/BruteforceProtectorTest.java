package com.gym.crm.application.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteforceProtectorTest {

    private final int ATTEMPTS_TO_BLOCK = 3;
    private final long BLOCK_TIME_IN_SECONDS = 10;
    private final String USERNAME = "testUser";

    private BruteforceProtector bruteforceProtector;

    @BeforeEach
    public void setUp() {
        bruteforceProtector = new BruteforceProtector(ATTEMPTS_TO_BLOCK, BLOCK_TIME_IN_SECONDS);
    }

    @Test
    public void shouldBlockAfterThreeFailedAttempts() {
        bruteforceProtector.loginFailed(USERNAME);
        assertFalse(bruteforceProtector.isBlocked(USERNAME));

        bruteforceProtector.loginFailed(USERNAME);
        assertFalse(bruteforceProtector.isBlocked(USERNAME));

        bruteforceProtector.loginFailed(USERNAME);
        assertTrue(bruteforceProtector.isBlocked(USERNAME));
    }

    @Test
    public void shouldReturnFalseWhenNotBlocked() {
        assertFalse(bruteforceProtector.isBlocked(USERNAME));
    }

    @Test
    public void shouldResetAfterBlockExpires() throws InterruptedException {
        bruteforceProtector.loginFailed(USERNAME);
        bruteforceProtector.loginFailed(USERNAME);
        bruteforceProtector.loginFailed(USERNAME);

        assertTrue(bruteforceProtector.isBlocked(USERNAME));

        Thread.sleep(TimeUnit.SECONDS.toMillis(BLOCK_TIME_IN_SECONDS+1));

        assertFalse(bruteforceProtector.isBlocked(USERNAME));
    }

    @Test
    public void shouldResetAttemptsAfterSuccessfulLogin() {
        bruteforceProtector.loginFailed(USERNAME);
        bruteforceProtector.loginFailed(USERNAME);

        assertFalse(bruteforceProtector.isBlocked(USERNAME));

        bruteforceProtector.loginSucceeded(USERNAME);

        assertFalse(bruteforceProtector.isBlocked(USERNAME));
    }

}