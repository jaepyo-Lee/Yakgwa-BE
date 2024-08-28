package com.prography.yakgwa.domain.place.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
    @AfterEach
    void init(){
        redisRepository.removeAllFrom(key);
        deleter.deleteAll();
    }
    @Test
    void 장소에좋아요기능() {
        // given
        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        User saveUser = dummyCreater.createAndSaveUser(100);
        LikePlaceRequest request = LikePlaceRequest.builder()
                .title("title").mapx("mapx").mapy("mapy")
                .build();
        key = GOOD_PLACE_KEYWORD + saveUser.getId();
        // when
        System.out.println("=====Logic Start=====");

        placeService.decideLike(saveUser.getId(), true, request);

        System.out.println("=====Logic End=====");
        // then
        Set<Object> likePlaces = redisRepository.findLikePlaces(saveUser.getId());
        assertThat(likePlaces.size()).isEqualTo(1);
    }

    @Test
    void 장소에좋아요_취소() {
        // given
        String GOOD_PLACE_KEYWORD = "GOOD_PLACE_USER:";
        User saveUser = dummyCreater.createAndSaveUser(100);
        LikePlaceRequest request = LikePlaceRequest.builder()
                .title("title").mapx("mapx").mapy("mapy")
                .build();
        key = GOOD_PLACE_KEYWORD + saveUser.getId();

        placeService.decideLike(saveUser.getId(), true, request);

        // when
        System.out.println("=====Logic Start=====");

        placeService.decideLike(saveUser.getId(),false,request);

        System.out.println("=====Logic End=====");
        // then
        Set<Object> likePlaces = redisRepository.findLikePlaces(saveUser.getId());
        assertThat(likePlaces.size()).isEqualTo(0);
    }

    @Test
    void 좋아요한장소전체_조회() {
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
}