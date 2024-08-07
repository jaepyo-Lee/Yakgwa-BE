package com.prography.yakgwa.domain.common.sqs.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmSqsMessage {
    private Long meetId;
    private LocalDateTime endTime;
    private String title;
    private String body;

    public static AlarmSqsMessage of(Long meetId, LocalDateTime endTime, String title, String body) {
        return new AlarmSqsMessage(meetId, endTime, title, body);
    }
}
