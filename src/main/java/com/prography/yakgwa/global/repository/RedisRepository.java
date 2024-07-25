package com.prography.yakgwa.global.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void likePlace(Long userId, String title, String mapx, String mapy){
        String value = likePlaceValueCreate(title, mapx, mapy);
        String key = likePlaceKeyCreate(userId);
        redisTemplate.opsForSet().add(key, value);
    }
    public void cancelLikePlace(Long userId, String title, String mapx, String mapy){
        String value = likePlaceValueCreate(title, mapx, mapy);
        String key = likePlaceKeyCreate(userId);
        redisTemplate.opsForSet().remove(key, value);
    }
    public Set<Object> findLikePlaces(Long userId){
        return redisTemplate.opsForSet().members(likePlaceKeyCreate(userId));

    }

    private String likePlaceValueCreate(String title, String mapx, String mapy) {
        return title + "_" + mapx + "_" + mapy;
    }

    public boolean isUserGoodPlace(Long userId, String title, String mapx, String mapy) {
        String key = likePlaceKeyCreate(userId);
        String value = likePlaceValueCreate(title, mapx, mapy);

        // 이미 좋아요를 눌렀는지 확인
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value))) {
            return true; // 이미 좋아요를 누른 경우 true 반환
        }
        return false; // 좋아요를 처음 누르는 경우 false 반환
    }
    private String likePlaceKeyCreate(Long userId) {
        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        return GOOD_PLACE_KEYWORD + userId;
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
