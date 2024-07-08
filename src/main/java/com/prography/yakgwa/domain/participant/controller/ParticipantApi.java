package com.prography.yakgwa.domain.participant.controller;

import com.prography.yakgwa.domain.participant.controller.res.EnterMeetResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Participant", description = "모임 참여관련 API입니다.")
public interface ParticipantApi {
    SuccessResponse<EnterMeetResponse> enter(@AuthenticationPrincipal CustomUserDetail user,
                                             @PathVariable("meetId") Long meetId);
}
