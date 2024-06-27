package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class MeetController {

    private final MeetService meetService;

    @PostMapping("/meets")
    public SuccessResponse<CreateMeetResponse> create(@RequestBody CreateMeetRequest createMeetRequest) {
        Meet meet = meetService.create(createMeetRequest.toRequestDto());
        return new SuccessResponse<>(CreateMeetResponse.of(meet.getId()));
    }

    @GetMapping("/meets/{meetId}")
    public SuccessResponse<MeetInfoWithParticipantResponse> findDetail(@PathVariable("meetId") Long meetId) {
        MeetInfoWithParticipant meetWithParticipant = meetService.findWithParticipant(meetId);
        return new SuccessResponse<>(MeetInfoWithParticipantResponse.of(meetWithParticipant.getMeet(), meetWithParticipant.getParticipants()));
    }

    @GetMapping("/users/{userId}/meets")
    public SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@PathVariable("userId") Long userId) {
        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = meetService.findWithStatus(userId);
        return new SuccessResponse<>(MeetWithStatusInfoResponse.of(meetWithVoteAndStatuses));
    }
}
