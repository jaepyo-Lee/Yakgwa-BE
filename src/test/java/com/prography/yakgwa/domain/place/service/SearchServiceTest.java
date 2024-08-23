package com.prography.yakgwa.domain.place.service;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.common.client.map.NaverClient;
import com.prography.yakgwa.domain.common.redis.RedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    NaverClient naverClient;
    @Mock
    RedisRepository redisRepository;
    @Mock
    UserJpaRepository userJpaRepository;
    @InjectMocks
    SearchService service;
    @Mock
    PlaceWriter placeWriter;

    @Test
    void 사용자가검색한장소가좋아요가눌렸는지_조회() {
        // given
        PlaceInfoDto placeInfoDtoDummy = createPlaceInfoDtoDummy(1);
        PlaceInfoDto placeInfoDtoDummy1 = createPlaceInfoDtoDummy(2);
        PlaceInfoDto placeInfoDtoDummy2 = createPlaceInfoDtoDummy(3);
        NaverMapResponseDto build = NaverMapResponseDto.builder()
                .items(List.of(placeInfoDtoDummy1, placeInfoDtoDummy2, placeInfoDtoDummy))
                .total(3)
                .start(1)
                .lastBuildDate(LocalDateTime.now().toString())
                .display(3)
                .build();

        User user = new User();
        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(naverClient.searchNaverAPIClient(anyString())).thenReturn(build);
        when(redisRepository.isUserGoodPlace(any(), any(), any(), any())).thenReturn(true);
        doNothing().when(placeWriter).writeIfNotExist(any());

        // when
        String searchString = "testSearch";
        Long userId = 1L;
        System.out.println("=====Logic Start=====");

        List<PlaceInfoWithUserLike> search = service.search(searchString, userId);

        System.out.println("=====Logic End=====");

        // then
        assertThat(search.size()).isEqualTo(3);
        assertThat(search.stream()
                .filter(placeInfoWithUserLike -> placeInfoWithUserLike.getIsUserLike().equals(Boolean.TRUE))
                .toList().size()).isEqualTo(3);

        // Verify that mocks were called as expected
        verify(userJpaRepository).findById(userId);
        verify(naverClient).searchNaverAPIClient(searchString);
        verify(redisRepository, times(3)).isUserGoodPlace(any(), any(), any(), any());
    }

    private PlaceInfoDto createPlaceInfoDtoDummy(int i) {
        return PlaceInfoDto.builder()
                .title("title"+i)
                .link("link"+i)
                .mapy("mapy"+i)
                .mapx("mapx"+i)
                .description("description"+i)
                .address("address"+i)
                .roadAddress("roadAddress"+i)
                .telephone("telephone"+i)
                .category("category"+i)
                .build();
    }
}