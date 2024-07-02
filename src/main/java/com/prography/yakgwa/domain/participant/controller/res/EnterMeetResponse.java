package com.prography.yakgwa.domain.participant.controller.res;

import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EnterMeetResponse {
    private Long participantId;
    public static EnterMeetResponse of(Long participantId){
        return EnterMeetResponse.builder()
                .participantId(participantId)
                .build();
    }
}
