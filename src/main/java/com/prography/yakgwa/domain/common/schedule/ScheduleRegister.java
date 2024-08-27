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
public class ScheduleRegister implements TriggerScheduler {
    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
    public static final String MEET_ALARM_FORMAT = "MeetAlarm_%s: %d";


    @Override
    public void schedule(Runnable task, LocalDateTime time) {
        Instant instant = time.toInstant(ZoneOffset.of("+9"));
        taskScheduler.schedule(task, instant);
    }

    @Override
    public void schedule(Meet meet, AlarmType type, Runnable task, LocalDateTime time) {
        String taskID = String.format(MEET_ALARM_FORMAT, type, meet.getId());
        Instant instant = time.toInstant(ZoneOffset.of("+9"));
        taskScheduler.schedule(task, instant);
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, instant);
        scheduleMap.put(taskID, scheduledFuture);
    }
}
