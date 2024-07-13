package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class MeetController implements MeetApi{

    private final MeetService meetService;

    @PostMapping("/meets")
    public SuccessResponse<CreateMeetResponse> create(@AuthenticationPrincipal CustomUserDetail user,
                                                      @RequestBody @Valid CreateMeetRequest createMeetRequest) {
        Meet meet = meetService.create(createMeetRequest.toRequestDto(user.getUserId()));
        return new SuccessResponse<>(CreateMeetResponse.of(meet.getId()));
    }

    @GetMapping("/meets/{meetId}")
    public SuccessResponse<MeetInfoWithParticipantResponse> findDetail(@PathVariable("meetId") Long meetId) {
        MeetInfoWithParticipant meetWithParticipant = meetService.findWithParticipant(meetId);
        return new SuccessResponse<>(MeetInfoWithParticipantResponse.of(meetWithParticipant.getMeet(), meetWithParticipant.getParticipants()));
    }

    @GetMapping("/meets")
    public SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@AuthenticationPrincipal CustomUserDetail user) {
        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = meetService.findWithStatus(user.getUserId());
        return new SuccessResponse<>(MeetWithStatusInfoResponse.of(meetWithVoteAndStatuses));
    }
}
