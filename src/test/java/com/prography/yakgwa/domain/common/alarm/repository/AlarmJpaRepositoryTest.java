package com.prography.yakgwa.domain.common.alarm.repository;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class AlarmJpaRepositoryTest {

    @Autowired
    AlarmJpaRepository alarmJpaRepository;
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 알람타입과모임을통해조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Alarm alarm = Alarm.builder().send(false).alarmType(AlarmType.END_VOTE).meet(saveMeet).build();
        Alarm alarm1 = Alarm.builder().send(false).alarmType(AlarmType.PROMISE_DAY).meet(saveMeet).build();
        alarmJpaRepository.save(alarm);
        alarmJpaRepository.save(alarm1);
        // when
        System.out.println("=====Logic Start=====");

        Optional<Alarm> findAlarm = alarmJpaRepository.findByMeetIdAndAlarmType(saveMeet.getId(), AlarmType.END_VOTE);

        System.out.println("=====Logic End=====");
        // then
        assertThat(findAlarm).isNotEmpty();
    }

    @Test
    void 없는알람타입과모임을통해조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Alarm alarm = Alarm.builder().send(false).alarmType(AlarmType.END_VOTE).meet(saveMeet).build();
        alarmJpaRepository.save(alarm);
        // when
        System.out.println("=====Logic Start=====");

        Optional<Alarm> findAlarm = alarmJpaRepository.findByMeetIdAndAlarmType(saveMeet.getId(), AlarmType.PROMISE_DAY);

        System.out.println("=====Logic End=====");
        // then
        assertThat(findAlarm).isEmpty();
    }
}