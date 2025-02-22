package com.prography.yakgwa.domain.place.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.place.NotFoundPlaceException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.meta.UserPlaceLikeCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRedisRepository redisRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date)
     */
    /**
     * redis로 다 받고, 12시마다 Batch로 저장시킴
     * redis가 필요할때마다(key)가 없을경우, 조회해서 캐싱올림
     */

    @UserPlaceLikeCache
    public void decideLike(Long userId, boolean like, LikePlaceRequest likePlaceRequest) throws JsonProcessingException {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        Place place = placeJpaRepository.findByTitleAndMapxAndMapy(
                likePlaceRequest.getTitle(),
                likePlaceRequest.getMapx(),
                likePlaceRequest.getMapy()
        ).orElseThrow(NotFoundPlaceException::new);

        boolean isPlaceLiked = redisRepository.isUserGoodPlace(user.getId(), place);
        if (like) {
            if (!isPlaceLiked) {
                redisRepository.likePlace(user.getId(),place);
            }
        } else {
            if (isPlaceLiked) {
                redisRepository.cancelLikePlace(userId, place);
            }
        }
    }


    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date)
     */
    @UserPlaceLikeCache
    public List<PlaceInfoWithUserLike> findLike(Long userId) {
        log.info("나의 즐겨찾기 조회시작");
        List<Place> likePlaceInfos = redisRepository.findLikePlaceInfos(userId);
        return likePlaceInfos.stream()
                .map(place -> PlaceInfoWithUserLike.builder()
                        .isUserLike(true)
                        .placeInfoDto(place.toInfoDto())
                        .build())
                .filter(Objects::nonNull)
                .toList();
    }

}
