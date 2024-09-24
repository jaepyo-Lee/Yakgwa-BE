package com.prography.yakgwa.domain.common.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.PlaceLike;
import com.prography.yakgwa.domain.place.repository.PlaceLikeJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.config.ScheduleConfig;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BatchSchedulerTest {
    @Autowired
    private PlaceRedisRepository placeRedisRepository;
    @Autowired
    private DummyCreater dummyCreater;
    @Autowired
    private PlaceLikeJpaRepository placeLikeJpaRepository;
    @Autowired
    private RepositoryDeleter deleter;
    @Autowired
    private BatchScheduler batchScheduler;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 특정시간에레디스의데이터가DB로저장되고삭제되는로직_테스트() throws JsonProcessingException {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        placeRedisRepository.likePlace(saveUser.getId(), savePlace);

        // 스케줄링 적용해서 테스트하고싶은데, 테스트코드에서 스케줄링이 적용안됌 추후 해결해서 다시 코드 짜기
        batchScheduler.saveCacheToDB();

        // when
        System.out.println("=====Logic Start=====");
        List<Place> afterPlaceInfosOfUser = placeRedisRepository.findLikePlaceInfos(saveUser.getId());
        List<PlaceLike> afterSchedulerPlaceLikes = placeLikeJpaRepository.findAllByUserId(saveUser.getId());
        assertAll(()->assertThat(afterSchedulerPlaceLikes.size()).isOne(),
                ()-> assertThat(afterPlaceInfosOfUser.size()).isZero());
        /*Awaitility.await()
                .atMost(10, TimeUnit.SECONDS) // 대기 시간을 10초로 늘림
                .untilAsserted(() -> {

                });*/
        System.out.println("=====Logic End=====");
        // then
    }
}