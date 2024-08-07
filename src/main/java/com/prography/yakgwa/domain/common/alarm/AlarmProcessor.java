package com.prography.yakgwa.domain.common.alarm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.schedule.AlarmScheduler;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ImplService
public class AlarmProcessor {
    private final AlarmScheduler alarmScheduler;
    private final TimeSlotJpaRepository timeSlotJpaRepository;

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-18
     * Finish-Date)
     */
    public void process(Meet meet) throws JsonProcessingException {
        alarmScheduler.registerAlarm(meet);
    }

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-18
     * Finish-Date)
     */
    public void change(AlarmType alarmType, Meet meet, LocalDateTime newTime) {
        alarmScheduler.changeAlarm(meet, newTime);
    }
}
