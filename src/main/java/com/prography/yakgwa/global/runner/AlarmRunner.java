package com.prography.yakgwa.global.runner;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmRunner implements ApplicationRunner {
    private final AlarmJpaRepository alarmJpaRepository;
    private final TaskScheduleManager alarmScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AlarmRunner: 알람조회");
        List<Alarm> all = alarmJpaRepository.findAll();
        log.info("AlarmRunner: 알람등록진행");
        all.stream()
                .filter(alarm -> !alarm.isSend())
                .forEach(alarm -> alarmScheduler.regist(alarm.getMeet(), alarm.getAlarmType()));
        log.info("AlarmRunner: 알람등록완료");
    }
}
