package com.prography.yakgwa.global.runner;

import com.prography.yakgwa.domain.common.schedule.Schedule;
import com.prography.yakgwa.domain.common.schedule.ScheduleJpaRepository;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmRunner implements ApplicationRunner {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final TaskScheduleManager taskScheduleManager;
    private final MeetJpaRepository meetJpaRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AlarmRunner: 알람등록진행");
        List<Schedule> all = scheduleJpaRepository.findAll();
        for (Schedule schedule : all) {
            Optional<Meet> maybeMeet = meetJpaRepository.findById(schedule.getMeetId());
            maybeMeet.ifPresent(meet -> {
                taskScheduleManager.regist(meet, schedule.getAlarmType());
            });
        }
        log.info("AlarmRunner: 알람등록완료");
    }
}
