package com.gca.service.helper;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomPasswordGenerator {
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random secureRandom = new Random();

    public String generatePassword() {
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(ALPHA_NUMERIC.charAt(secureRandom.nextInt(ALPHA_NUMERIC.length())));
        }

        return sb.toString();
    }
}
