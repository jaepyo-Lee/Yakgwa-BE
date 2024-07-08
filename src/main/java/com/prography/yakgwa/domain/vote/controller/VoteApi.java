package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.req.ConfirmPlaceVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.domain.vote.controller.req.VotePlaceRequest;
import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusReponse;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Vote", description = "투표관련 API입니다.")
public interface VoteApi {
    SuccessResponse<PlaceVoteInfoWithStatusReponse> placeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                          @PathVariable("meetId") Long meetId);

    SuccessResponse<TimeVoteInfoWithStatusResponse> timeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                         @PathVariable("meetId") Long meetId);

    SuccessResponse votePlaces(@AuthenticationPrincipal CustomUserDetail user,
                               @PathVariable("meetId") Long meetId,
                               @RequestBody VotePlaceRequest votePlaceRequest);

    SuccessResponse voteTimes(@AuthenticationPrincipal CustomUserDetail user,
                              @PathVariable("meetId") Long meetId,
                              @RequestBody EnableTimeRequest enableTimeRequest);

    SuccessResponse<String> confirmPlaceInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                               @PathVariable Long meetId,
                                               @RequestBody ConfirmPlaceVoteInMeetRequest request);

    SuccessResponse<String> confirmTimeInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                              @PathVariable Long meetId,
                                              @RequestBody ConfirmTimeVoteInMeetRequest request);
}
