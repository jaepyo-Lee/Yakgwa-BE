package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.FcmMessageConverter;
import com.prography.yakgwa.domain.common.alarm.FirebaseMessageSender;
import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantReader;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@ImplService
public class ScheduleExecutor {
    private final TaskScheduler taskScheduler;
    private final FirebaseMessageSender messageSender;
    private final ParticipantReader participantReader;
    private final FcmMessageConverter fcmMessageConverter;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ScheduledFuture<?>, Runnable> taskMap = new ConcurrentHashMap<>();
    private final AlarmJpaRepository alarmJpaRepository;


    //taskId는 알림명+meetId+userId로 하면되겠다!
    @Transactional
    public void registerAlarm(String taskID, Meet meet, String title, String body, LocalDateTime alarmTime) {
        log.info("등록중");
        Instant instant = alarmTime.toInstant(ZoneOffset.of("+9"));

        Runnable runnable = () -> {
            try {
                //누구에게 보내야할지 정해야함(meetId로 조회 -> participant조회 -> user의 fcm뽑기 -> 메시지 생성 -> 전송)
                List<Participant> participants = participantReader.readAllWithUserByMeetId(meet.getId());
                for (Participant participant : participants) {
                    Alarm alarm = Alarm.builder()
                            .taskID(taskID).meet(meet)
                            .body(body).title(title).user(participant.getUser()).time(alarmTime)
                            .build();
                    alarmJpaRepository.save(alarm);
                    String fcmToken = participant.getUser().getFcmToken();
                    String fcmMessage = fcmMessageConverter.makeMessage(fcmToken, title, body);
                    messageSender.sendMessageTo(fcmMessage);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        register(taskID, runnable, instant);
    }

    private void register(String taskID, Runnable runnable, Instant instant) {
        ScheduledFuture<?> schedule = taskScheduler.schedule(runnable, instant);
        scheduleMap.put(taskID, schedule);
        taskMap.put(schedule, runnable);
    }


    public void changeAlarm(AlarmType alarmType, Meet meet, LocalDateTime time) {
        log.info("등록된 작업 시간 변경 :{}", LocalDateTime.now());
        String taskID = String.format("%s-%d", alarmType.toString(), meet.getId());

        Runnable runnable = cancelAlarm(taskID);

        register(taskID, runnable, time.toInstant(ZoneOffset.of("+9")));

        alarmJpaRepository.findByTaskID(taskID).ifPresent(alarm -> alarm.changeTime(time));

    }

    private Runnable cancelAlarm(String taskID) {
        ScheduledFuture<?> scheduledFuture = scheduleMap.get(taskID);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            ScheduledFuture<?> remove = scheduleMap.remove(taskID);
            return taskMap.remove(remove);
        } else {
            System.out.println("없는 ID");
            return null;
        }
    }
}
