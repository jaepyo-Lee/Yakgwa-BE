package com.prography.yakgwa.domain.vote.controller.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Vote", description = "투표관련 API입니다.")
public interface TimeVoteApi {
    @Operation(summary = "모임의 시간투표 API", description = "")
    SuccessResponse voteTimes(@AuthenticationPrincipal CustomUserDetail user,
                              @PathVariable("meetId") Long meetId,
                              @RequestBody EnableTimeRequest enableTimeRequest);


    @Operation(summary = "모임의 시간확정 API", description = "약과장만 가능")
    SuccessResponse<String> confirmTimeInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                              @PathVariable Long meetId,
                                              @RequestBody ConfirmTimeVoteInMeetRequest request) throws JsonProcessingException;
}
