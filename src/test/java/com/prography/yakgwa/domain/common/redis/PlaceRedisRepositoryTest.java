package com.prography.yakgwa.domain.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class PlaceRedisRepositoryTest extends IntegrationTestSupport {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 장소좋아요누른사용자의아이디반환로직_테스트() throws JsonProcessingException {
        // given
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        Place savePlace3 = dummyCreater.createAndSavePlace(3);
        Place savePlace4 = dummyCreater.createAndSavePlace(4);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        placeRedisRepository.likePlace(saveUser.getId(), savePlace);
        placeRedisRepository.likePlace(saveUser.getId(), savePlace2);
        placeRedisRepository.likePlace(saveUser2.getId(), savePlace3);
        placeRedisRepository.likePlace(saveUser2.getId(), savePlace4);

        // when
        System.out.println("=====Logic Start=====");

        List<Integer> goodPlaceUserIds = placeRedisRepository.getGoodPlaceUserId();

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(goodPlaceUserIds).contains(1),
                () -> assertThat(goodPlaceUserIds).contains(2),
                () -> assertThat(goodPlaceUserIds).hasSize(2));
    }

    @Test
    void 장소좋아요누른사용자의아이디가100개이상일때전체정상반환로직_테스트() throws JsonProcessingException {
        // given
        Place savePlace = dummyCreater.createAndSavePlace(1);
        for (int i = 1; i <= 101; i++) {
            dummyCreater.createAndSaveUser(i);
        }
        for (int i = 1; i <= 101; i++) {
            placeRedisRepository.likePlace((long) i, savePlace);
        }

        // when
        System.out.println("=====Logic Start=====");

        List<Integer> goodPlaceUserIds = placeRedisRepository.getGoodPlaceUserId();

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(goodPlaceUserIds).hasSize(101));
    }
}