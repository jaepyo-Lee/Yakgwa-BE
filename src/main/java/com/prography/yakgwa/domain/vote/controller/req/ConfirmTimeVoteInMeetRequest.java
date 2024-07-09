package com.prography.yakgwa.domain.vote.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "시간확정요청")
@Getter
public class ConfirmTimeVoteInMeetRequest {
    @Schema(description = "확정하고자 하는 시간의 id값")
    private Long confirmTimeSlotId;
}
