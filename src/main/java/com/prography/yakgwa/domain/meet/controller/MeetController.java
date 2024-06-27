package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.controller.res.CreateMeetResponse;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class MeetController {

    private final MeetService meetService;

    @PostMapping("/meets")
    public SuccessResponse create(@RequestBody CreateMeetRequest createMeetRequest) {
        Meet meet = meetService.create(createMeetRequest.toRequestDto());
        return new SuccessResponse(CreateMeetResponse.of(meet.getId()));
    }
}
