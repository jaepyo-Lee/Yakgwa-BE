package com.prography.yakgwa.global.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final RedisTemplate<String,Object> redisTemplate;

    public void refreshSave(String authId, String refreshToken,Duration duration) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authId, refreshToken,duration);
    }

    public String getRefreshToken(String authId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return "Bearer " + valueOperations.get(authId);
    }

    public void removeRefreshToken(String authId) {
        redisTemplate.delete(authId);
    }

    public String getValue(String token) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = (String) valueOperations.get(token);
        return value;
    }

    public void logoutTokenSave(String accessToken, Duration leftTime) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, "logout", leftTime);
    }
}
