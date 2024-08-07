package com.prography.yakgwa.domain.common.sqs;

import com.prography.yakgwa.domain.common.alarm.FcmMessageConverter;
import com.prography.yakgwa.domain.common.alarm.FirebaseMessageSender;
import com.prography.yakgwa.domain.common.schedule.AlarmScheduler;
import com.prography.yakgwa.domain.common.sqs.message.AlarmSqsMessage;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AwsSqsAlarmListenerService {
    private final ParticipantJpaRepository participantJpaRepository;
    private final FcmMessageConverter fcmMessageConverter;
    private final FirebaseMessageSender messageSender;
    private final MeetJpaRepository meetJpaRepository;
    private final MeetStatusJudger meetStatusJudger;
    private final AwsSqsMessageSender sqsMessageSender;
    private final AlarmScheduler alarmScheduler;

    @SqsListener(value = "${spring.cloud.aws.sqs.alarm-queue-name}")
    public void listenAfter24h(AlarmSqsMessage message) throws IOException {
        sendAlarm(message);
        Meet meet = meetJpaRepository.findById(message.getMeetId()).orElseThrow(NotFoundMeetException::new);
        //확정이라면 약속당일 알람
        if (meetStatusJudger.isConfirm(meet)){
            sqsMessageSender.sendAlarmMessage(AlarmSqsMessage.of(meet.getId(), meet.getEndTime().minusHours(3L), "확정되었습니다.", meet.getTitle() + "모임 3시간전입니다!"));
        }
        //확정이 아니라면 로컬 스케줄러에 저장해서 24시간뒤에 다시 확인 그사이에 확정되면 해당 스케줄링 삭제하기
    }

    private void sendAlarm(AlarmSqsMessage message) throws IOException {
        List<Participant> allByMeetId = participantJpaRepository.findAllByMeetId(message.getMeetId());
        for (Participant participant : allByMeetId) {
            String fcmToken = participant.getUser().getFcmToken();
            String fcmMessage = fcmMessageConverter.makeMessage(fcmToken, message.getTitle(), message.getBody());
            messageSender.sendMessageTo(fcmMessage);
        }
    }
}