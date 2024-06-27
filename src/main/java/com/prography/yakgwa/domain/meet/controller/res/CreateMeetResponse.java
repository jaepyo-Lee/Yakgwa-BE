package com.prography.yakgwa.domain.meet.controller.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMeetResponse {
    private Long meetId;
    public static CreateMeetResponse of(Long meetId){
        return CreateMeetResponse.builder()
                .meetId(meetId)
                .build();
    }
}
