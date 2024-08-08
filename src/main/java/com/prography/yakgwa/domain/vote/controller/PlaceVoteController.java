package com.prography.yakgwa.domain.vote.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.vote.controller.docs.PlaceVoteApi;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmPlaceVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.VotePlaceRequest;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.service.place.PlaceVoteExecuteService;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${app.api.base}")
public class PlaceVoteController implements PlaceVoteApi {
    private final VoteExecuter<PlaceVote, Set<Long>> executer;

    @Autowired
    public PlaceVoteController(PlaceVoteExecuteService executer) {
        this.executer = executer;
    }

    //장소투표
    @Override
    @PostMapping("/meets/{meetId}/places")
    public SuccessResponse votePlaces(@AuthenticationPrincipal CustomUserDetail user,
                                      @PathVariable("meetId") Long meetId,
                                      @RequestBody @Valid VotePlaceRequest votePlaceRequest) {
        List<PlaceVote> vote = executer.vote(user.getUserId(), meetId, votePlaceRequest.getCurrentVotePlaceSlotIds());
        return SuccessResponse.ok("장소 투표하였습니다.");
    }

    @Override
    @PatchMapping("/meets/{meetId}/places/confirm")
    public SuccessResponse<String> confirmPlaceInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                                      @PathVariable("meetId") Long meetId,
                                                      @RequestBody @Valid ConfirmPlaceVoteInMeetRequest request) throws JsonProcessingException {
        executer.confirm(user.getUserId(), meetId, request.getConfirmPlaceSlotId());
        return SuccessResponse.ok("장소가 확정되었습니다");
    }
}
