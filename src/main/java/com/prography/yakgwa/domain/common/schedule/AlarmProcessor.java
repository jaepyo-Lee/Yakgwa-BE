package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.common.alarm.FcmMessageConverter;
import com.prography.yakgwa.domain.common.alarm.FirebaseMessageSender;
import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@ImplService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmProcessor {
    private final AlarmJpaRepository alarmJpaRepository;
    private final FirebaseMessageSender firebaseMessageSender;
    private final FcmMessageConverter fcmMessageConverter;
    private final ParticipantJpaRepository participantJpaRepository;
    private final MeetJpaRepository meetJpaRepository;

    @Async
    @Transactional
    public void process(Long meetId, AlarmType alarmType) {

        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        String title = "약속잡는과정";

        String body = alarmType.makeBodyMessage(meet.getTitle());

        List<Participant> participants = participantJpaRepository.findAllByMeetId(meet.getId());
        for (Participant participant : participants) {
            sendNotification(participant, title, body);
        }
        Alarm alarm = alarmJpaRepository.findByMeetIdAndAlarmType(meet.getId(),alarmType).orElseThrow(() -> new RuntimeException("없는알람입니다."));
        alarm.send();
    }

    private void sendNotification(Participant participant, String title, String body) {
        try {
            String message = fcmMessageConverter.makeMessage(participant.getUser().getFcmToken(), title, body);
            log.info("{} 알림전송작업 진행",title);
            firebaseMessageSender.sendMessageTo(message);
            log.info("{} 알림전송작업 완료",title);

        } catch (IOException e) {
            log.error("FCM 메시지 전송 실패: {}", participant.getUser().getId(), e);
        }
    }
}
