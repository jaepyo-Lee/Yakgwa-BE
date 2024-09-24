package com.prography.yakgwa.domain.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class PlaceRedisRepository {
    public static final String GOOD_PLACE_USER = "GOOD_PLACE_USER:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<Integer> getGoodPlaceUserId() {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(100L).match(GOOD_PLACE_USER + "*").build();
        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        return cursor.stream()
                .map(s -> Integer.parseInt(s.substring(GOOD_PLACE_USER.length())))
                .toList();
    }
    public void deletePlaceLikeByUserId(Long userId){
        redisTemplate.delete(likePlaceKeyCreate(userId));
    }
    public boolean isUserGoodPlace(Long userId, Object placeEntity) throws JsonProcessingException {
        String key = likePlaceKeyCreate(userId);
        String value = objectMapper.writeValueAsString(placeEntity);

        // 이미 좋아요를 눌렀는지 확인
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value))) {
            return true; // 이미 좋아요를 누른 경우 true 반환
        }
        return false; // 좋아요를 처음 누르는 경우 false 반환
    }

    public void likePlace(Long userId, Object placeEntity) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(placeEntity);
        String key = likePlaceKeyCreate(userId);
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, Duration.ofDays(8L));
    }

    public void cancelLikePlace(Long userId, Object placeEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(placeEntity);
        String key = likePlaceKeyCreate(userId);
        redisTemplate.opsForSet().remove(key, value);
    }

    public List<Place> findLikePlaceInfos(Long userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Object> userLikeplaces = redisTemplate.opsForSet().members(likePlaceKeyCreate(userId));

        // 스트림을 통해 역직렬화하면서 예외를 처리
        return userLikeplaces.stream().map(o -> {
                    try {
                        // Redis에서 가져온 Object를 문자열로 캐스팅하고 역직렬화
                        String json = o.toString();  // 저장된 데이터가 JSON 형태라면
                        return objectMapper.readValue(json, Place.class);
                    } catch (JsonProcessingException e) {
                        // 예외가 발생할 경우, 적절한 처리를 하거나 로그를 남기고 null 반환
                        e.printStackTrace(); // 필요에 따라 예외 처리
                        return null;
                    }
                }).filter(Objects::nonNull)  // null 값은 제외
                .toList();
    }

    private String likePlaceKeyCreate(Long userId) {
        return GOOD_PLACE_USER + userId;
    }

    public boolean isUserGoodPlaceCache(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(likePlaceKeyCreate(userId)));
    }
}
