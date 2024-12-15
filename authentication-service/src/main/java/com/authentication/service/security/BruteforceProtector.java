package com.authentication.service.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class BruteforceProtector {

    private final int maxAttempts;
    private final long blockTime;
    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blockUntil = new ConcurrentHashMap<>();

    public BruteforceProtector() {
        this.maxAttempts = 3;
        this.blockTime = TimeUnit.MINUTES.toMillis(5);
    }

    public BruteforceProtector(int maxAttempts, long blockTimeInSeconds) {
        this.maxAttempts = maxAttempts;
        this.blockTime = TimeUnit.SECONDS.toMillis(blockTimeInSeconds);
    }

    public void loginFailed(String username) {
        int attemptsCount = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, attemptsCount);

        if (attemptsCount >= maxAttempts) {
            blockUntil.put(username, System.currentTimeMillis() + blockTime);
        }
    }

    public boolean isBlocked(String username) {
        Long blockTime = blockUntil.get(username);
        if (blockTime == null) {
            return false;
        }

        if (System.currentTimeMillis() <= blockTime) {
            return true;
        }

        resetBlock(username);

        return false;
    }

    public void loginSucceeded(String username) {
        resetBlock(username);
    }

    private void resetBlock(String username) {
        attempts.remove(username);
        blockUntil.remove(username);
    }

}

