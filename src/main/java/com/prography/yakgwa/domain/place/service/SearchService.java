package com.prography.yakgwa.domain.place.service;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.global.client.map.NaverClient;
import com.prography.yakgwa.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final RedisRepository redisRepository;
    private final UserReader userReader;
    private final NaverClient naverClient;
    private final PlaceWriter placeWriter;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-25
     * Finish-Date)
     */
    public List<PlaceInfoWithUserLike> search(String search, Long userId) {
        User user = userReader.read(userId);
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
