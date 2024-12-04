package com.gym.crm.application.service.impl.serviceutils;

import com.gym.crm.application.exception.AuthenticationException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class PasswordHandler {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int ITERATIONS = 3;
    private static final int MEMORY = 1 << 13;
    private static final int PARALLELISM = 1;

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    @Value("${security.password.length}")
    private int passwordLength = 10;

    public String hashPassword(String plainPassword) {
        byte[] salt = generateSalt();

        return hashPassword(plainPassword, salt);
    }

    public boolean isPasswordMatch(String plainPassword, String storedPasswordHash) {
        String[] parts = storedPasswordHash.split(":");
        if (parts.length != 2) {
            throw new AuthenticationException("Invalid password format");
        }
        byte[] salt = hexToBytes(parts[0]);

        String computedHash = hashPassword(plainPassword, salt);

        return storedPasswordHash.equals(computedHash);
    }

    public String generateRandomHashedPassword() {
        String password = generateRandomPassword();
        byte[] salt = generateSalt();

        return hashPassword(password, salt);
    }

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    private String hashPassword(String plainPassword, byte[] salt) {
        byte[] passwordBytes = plainPassword.getBytes();
        byte[] hash = new byte[HASH_LENGTH];

        Argon2Parameters.Builder paramsBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(PARALLELISM)
                .withMemoryAsKB(MEMORY)
                .withIterations(ITERATIONS);

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(paramsBuilder.build());
        generator.generateBytes(passwordBytes, hash);

        return bytesToHex(salt) + ":" + bytesToHex(hash);
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();

        random.nextBytes(salt);

        return salt;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }

        return data;
    }

}
