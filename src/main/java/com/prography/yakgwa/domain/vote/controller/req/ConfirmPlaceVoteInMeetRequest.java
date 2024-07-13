package com.prography.yakgwa.domain.vote.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "장소확정요청")
@Getter
public class ConfirmPlaceVoteInMeetRequest {
    @Schema(description = "확정하고자 하는 장소의 id값")
    @NotNull(message = "확정하기위한 장소를 보내주세요.")
    private Long confirmPlaceSlotId;
}
