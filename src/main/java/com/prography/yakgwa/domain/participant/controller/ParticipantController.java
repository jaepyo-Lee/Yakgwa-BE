package com.prography.yakgwa.domain.participant.controller;

import com.prography.yakgwa.domain.participant.controller.res.EnterMeetResponse;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.service.ParticipantService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/users/{userId}/meets/{meetId}")
    public SuccessResponse<EnterMeetResponse> enter(@PathVariable("userId") Long userId,
                                                    @PathVariable("meetId") Long meetId) {
        Participant participant = participantService.enterMeet(userId, meetId);
        return new SuccessResponse<>(EnterMeetResponse.of(participant.getId()));
    }
}
