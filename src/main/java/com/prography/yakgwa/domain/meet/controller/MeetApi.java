package com.prography.yakgwa.domain.meet.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.domain.meet.controller.res.PostConfirmMeetInfoResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Meet", description = "모임관련 API입니다.")
public interface MeetApi {
    @Operation(summary = "모임 생성 API", description = "")
    SuccessResponse<CreateMeetResponse> create(@AuthenticationPrincipal CustomUserDetail user,
                                               @RequestBody CreateMeetRequest createMeetRequest) throws JsonProcessingException;

    @Operation(summary = "모임의 상세정보 조회 API")
    SuccessResponse<MeetInfoWithParticipantResponse> findDetail(@PathVariable("meetId") Long meetId);

    @Operation(summary = "사용자가 현재 참여중인 모임목록 조회 API", description = "")
    SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@AuthenticationPrincipal CustomUserDetail user);

    @Operation(summary = "사용자의 약속히스토리 api", description = "현재 시점에서 확정한 약속 시간 +1시간 이상인 항목만 노출<br>" +
            "정렬 기준 : 약속 시간 최신순")
    SuccessResponse<PostConfirmMeetInfoResponse> findPostConfirmMeet(@AuthenticationPrincipal CustomUserDetail userDetail);
}
