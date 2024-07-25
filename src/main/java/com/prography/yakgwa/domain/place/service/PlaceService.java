package com.prography.yakgwa.domain.place.service;

import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final RedisRepository redisRepository;
    private final UserReader userReader;

    public void decideLike(Long userId, boolean like, LikePlaceRequest likePlaceRequest) {
        User user = userReader.read(userId);
        if (!like) {
            redisRepository.cancelLikePlace(userId, likePlaceRequest.getTitle(), likePlaceRequest.getMapx(), likePlaceRequest.getMapy());
            return;
        }
        redisRepository.likePlace(userId, likePlaceRequest.getTitle(), likePlaceRequest.getMapx(), likePlaceRequest.getMapy());
    }
}
