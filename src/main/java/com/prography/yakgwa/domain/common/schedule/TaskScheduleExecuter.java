package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.slot.NotFoundTimeSlotException;
import com.prography.yakgwa.global.meta.ImplService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class TaskScheduleExecuter {
    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ScheduledFuture<?>, Runnable> taskMap = new ConcurrentHashMap<>();
    private final AlarmJpaRepository alarmJpaRepository;
    public static final String MEET_ALARM_FORMAT = "MeetAlarm: %d";
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final AfterVoteTaskExectorFacade exectorFacade;

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-08-7, 수, 14:38
     * Finish-Date)
     */
    /**
     * 코드 별론데?
     */
    @Transactional
    public void registerAlarm(Meet meet, AlarmType type) {
        log.info("알람 등록 중: {}", meet.getId());
        Runnable runnable = () -> exectorFacade.execute(meet.getId(), type);

        Optional<TimeSlot> confirmedTimeSlot = getConfirmedTimeSlot(meet);

        confirmedTimeSlot.ifPresent(timeSlot -> {
            LocalDateTime time = timeSlot.getTime();
            scheduleTask(meet, runnable, time);
            saveAlarm(meet);
        });
    }

    private Optional<TimeSlot> getConfirmedTimeSlot(Meet meet) {
        return timeSlotJpaRepository.findAllByMeetId(meet.getId()).stream()
                .filter(TimeSlot::getConfirm)
                .findFirst();

    }

    private void scheduleTask(Meet meet, Runnable task, LocalDateTime time) {
        String taskID = String.format(MEET_ALARM_FORMAT, meet.getId());
        Instant instant = time.toInstant(ZoneOffset.of("+9"));

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, instant);
        scheduleMap.put(taskID, scheduledFuture);
        taskMap.put(scheduledFuture, task);
    }

    private void saveAlarm(Meet meet) {
        alarmJpaRepository.save(Alarm.builder().meet(meet).build());
    }

    @Transactional
    public void changeAlarm(Meet meet, LocalDateTime newTime) {
        log.info("알람 시간 변경 요청: {}", meet.getId());

        String taskID = String.format(MEET_ALARM_FORMAT, meet.getId());
        Runnable alarmTask = cancelAlarm(taskID);

        if (alarmTask != null) {
            scheduleTask(meet, alarmTask, newTime);
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
    }
}
