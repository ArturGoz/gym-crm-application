package com.gca.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@NoArgsConstructor
public class LoginAttemptService {

    @Value("${login.attempt.max}")
    private int MAX_ATTEMPT = 3;

    @Value("${login.attempt.block-time-ms}")
    private long BLOCK_TIME_MS = 60 * 1000; // 1 minute

    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attemptsCache.remove(username.toLowerCase());
    }

    public void loginFailed(String username) {
        Attempt attempt = attemptsCache.getOrDefault(username, new Attempt(0, System.currentTimeMillis()));
        attempt.count++;
        attempt.lastAttemptTime = System.currentTimeMillis();

        attemptsCache.put(username, attempt);
    }

    public boolean isBlocked(String username) {
        Attempt attempt = attemptsCache.get(username);

        return attempt != null && attempt.count >= MAX_ATTEMPT && !isExpired(attempt);
    }

    private boolean isExpired(Attempt attempt) {
        return (System.currentTimeMillis() - attempt.lastAttemptTime) >= BLOCK_TIME_MS;
    }

    @AllArgsConstructor
    private static class Attempt {
        int count;
        long lastAttemptTime;
    }
}
