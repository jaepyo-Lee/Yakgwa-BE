package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.docs.PlaceVoteFindApi;
import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.service.place.PlaceVoteFindService;
import com.prography.yakgwa.domain.vote.service.VoteFinder;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceInfosByMeetStatus;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.base}")
public class PlaceVoteFindController implements PlaceVoteFindApi {
    private final VoteFinder<PlaceInfosByMeetStatus> voteFinder;

    public PlaceVoteFindController(PlaceVoteFindService placeVoteFindService) {
        this.voteFinder = placeVoteFindService;
    }

    //나의 장소투표 목록
    @Override
    @GetMapping("/meets/{meetId}/places")
    public SuccessResponse<PlaceVoteInfoWithStatusResponse> placeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                                  @PathVariable("meetId") Long meetId) {
        PlaceInfosByMeetStatus placeInfo = voteFinder.findVoteInfoWithStatusOf(user.getUserId(), meetId);
        return new SuccessResponse<>(PlaceVoteInfoWithStatusResponse.of(placeInfo.getVoteStatus(),
                placeInfo.getPlaces()));
    }

}
