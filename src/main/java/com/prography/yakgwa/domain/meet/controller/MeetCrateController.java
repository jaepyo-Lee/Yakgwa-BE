package com.prography.yakgwa.domain.meet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.service.MeetCreateService;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class MeetCrateController implements MeetCreateApi {
    private final MeetCreateService meetCreateService;

    @PostMapping("/meets")
    public SuccessResponse<CreateMeetResponse> create(@AuthenticationPrincipal CustomUserDetail user,
                                                      @RequestBody @Valid CreateMeetRequest createMeetRequest) throws JsonProcessingException {
        Meet meet = meetCreateService.create(createMeetRequest.toRequestDto(user.getUserId()));
        return new SuccessResponse<>(CreateMeetResponse.of(meet.getId()));
    }
}
