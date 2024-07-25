package com.prography.yakgwa.domain.place.service;

import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.global.format.exception.place.NotFoundPlaceException;
import com.prography.yakgwa.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final RedisRepository redisRepository;
    private final UserReader userReader;
    private final PlaceReader placeReader;

    public void decideLike(Long userId, boolean like, LikePlaceRequest likePlaceRequest) {
        User user = userReader.read(userId);
        if (!like) {
            redisRepository.cancelLikePlace(userId, likePlaceRequest.getTitle(), likePlaceRequest.getMapx(), likePlaceRequest.getMapy());
            return;
        }
        redisRepository.likePlace(userId, likePlaceRequest.getTitle(), likePlaceRequest.getMapx(), likePlaceRequest.getMapy());
    }

    public List<PlaceInfoWithUserLike> findLike(Long userId) {
        Set<Object> likePlaces = redisRepository.findLikePlaces(userId);

        return likePlaces.stream()
                .map(this::convertToPlaceInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PlaceInfoWithUserLike convertToPlaceInfo(Object likePlace) {
        String[] placeData = parsePlace(likePlace);
        if (placeData == null) return null;

        String title = placeData[0];
        String mapx = placeData[1];
        String mapy = placeData[2];

        Place place = placeReader.readByMapxAndMapyAndTitle(mapx, mapy, title)
                .orElseThrow(NotFoundPlaceException::new);

        return PlaceInfoWithUserLike.builder()
                .isUserLike(true)
                .placeInfoDto(place.toInfoDto())
                .build();
    }

    private String[] parsePlace(Object likePlace) {
        String value = String.valueOf(likePlace);
        String[] split = value.split("_");

        if (split.length != 3) {
            log.warn("Invalid place data format: {}", value);
            return null;
        }

        return split;
    }
}
