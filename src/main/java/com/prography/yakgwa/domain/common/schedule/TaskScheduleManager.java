package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.service.PlaceConfirm;
import com.prography.yakgwa.domain.vote.service.TimeConfirm;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.meta.ImplService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.prography.yakgwa.global.format.enumerate.AlarmType.END_VOTE;
import static com.prography.yakgwa.global.format.enumerate.AlarmType.PROMISE_DAY;
import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@ImplService
@Transactional
public class TaskScheduleManager {
    private final ScheduleRegister taskSchedulerManager;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final AlarmProcessor alarmProcessor;
    private final MeetJpaRepository meetJpaRepository;
    private final PlaceConfirm placeConfirm;
    private final TimeConfirm timeConfirm;
    private final MeetConfirmChecker meetConfirmChecker;
    private final AlarmJpaRepository alarmJpaRepository;
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
        boolean isSend = regist(meet, type);
        alarmJpaRepository.save(Alarm.builder()
                .alarmType(type)
                .send(isSend)
                .meet(meet)
                .build());
    }

    public boolean regist(Meet meet, AlarmType type) {
        if (type.equals(END_VOTE)) { // 확정 안되었을 때
            Runnable runnable = getRunnable(meet, type);
            taskSchedulerManager.scheduleTask(meet, type, runnable,/* meet.getVoteTime()*/now().plusMinutes(1L));
            return false;
        }
        Runnable runnable = getRunnable(meet, type);
        TimeSlot timeSlot = getConfirmedTimeSlot(meet).get();
        LocalDateTime time = timeSlot.getTime();
        taskSchedulerManager.scheduleTask(meet, type, runnable, time);
        return time.isBefore(now());
    }

    private Runnable getRunnable(Meet meet, AlarmType type) {
        Runnable runnable = null;
        if (type.equals(END_VOTE)) { // 확정 안되었을 때
            runnable = () -> {
                Meet callMeet = meetJpaRepository.findById(meet.getId()).orElseThrow(NotFoundMeetException::new);
                placeConfirm.confirmMaxOf(callMeet);
                timeConfirm.confirmMaxOf(callMeet);
                if (meetConfirmChecker.isMeetConfirm(callMeet)) {
                    registerAlarm(callMeet, PROMISE_DAY);
                }
                alarmProcessor.process(meet.getId(), type); // 알람을 보내는 메서드
            };
        } else if (type.equals(PROMISE_DAY)) {
            runnable = () -> alarmProcessor.process(meet.getId(), type);
        }
        return runnable;
    }

    private Optional<TimeSlot> getConfirmedTimeSlot(Meet meet) {
        return timeSlotJpaRepository.findAllByMeetId(meet.getId()).stream()
                .filter(TimeSlot::isConfirm)
                .findFirst();
    }
}
