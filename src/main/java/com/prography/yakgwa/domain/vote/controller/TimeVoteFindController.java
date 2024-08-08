package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.docs.TimeVoteFindApi;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.service.time.TimeVoteFindService;
import com.prography.yakgwa.domain.vote.service.VoteFinder;
import com.prography.yakgwa.domain.vote.service.time.res.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.base}")
public class TimeVoteFindController implements TimeVoteFindApi {
    private final VoteFinder<TimeInfosByMeetStatus> voteFinder;

    public TimeVoteFindController(TimeVoteFindService voteFinder) {
        this.voteFinder = voteFinder;
    }

    //나의 시간투표 목록
    @GetMapping("/meets/{meetId}/times")
    public SuccessResponse<TimeVoteInfoWithStatusResponse> timeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                                @PathVariable("meetId") Long meetId) {
        TimeInfosByMeetStatus timeInfo = voteFinder.findVoteInfoWithStatusOf(user.getUserId(), meetId);
        return new SuccessResponse<>(TimeVoteInfoWithStatusResponse.of(timeInfo.getVoteStatus(), timeInfo.getTimeSlots(), timeInfo.getMeet()));
    }
}
