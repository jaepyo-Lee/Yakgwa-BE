package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ScheduleJpaRepositoryTest extends IntegrationTestSupport {
    @AfterEach
    void init(){
        deleter.deleteAll();
    }
    @Test
    void 모임Id와AlarmType을통해저장되어있는Schedule조회하기() {
        // given
        long meetId = 1L;
        Schedule schedule1 = new Schedule(meetId, AlarmType.END_VOTE);
        Schedule schedule2 = new Schedule(meetId, AlarmType.PROMISE_DAY);
        scheduleJpaRepository.save(schedule1);

        // when
        System.out.println("=====Logic Start=====");

        Optional<Schedule> maybe1 = scheduleJpaRepository.findByMeetIdAndAlarmType(meetId, AlarmType.END_VOTE);
        Optional<Schedule> maybe2 = scheduleJpaRepository.findByMeetIdAndAlarmType(meetId, AlarmType.PROMISE_DAY);
        System.out.println("=====Logic End=====");
        // then
        assertAll(()->assertThat(maybe1).isPresent(),
                ()-> assertThat(maybe2).isEmpty());
    }
}