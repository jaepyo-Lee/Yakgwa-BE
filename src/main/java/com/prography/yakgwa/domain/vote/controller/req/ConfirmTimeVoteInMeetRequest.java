package com.prography.yakgwa.domain.vote.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "시간확정요청")
@Getter
public class ConfirmTimeVoteInMeetRequest {
    @Schema(description = "확정하고자 하는 시간의 id값")
    @NotNull(message = "확정하기위한 시간을 보내주세요")
    private Long confirmTimeSlotId;
}
