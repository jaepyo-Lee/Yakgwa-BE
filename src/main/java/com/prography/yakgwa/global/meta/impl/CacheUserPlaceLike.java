package com.prography.yakgwa.global.meta.impl;

import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.PlaceLike;
import com.prography.yakgwa.domain.place.entity.dto.PlaceRedisDto;
import com.prography.yakgwa.domain.place.repository.PlaceLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheUserPlaceLike {
    private final PlaceRedisRepository redisRepository;
    private final PlaceLikeJpaRepository placeLikeJpaRepository;

    @Around("@annotation(com.prography.yakgwa.global.meta.UserPlaceLikeCache)")
    public Object cache(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long userId = (Long) args[0];

        boolean isUserGoodPlaceCache = redisRepository.isUserGoodPlaceCache(userId);
        if (!isUserGoodPlaceCache) {
            List<PlaceLike> placeLikes = placeLikeJpaRepository.findAllByUserId(userId);
            for (PlaceLike placeLike : placeLikes) {
                PlaceRedisDto redisDto = placeLike.getPlace().toRedisDto();
                redisRepository.likePlace(userId, redisDto);
            }
        }
        return joinPoint.proceed();
    }
}
