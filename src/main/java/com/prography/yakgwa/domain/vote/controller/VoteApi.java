package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.req.ConfirmPlaceVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.domain.vote.controller.req.VotePlaceRequest;
import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Vote", description = "투표관련 API입니다.")
public interface VoteApi {
    @Operation(summary = "모임의 사용자가 투표한 장소목록 조회 API", description = "")
    SuccessResponse<PlaceVoteInfoWithStatusResponse> placeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                           @PathVariable("meetId") Long meetId);

    @Operation(summary = "특정 모임의 내가 투표한 시간 조회 API", description = "")
    SuccessResponse<TimeVoteInfoWithStatusResponse> timeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                         @PathVariable("meetId") Long meetId);

    @Operation(summary = "모임의 장소투표 API", description = "")
    SuccessResponse votePlaces(@AuthenticationPrincipal CustomUserDetail user,
                               @PathVariable("meetId") Long meetId,
                               @RequestBody VotePlaceRequest votePlaceRequest);

    @Operation(summary = "모임의 시간투표 API", description = "")
    SuccessResponse voteTimes(@AuthenticationPrincipal CustomUserDetail user,
                              @PathVariable("meetId") Long meetId,
                              @RequestBody EnableTimeRequest enableTimeRequest);

    @Operation(summary = "모임의 장소확정 API", description = "약과장만 가능")
    SuccessResponse<String> confirmPlaceInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                               @PathVariable Long meetId,
                                               @RequestBody ConfirmPlaceVoteInMeetRequest request);

    @Operation(summary = "모임의 시간확정 API", description = "약과장만 가능")
    SuccessResponse<String> confirmTimeInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                              @PathVariable Long meetId,
                                              @RequestBody ConfirmTimeVoteInMeetRequest request);
}
