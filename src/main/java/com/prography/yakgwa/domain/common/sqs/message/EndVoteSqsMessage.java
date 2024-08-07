package com.prography.yakgwa.domain.common.sqs.message;


import com.prography.yakgwa.domain.meet.entity.Meet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EndVoteSqsMessage {
    private Long meetId;
    private LocalDateTime endTime;

    public static EndVoteSqsMessage of(Meet meet) {
        return new EndVoteSqsMessage(meet.getId(), meet.getEndTime());
    }
}
