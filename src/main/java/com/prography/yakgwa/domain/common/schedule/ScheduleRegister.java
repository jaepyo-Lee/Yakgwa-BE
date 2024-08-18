package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class ScheduleRegister {
    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
    public static final String MEET_ALARM_FORMAT = "MeetAlarm_%s: %d";

    public void scheduleTask(Meet meet, AlarmType type, Runnable task, LocalDateTime time) {
        String taskID = String.format(MEET_ALARM_FORMAT, type, meet.getId());
        Instant instant = time.toInstant(ZoneOffset.of("+9"));

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, instant);
        scheduleMap.put(taskID, scheduledFuture);
//        taskMap.put(scheduledFuture, task);
    }

    /*public void changeAlarm(Meet meet, LocalDateTime newTime) {
        String taskID = String.format(MEET_ALARM_FORMAT, meet.getId());
        Runnable alarmTask = cancelAlarm(taskID);

        if (alarmTask != null) {
            scheduleTask(meet, type, alarmTask, newTime);
            log.info("알람 시간 변경 완료: {}", meet.getId());
        } else {
            log.warn("알람 시간 변경 실패, 기존 알람을 찾을 수 없음: {}", meet.getId());
        }
    }

    private Runnable cancelAlarm(String taskID) {
        ScheduledFuture<?> scheduledFuture = scheduleMap.remove(taskID);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            return taskMap.remove(scheduledFuture);
        } else {
            log.warn("알람 취소 실패, 존재하지 않는 ID: {}", taskID);
            return null;
        }
    }*/
}
