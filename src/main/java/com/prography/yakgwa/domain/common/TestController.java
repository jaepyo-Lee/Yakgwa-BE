package com.prography.yakgwa.domain.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.alarm.AlarmProcessor;
import com.prography.yakgwa.domain.common.schedule.ScheduleExecutor;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final ScheduleExecutor scheduleExecutor;
    private final AlarmProcessor alarmProcessor;
    private final MeetReader meetReader;

    @PostMapping("/test/schedule")
    public void testSchedule(@RequestParam("meetId") Long meetId) throws JsonProcessingException {
        LocalDateTime time = LocalDateTime.now().plusSeconds(10L);
        System.out.println("시작시간:"+time);
        Meet meet = meetReader.read(meetId);
        alarmProcessor.process(AlarmType.VOTE_CONFIRM,meet,time);
    }
/*
    @PostMapping("/test/schedule/cancel")
    public void testCancel(@RequestParam("taskId") String taskId) {
        System.out.println("취소시간"+LocalDateTime.now());
        scheduleExecutor.cancelAlarm(taskId);
    }

    @PostMapping("test/schedule/change")
    public void testChange(@RequestParam("taskId") String taskId) {
        LocalDateTime time = LocalDateTime.now().plusSeconds(10L);
        System.out.println("변경시간"+time);
        scheduleExecutor.changeAlarm(taskId,time);
    }*/
}
