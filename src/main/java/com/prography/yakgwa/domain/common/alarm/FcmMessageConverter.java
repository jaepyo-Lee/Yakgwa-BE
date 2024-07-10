package com.prography.yakgwa.domain.common.alarm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.common.alarm.dto.FcmMessage;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ImplService
public class FcmMessageConverter {
    private final ObjectMapper objectMapper;

    public String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
}
