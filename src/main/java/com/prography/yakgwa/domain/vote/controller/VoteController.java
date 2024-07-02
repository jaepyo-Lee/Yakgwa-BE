package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusReponse;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.service.VoteService;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class VoteController {
    private final VoteService voteService;

    @GetMapping("/users/{userId}/meets/{meetId}/places")
    public SuccessResponse<PlaceVoteInfoWithStatusReponse> placeInfoByMeetStatus(@PathVariable("userId") Long userId,
                                                                                 @PathVariable("meetId") Long meetId) {
        PlaceInfosByMeetStatus placeInfo = voteService.findPlaceInfoWithMeetStatus(userId, meetId);
        return new SuccessResponse<>(PlaceVoteInfoWithStatusReponse.of(placeInfo.getMeetStatus(),
                placeInfo.getPlaces()));
    }

    @GetMapping("/users/{userId}/meets/{meetId}/times")
    public SuccessResponse<TimeVoteInfoWithStatusResponse> timeInfoByMeetStatus(@PathVariable("userId") Long userId,
                                                                                @PathVariable("meetId") Long meetId) {
        TimeInfosByMeetStatus timeInfo = voteService.findTimeInfoWithMeetStatus(userId, meetId);
        return new SuccessResponse<>(TimeVoteInfoWithStatusResponse.of(timeInfo.getMeetStatus(), timeInfo.getTimeSlots()));
    }
}
