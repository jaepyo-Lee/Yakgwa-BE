package com.prography.yakgwa.domain.common.schedule;

import java.time.LocalDateTime;

public interface TriggerScheduler {
    void schedule(Runnable task, LocalDateTime time);
}
