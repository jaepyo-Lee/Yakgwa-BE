package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.FirebaseMessageSender;
import com.prography.yakgwa.global.meta.ImplService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class ScheduleExecutor {
    private final TaskScheduler taskScheduler;
    private final FirebaseMessageSender messageSender;

    @Transactional
    public void registerAlarm(String message,LocalDateTime alarmTime) {
        log.info("스케줄링 시작");
        log.info("scheduling thread {}", Thread.currentThread().getName());
        Instant instant = alarmTime.toInstant(ZoneOffset.of("+9"));
        Runnable runnable = () -> {
            try {
                messageSender.sendMessageTo(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        taskScheduler.schedule(runnable, instant);
    }
}
