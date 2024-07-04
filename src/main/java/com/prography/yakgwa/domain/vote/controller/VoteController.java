package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.req.ConfirmPlaceVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.VotePlaceRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusReponse;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.service.VoteService;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class VoteController {
    private final VoteService voteService;

    //나의 장소투표 목록
    @GetMapping("/users/{userId}/meets/{meetId}/places")
    public SuccessResponse<PlaceVoteInfoWithStatusReponse> placeInfoByMeetStatus(@PathVariable("userId") Long userId,
                                                                                 @PathVariable("meetId") Long meetId) {
        PlaceInfosByMeetStatus placeInfo = voteService.findPlaceInfoWithMeetStatus(userId, meetId);
        return new SuccessResponse<>(PlaceVoteInfoWithStatusReponse.of(placeInfo.getMeetStatus(),
                placeInfo.getPlaces()));
    }

    //나의 시간투표 목록
    @GetMapping("/users/{userId}/meets/{meetId}/times")
    public SuccessResponse<TimeVoteInfoWithStatusResponse> timeInfoByMeetStatus(@PathVariable("userId") Long userId,
                                                                                @PathVariable("meetId") Long meetId) {
        TimeInfosByMeetStatus timeInfo = voteService.findTimeInfoWithMeetStatus(userId, meetId);
        return new SuccessResponse<>(TimeVoteInfoWithStatusResponse.of(timeInfo.getMeetStatus(), timeInfo.getTimeSlots()));
    }

    //장소투표
    @PostMapping("/users/{userId}/meets/{meetId}/places")
    public SuccessResponse votePlaces(@PathVariable("userId") Long userId,
                                      @PathVariable("meetId") Long meetId,
                                      @RequestBody VotePlaceRequest votePlaceRequest) {
        voteService.votePlace(userId, meetId, votePlaceRequest.getCurrentVotePlaceSlotIds());
        return SuccessResponse.ok("장소 투표하였습니다.");
    }

    //장소투표
    @PostMapping("/users/{userId}/meets/{meetId}/times")
    public SuccessResponse voteTimes(@PathVariable("userId") Long userId,
                                     @PathVariable("meetId") Long meetId,
                                     @RequestBody EnableTimeRequest enableTimeRequest) {
        voteService.voteTime(userId, meetId, enableTimeRequest.toRequestDto());
        return SuccessResponse.ok("시간 투표하였습니다");
    }

    @PatchMapping("/users/{userId}/meets/{meetId}/places/confirm")
    public SuccessResponse<String> confirmPlaceInMeet(@PathVariable Long userId,
                                                      @PathVariable Long meetId,
                                                      @RequestBody ConfirmPlaceVoteInMeetRequest request) {
        voteService.confirmPlace(userId, meetId, request.getConfirmPlaceSlotId());
        return SuccessResponse.ok("장소가 확정되었습니다");
    }

    @PatchMapping("/users/{userId}/meets/{meetId}/times/confirm")
    public SuccessResponse<String> confirmTimeInMeet(@PathVariable Long userId,
                                                     @PathVariable Long meetId,
                                                     @RequestBody ConfirmTimeVoteInMeetRequest request) {
        voteService.confirmTime(userId, meetId, request.getConfirmTimeSlotId());
        return SuccessResponse.ok("시간이 확정되었습니다");
    }
}