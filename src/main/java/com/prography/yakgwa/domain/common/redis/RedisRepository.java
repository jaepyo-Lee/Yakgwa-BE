package com.prography.yakgwa.domain.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    public static final String GOOD_PLACE_USER = "GOOD_PLACE_USER:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;




    public void removeAllFrom(String key) {
        redisTemplate.delete(key);
    }

    public void refreshSave(String refreshRegisterIdId, String refreshToken, Duration duration) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshRegisterIdId, refreshToken, duration);
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
