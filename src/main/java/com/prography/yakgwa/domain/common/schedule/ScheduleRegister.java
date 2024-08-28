package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class ScheduleRegister implements TriggerScheduler {
    private final TaskScheduler taskScheduler;

    @Override
    public void schedule(Runnable task, LocalDateTime time) {
        Instant instant = time.toInstant(ZoneOffset.of("+9"));
        taskScheduler.schedule(task, instant);
    }
}
