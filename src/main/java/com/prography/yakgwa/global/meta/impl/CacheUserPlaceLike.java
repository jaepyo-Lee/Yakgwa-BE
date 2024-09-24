package com.prography.yakgwa.global.meta.impl;

import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.PlaceLike;
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

    /**
     * Todo
     * Work) Aop가 파라미터의 위치에 의존하고있음. 파라미터의 위치가 0번째가 아니라면 정상적으로 동작하지 않음. 해당 문제 해결해야함
     * Write-Date)
     * Finish-Date)
     */
    @Around("@annotation(com.prography.yakgwa.global.meta.UserPlaceLikeCache)")
    public Object cache(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long userId = (Long) args[0];

        boolean isUserGoodPlaceCache = redisRepository.isUserGoodPlaceCache(userId);
        if (!isUserGoodPlaceCache) {
            List<PlaceLike> placeLikes = placeLikeJpaRepository.findAllByUserId(userId);
            List<Place> places = placeLikes.stream().map(PlaceLike::getPlace).toList();
            for (Place place : places) {
                redisRepository.likePlace(userId, place.toRedisDto());
            }
        }
        return joinPoint.proceed();
    }
}
