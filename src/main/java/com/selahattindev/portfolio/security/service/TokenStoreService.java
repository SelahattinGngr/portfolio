package com.selahattindev.portfolio.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenStoreService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "refresh_token:";

    private String buildKey(String username, String deviceId) {
        return TOKEN_PREFIX + username + ":" + deviceId;
    }

    public boolean tokenExists(String username, String deviceId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(username, deviceId)));
    }

    public void storeToken(String username, String deviceId, String token, long expirationMillis) {
        redisTemplate.opsForValue().set(buildKey(username, deviceId), token, Duration.ofMillis(expirationMillis));
    }

    public String getToken(String username, String deviceId) {
        return (String) redisTemplate.opsForValue().get(buildKey(username, deviceId));
    }

    public void deleteToken(String username, String deviceId) {
        redisTemplate.delete(buildKey(username, deviceId));
    }

    public void deleteAllTokensForUser(String username) {
        Set<String> keys = redisTemplate.keys(TOKEN_PREFIX + username + ":*");
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(redisTemplate::delete);
        }
    }
}
