package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.redis.MessageDto;
import com.prography.yakgwa.domain.common.redis.RedisPublisher;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class TaskScheduleManager {
    private final TriggerScheduler triggerScheduler;
    private final RedisPublisher redisPublisher;
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
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
        Schedule schedule = Schedule.builder().meetId(meet.getId()).alarmType(type).build();
        scheduleJpaRepository.save(schedule);
        regist(meet, type);
    }

    public void regist(Meet meet, AlarmType type) {
        Runnable runnable = () -> {
            scheduleJpaRepository.findByMeetIdAndAlarmType(meet.getId(), type).ifPresent(scheduleJpaRepository::delete);
            redisPublisher.publish(MessageDto.builder()
                    .meetId(meet.getId())
                    .alarmType(type)
                    .build());
        };
        LocalDateTime scheduleTime;
        if (type.equals(AlarmType.END_VOTE)) {
            scheduleTime = meet.getVoteTime();
        } else {
            TimeSlot timeSlot = timeSlotJpaRepository.findAllByMeetId(meet.getId()).stream()
                    .filter(TimeSlot::isConfirm)
                    .findFirst().get();
            scheduleTime = timeSlot.getTime();
        }
        triggerScheduler.schedule(runnable, scheduleTime);
    }
}
