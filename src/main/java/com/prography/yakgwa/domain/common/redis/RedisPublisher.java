package com.prography.yakgwa.domain.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> template;
    public RedisPublisher(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    /**
     * Object publish
     */
    public void publish(MessageDto dto) {
        log.info("Publishing message: {}", dto);
        log.info("전송");
        ChannelTopic topic = ChannelTopic.of("topic1");
        template.convertAndSend(topic.getTopic(), dto);
    }

    /**
     * String publish
     */
    public void publish(ChannelTopic topic ,String data) {
        template.convertAndSend(topic.getTopic(), data);
    }
}
