package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.redis.MessageDto;
import com.prography.yakgwa.domain.common.redis.RedisPublisher;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class TaskScheduleManager {
    private final TriggerScheduler taskSchedulerManager;
    private final RedisPublisher redisPublisher;
    private final ScheduleJpaRepository scheduleJpaRepository;
    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-08-7, 수, 14:38
     * Finish-Date)
     */
    /**
     * 모임확정생성시 약속당일에 알림보내기
     * 모임미확정생성시 24시간뒤 확정여부 계산과 확정되었다면 약속당일알람, 확정안되었다면  아무것도 없음
     */

    public void registerAlarm(Meet meet, AlarmType type) {
        log.info("알람 등록 중: {}", meet.getId());
        regist(meet,type);
    }

    public boolean regist(Meet meet,AlarmType type) {
        Schedule schedule = Schedule.builder().meetId(meet.getId()).alarmType(type).build();
        Schedule save = scheduleJpaRepository.save(schedule);
        Runnable runnable = () -> {
            scheduleJpaRepository.findById(save.getId()).ifPresent(Schedule::send);
            redisPublisher.publish(MessageDto.builder()
                    .meetId(meet.getId())
                    .alarmType(type)
                    .build());
        };
        taskSchedulerManager.schedule(runnable, now().plusSeconds(10L));
        return true;
    }
}
