package com.prography.yakgwa.domain.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.common.schedule.AlarmProcessor;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MessageDto messageDto = null;
        try {
            messageDto = mapper.readValue(message.getBody(), MessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Redis Subscribe Channel : ", messageDto.getMeetId());
        log.info("Redis SUB Message : {}", messageDto.toString());
        if (messageDto.getAlarmType().equals(AlarmType.END_VOTE)) {
            Optional<Meet> maybeMeet = voteMaxConfirmer.confirmMaxVote(messageDto.getMeetId());
            maybeMeet.ifPresent(meet->scheduleManager.registerAlarm(meet,AlarmType.PROMISE_DAY));
            alarmProcessor.sendAllFrom(messageDto.getMeetId(), messageDto.getAlarmType());
        } else {
            alarmProcessor.sendAllFrom(messageDto.getMeetId(), messageDto.getAlarmType());
        }
    }
}
