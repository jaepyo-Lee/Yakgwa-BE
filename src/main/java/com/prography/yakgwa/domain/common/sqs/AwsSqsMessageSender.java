package com.prography.yakgwa.domain.common.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
public class AwsSqsMessageSender {
    private final SqsTemplate queueMessagingTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.cloud.aws.sqs.alarm-queue-name}")
    private String ALARM_QUEUE_NAME;
    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String QUEUE_NAME;//생각해보면 알람주는것만 있어도 될것같은데? 아니다 있어야겠네

    @Autowired
    public AwsSqsMessageSender(SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
        this.queueMessagingTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
        this.objectMapper = objectMapper;
    }

    public void sendReMessage(Object message) throws JsonProcessingException {
        String messageToString = objectMapper.writeValueAsString(message);
        queueMessagingTemplate.send(to -> to
                .queue(QUEUE_NAME)
                .delaySeconds(15 * 60) //sqs최대지연이 15분
                .payload(messageToString));
    }

    public void sendAlarmMessage(Object message) throws JsonProcessingException {
        String messageToString = objectMapper.writeValueAsString(message);
        queueMessagingTemplate.send(to -> to
                .queue(ALARM_QUEUE_NAME)
                .delaySeconds(15 * 60) //sqs최대지연이 15분
                .payload(messageToString));
    }
}
