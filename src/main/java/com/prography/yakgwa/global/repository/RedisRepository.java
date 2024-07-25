package com.prography.yakgwa.global.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private String GOOD_PLACE_KEYWORD = "USER_GOOD_PLACE:";

    public boolean isUserGoodPlace(Long userId, String title, String mapx, String mapy) {
        String key = GOOD_PLACE_KEYWORD + userId;
        String value = title + "_" + mapx + "_" + mapy;

        // 이미 좋아요를 눌렀는지 확인
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value))) {
            return true; // 이미 좋아요를 누른 경우 true 반환
        }
        return false; // 좋아요를 처음 누르는 경우 false 반환
    }


    public void refreshSave(String authId, String refreshToken, Duration duration) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authId, refreshToken, duration);
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
