package com.prography.yakgwa.domain.meet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Meet", description = "모임관련 API입니다.")
public interface MeetCreateApi {
    @Operation(summary = "모임 생성 API", description = "")
    SuccessResponse<CreateMeetResponse> create(@AuthenticationPrincipal CustomUserDetail user,
                                               @RequestBody CreateMeetRequest createMeetRequest) throws JsonProcessingException;
}
