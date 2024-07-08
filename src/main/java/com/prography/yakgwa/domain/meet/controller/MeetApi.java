package com.prography.yakgwa.domain.meet.controller;


import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Meet", description = "모임관련 API입니다.")
public interface MeetApi {
    SuccessResponse<CreateMeetResponse> create(@AuthenticationPrincipal CustomUserDetail user,
                                                      @RequestBody CreateMeetRequest createMeetRequest);

    SuccessResponse<MeetInfoWithParticipantResponse> findDetail(@PathVariable("meetId") Long meetId);

    SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@AuthenticationPrincipal CustomUserDetail user);
}
