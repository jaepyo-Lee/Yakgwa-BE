package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.docs.MeetApi;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.domain.meet.controller.res.PostConfirmMeetInfoResponse;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class MeetController implements MeetApi {

    private final MeetService meetService;

    @GetMapping("/meets/{meetId}")
    public SuccessResponse<MeetInfoWithParticipantResponse> findDetail(@PathVariable("meetId") Long meetId) {
        MeetInfoWithParticipant meetWithParticipant = meetService.findWithParticipant(meetId);
        SuccessResponse<MeetInfoWithParticipantResponse> meetInfoWithParticipantResponseSuccessResponse = new SuccessResponse<>(MeetInfoWithParticipantResponse.of(meetWithParticipant.getMeet(), meetWithParticipant.getParticipants()));

        return meetInfoWithParticipantResponseSuccessResponse;
    }

    @GetMapping("/meets")
    public SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@AuthenticationPrincipal CustomUserDetail user) {
        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = meetService.findWithStatus(user.getUserId());
        return new SuccessResponse<>(MeetWithStatusInfoResponse.of(meetWithVoteAndStatuses));
    }

    @GetMapping("/meets/record")
    public SuccessResponse<PostConfirmMeetInfoResponse> findPostConfirmMeet(@AuthenticationPrincipal CustomUserDetail userDetail){
        List<MeetWithVoteAndStatus> postCofirm = meetService.findPostConfirm(userDetail.getUserId());
        return new SuccessResponse<>(PostConfirmMeetInfoResponse.of(postCofirm));
    }
}
