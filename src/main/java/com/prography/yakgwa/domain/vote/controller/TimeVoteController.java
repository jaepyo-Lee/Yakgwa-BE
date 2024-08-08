package com.prography.yakgwa.domain.vote.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.vote.controller.docs.TimeVoteApi;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.service.time.TimeVoteExecuteService;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.base}")
public class TimeVoteController implements TimeVoteApi {

    private final VoteExecuter<TimeVote, EnableTimeRequestDto> voteExecuter;

    @Autowired
    public TimeVoteController(TimeVoteExecuteService executer) {
        this.voteExecuter = executer;
    }

    //시간투표
    @Override
    @PostMapping("/meets/{meetId}/times")
    public SuccessResponse voteTimes(@AuthenticationPrincipal CustomUserDetail user,
                                     @PathVariable("meetId") Long meetId,
                                     @RequestBody @Valid EnableTimeRequest enableTimeRequest) {
        voteExecuter.vote(user.getUserId(), meetId, enableTimeRequest.toRequestDto());
        return SuccessResponse.ok("시간 투표하였습니다");
    }

    @Override
    @PatchMapping("/meets/{meetId}/times/confirm")
    public SuccessResponse<String> confirmTimeInMeet(@AuthenticationPrincipal CustomUserDetail user,
                                                     @PathVariable("meetId") Long meetId,
                                                     @RequestBody @Valid ConfirmTimeVoteInMeetRequest request) throws JsonProcessingException {
        voteExecuter.confirm(meetId, user.getUserId(), request.getConfirmTimeSlotId());
        return SuccessResponse.ok("시간이 확정되었습니다");
    }
}
