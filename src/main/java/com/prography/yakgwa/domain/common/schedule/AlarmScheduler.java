package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.FcmMessageConverter;
import com.prography.yakgwa.domain.common.alarm.FirebaseMessageSender;
import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.slot.NotFoundTimeSlotException;
import com.prography.yakgwa.global.meta.ImplService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class AlarmScheduler {
    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ScheduledFuture<?>, Runnable> taskMap = new ConcurrentHashMap<>();
    private final AlarmJpaRepository alarmJpaRepository;
    public static final String MEET_ALARM_FORMAT = "MeetAlarm: %d";
    private final FirebaseMessageSender firebaseMessageSender;
    private final FcmMessageConverter fcmMessageConverter;
    private final ParticipantJpaRepository participantJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-08-7, 수, 14:38
     * Finish-Date)
     */
    @Transactional
    public boolean registerAlarm(Meet meet) {
        log.info("알람 등록 중: {}", meet.getId());

        Runnable alarmTask = createAlarmTask(meet);
        LocalDateTime alarmTime = getConfirmedTimeSlot(meet).getTime();
        scheduleTask(meet, alarmTask, alarmTime);
        saveAlarm(meet);
        return true;
    }

    private Runnable createAlarmTask(Meet meet) {
        String title = "약속잡는과정";
        String body = "\"" + meet.getTitle() + "\"" + " 모임 약속 3시간전입니다.";

        return () -> {
            List<Participant> participants = participantJpaRepository.findAllByMeetId(meet.getId());
            for (Participant participant : participants) {
                sendNotification(participant, title, body);
            }
            Alarm alarm = alarmJpaRepository.findByMeetId(meet.getId()).orElseThrow(() -> new RuntimeException("없는알람입니다."));
            alarm.send();
        };
    }

    private void sendNotification(Participant participant, String title, String body) {
        try {
            String message = fcmMessageConverter.makeMessage(participant.getUser().getFcmToken(), title, body);
            firebaseMessageSender.sendMessageTo(message);

        } catch (IOException e) {
            log.error("FCM 메시지 전송 실패: {}", participant.getUser().getId(), e);
        }
    }

    private TimeSlot getConfirmedTimeSlot(Meet meet) {
        return timeSlotJpaRepository.findAllByMeetId(meet.getId()).stream()
                .filter(TimeSlot::getConfirm)
                .findFirst()
                .orElseThrow(NotFoundTimeSlotException::new);
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
