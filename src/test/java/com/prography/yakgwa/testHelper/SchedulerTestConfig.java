package com.prography.yakgwa.testHelper;

import com.prography.yakgwa.domain.common.redis.PlaceRedisRepository;
import com.prography.yakgwa.domain.common.schedule.BatchScheduler;
import com.prography.yakgwa.domain.place.repository.PlaceLikeJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SchedulerTestConfig {
    @Bean
    public BatchScheduler batchScheduler(PlaceRedisRepository placeRedisRepository,
                                         PlaceLikeJpaRepository placeLikeJpaRepository,
                                         UserJpaRepository userJpaRepository) {
        return new BatchScheduler(placeRedisRepository, placeLikeJpaRepository, userJpaRepository);
    }
}
