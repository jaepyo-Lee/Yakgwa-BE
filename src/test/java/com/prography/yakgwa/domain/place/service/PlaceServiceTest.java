package com.prography.yakgwa.domain.place.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.PlaceLike;
import com.prography.yakgwa.domain.place.entity.dto.PlaceRedisDto;
import com.prography.yakgwa.domain.place.repository.PlaceLikeJpaRepository;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.common.redis.RedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.Redis;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceServiceTest {

    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    PlaceService placeService;
    @Autowired
    RedisRepository redisRepository;

    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    RepositoryDeleter deleter;
    String key;
    @Autowired
    private PlaceRedisRepository placeRedisRepository;
    @MockBean
    PlaceLikeJpaRepository placeLikeJpaRepository;
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 장소에좋아요기능() throws JsonProcessingException {
        // given
        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        User saveUser = dummyCreater.createAndSaveUser(100);
        String title = "title";
        String mapx = "mapx";
        String mapy = "mapy";
        placeJpaRepository.save(Place.builder().title(title).mapx(mapx).mapy(mapy).build());
        LikePlaceRequest request = LikePlaceRequest.builder()
                .title(title).mapx(mapx).mapy(mapy)
                .build();
        key = GOOD_PLACE_KEYWORD + saveUser.getId();
        // when
        System.out.println("=====Logic Start=====");

        placeService.decideLike(saveUser.getId(), true, request);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceRedisDto> likePlaceInfos = placeRedisRepository.findLikePlaceInfos(saveUser.getId());
        assertThat(likePlaceInfos.size()).isOne();
    }

    @Test
    void 장소에좋아요_취소() throws JsonProcessingException {
        // given
        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        User saveUser = dummyCreater.createAndSaveUser(100);
        String title = "title";
        String mapx = "mapx";
        String mapy = "mapy";
        placeJpaRepository.save(Place.builder().title(title).mapx(mapx).mapy(mapy).build());
        LikePlaceRequest request = LikePlaceRequest.builder()
                .title("title").mapx("mapx").mapy("mapy")
                .build();
        key = GOOD_PLACE_KEYWORD + saveUser.getId();

        placeService.decideLike(saveUser.getId(), true, request);

        // when
        System.out.println("=====Logic Start=====");

        placeService.decideLike(saveUser.getId(), false, request);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceRedisDto> likePlaces = placeRedisRepository.findLikePlaceInfos(saveUser.getId());
        assertThat(likePlaces.size()).isEqualTo(0);
    }

    @Test
    void 좋아요한장소전체_조회() throws JsonProcessingException {
        // given

        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        User saveUser = dummyCreater.createAndSaveUser(100);
        LikePlaceRequest request1 = LikePlaceRequest.builder()
                .title("title1").mapx("mapx1").mapy("mapy1")
                .build();
        LikePlaceRequest request2 = LikePlaceRequest.builder()
                .title("title2").mapx("mapx2").mapy("mapy2")
                .build();
        key = GOOD_PLACE_KEYWORD + saveUser.getId();
        placeJpaRepository.save(Place.builder().title("title1").mapx("mapx1").mapy("mapy1").build());
        placeJpaRepository.save(Place.builder().title("title2").mapx("mapx2").mapy("mapy2").build());

        placeService.decideLike(saveUser.getId(), true, request1);
        placeService.decideLike(saveUser.getId(), true, request2);


        // when
        System.out.println("=====Logic Start=====");

        List<PlaceInfoWithUserLike> like = placeService.findLike(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(like.size()).isEqualTo(2);
    }

    @Test
    void 캐싱된좋아요장소없을때캐싱작동여부확인() throws JsonProcessingException {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        redisRepository.removeAllFrom("GOOD_PLACE_USER:"+saveUser.getId());
        PlaceLike mockPlaceLike = Mockito.mock(PlaceLike.class);
        String title = "title";
        String mapx = "mapx";
        String mapy = "mapy";


        when(placeLikeJpaRepository.findAllByUserId(saveUser.getId())).thenReturn(List.of(mockPlaceLike));
        when(mockPlaceLike.getPlace()).thenReturn(Place.builder().title(title).mapx(mapx).mapy(mapy).build());

        String saveTitle = "saveTitle";
        String saveMapx = "saveMapx";
        String saveMapy = "saveMapy";
        placeJpaRepository.save(Place.builder().title(saveTitle).mapx(saveMapx).mapy(saveMapy).build());
        LikePlaceRequest request = LikePlaceRequest.builder()
                .title(saveTitle).mapx(saveMapx).mapy(saveMapy)
                .build();

        // when
        System.out.println("=====Logic Start=====");

        placeService.decideLike(saveUser.getId(), true, request);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceRedisDto> likePlaceInfos = placeRedisRepository.findLikePlaceInfos(saveUser.getId());
        assertThat(likePlaceInfos.size()).isEqualTo(2);

    }
}