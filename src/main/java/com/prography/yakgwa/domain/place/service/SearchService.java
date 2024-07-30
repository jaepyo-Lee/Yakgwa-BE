package com.prography.yakgwa.domain.place.service;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.client.map.NaverClient;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final RedisRepository redisRepository;
    private final NaverClient naverClient;
    private final PlaceWriter placeWriter;
    private final UserJpaRepository userJpaRepository;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-25
     * Finish-Date)
     */
    public List<PlaceInfoWithUserLike> search(String search, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        NaverMapResponseDto naverResponse = naverClient.searchNaverAPIClient(search);
        List<PlaceInfoDto> items = naverResponse.getItems();
        List<PlaceInfoWithUserLike> placeInfoWithUserLikes = new ArrayList<>();
        for (PlaceInfoDto item : items) {
            placeWriter.writeNotExist(item.toEntity());
            placeInfoWithUserLikes.add(createPlaceInfoWithUserLike(item, user));
        }
        return placeInfoWithUserLikes;
    }

    private PlaceInfoWithUserLike createPlaceInfoWithUserLike(PlaceInfoDto item, User user) {
        boolean userGoodPlace = redisRepository.isUserGoodPlace(user.getId(), item.getTitle(), item.getMapx(), item.getMapy());
        return PlaceInfoWithUserLike.builder()
                .placeInfoDto(item)
                .isUserLike(userGoodPlace)
                .build();
    }


}
