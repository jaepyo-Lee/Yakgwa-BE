package com.prography.yakgwa.domain.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.common.schedule.AlarmProcessor;
import com.prography.yakgwa.domain.common.schedule.ScheduleJpaRepository;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.service.VoteMaxConfirmer;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AlarmProcessor alarmProcessor;
    private final VoteMaxConfirmer voteMaxConfirmer;
    private final TaskScheduleManager scheduleManager;
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MessageDto messageDto = null;
        try {
            messageDto = mapper.readValue(message.getBody(), MessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
/*
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());
*/
//            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

        log.info("Redis Subscribe Channel : ", messageDto.getMeetId());
        log.info("Redis SUB Message : {}", messageDto.toString());

        // Return || Another Method Call (etc.sae to DB)
        // TODO
        /*
         * 여기 알림이 들어갈 예정이다.
         *
         * */
        if (messageDto.getAlarmType().equals(AlarmType.END_VOTE)) {
            scheduleJpaRepository.findByMeetIdAndAlarmType(messageDto.getMeetId(), AlarmType.END_VOTE).ifPresent(scheduleJpaRepository::delete);
            Optional<Meet> maybeMeet = voteMaxConfirmer.confirmMaxVote(messageDto.getMeetId());
            maybeMeet.ifPresent(meet->scheduleManager.regist(meet,AlarmType.PROMISE_DAY));
            alarmProcessor.sendAllFrom(messageDto.getMeetId(), messageDto.getAlarmType());
        } else {
            scheduleJpaRepository.findByMeetIdAndAlarmType(messageDto.getMeetId(), AlarmType.PROMISE_DAY).ifPresent(scheduleJpaRepository::delete);
            alarmProcessor.sendAllFrom(messageDto.getMeetId(), messageDto.getAlarmType());
        }
    }
    /*private Runnable getRunnable(Meet meet, AlarmType type) {
        Runnable runnable = null;
        if (type.equals(END_VOTE)) { // 확정 안되었을 때
            runnable = () -> {
                Meet callMeet = meetJpaRepository.findById(meet.getId()).orElseThrow(NotFoundMeetException::new);
                voteMaxConfirmer.confirmMaxVote(meet);
                if (meetConfirmChecker.isMeetConfirm(callMeet)) {
                    registerAlarm(callMeet, PROMISE_DAY);
                }
                alarmProcessor.sendAllFrom(meet.getId(), type); // 알람을 보내는 메서드
            };
        } else if (type.equals(PROMISE_DAY)) {
            runnable = () -> alarmProcessor.sendAllFrom(meet.getId(), type);
        }
        return runnable;
    }

    private Optional<TimeSlot> getConfirmedTimeSlot(Meet meet) {
        return timeSlotJpaRepository.findAllByMeetId(meet.getId()).stream()
                .filter(TimeSlot::isConfirm)
                .findFirst();
    }*/
}
