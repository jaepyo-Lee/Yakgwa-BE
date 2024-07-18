package com.prography.yakgwa.domain.common.alarm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.schedule.ScheduleExecutor;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ImplService
public class AlarmProcessor {
    private final ScheduleExecutor scheduleExecutor;

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-18
     * Finish-Date)
     */
    public void process(AlarmType alarmType,Meet meet, LocalDateTime time) throws JsonProcessingException {
        String title = meet.getTitle() + alarmType.getTitle();
        String body = meet.getTitle() + alarmType.getBody();
        String taskID = String.format("%s-%d", alarmType, meet.getId());
        scheduleExecutor.registerAlarm(taskID, meet, title, body, time);
    }

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-18
     * Finish-Date)
     */
    public void change(AlarmType alarmType, Meet meet, LocalDateTime newTime){
        scheduleExecutor.changeAlarm(alarmType, meet, newTime);
    }
}
