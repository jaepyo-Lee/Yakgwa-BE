package com.prography.yakgwa.domain.place.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.client.map.NaverClient;
import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final PlaceRedisRepository redisRepository;
    private final NaverClient naverClient;
    private final PlaceWriter placeWriter;
    private final UserJpaRepository userJpaRepository;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-25
     * Finish-Date)
     */
    /**
     * redis로 다 받고, 12시마다 Batch로 저장시킴
     * redis가 필요할때마다(key)가 없을경우, 조회해서 캐싱올림
     * 
    */
    public List<PlaceInfoWithUserLike> search(String search, Long userId) throws Exception {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        NaverMapResponseDto naverResponse = naverClient.searchNaverAPIClient(search);
        List<PlaceInfoDto> items = naverResponse.getItems();
        List<PlaceInfoWithUserLike> placeInfoWithUserLikes = new ArrayList<>();
        for (PlaceInfoDto item : items) {
            placeWriter.writeIfNotExist(item.toEntity());
            placeInfoWithUserLikes.add(createPlaceInfoWithUserLike(item.toEntity(), user));
        }
        return placeInfoWithUserLikes;
    }

    private PlaceInfoWithUserLike createPlaceInfoWithUserLike(Place place, User user) throws JsonProcessingException {
        boolean userGoodPlace1 = redisRepository.isUserGoodPlace(user.getId(),place.toRedisDto());
        return PlaceInfoWithUserLike.builder()
                .placeInfoDto(place.toInfoDto())
                .isUserLike(userGoodPlace1)
                .build();
    }


}
