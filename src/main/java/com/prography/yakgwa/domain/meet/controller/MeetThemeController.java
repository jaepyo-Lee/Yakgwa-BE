package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.res.MeetThemeResponse;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.MeetThemeService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("${app.api.base}")
@RestController
public class MeetThemeController {
    private final MeetThemeService meetThemeService;

    @GetMapping("/theme")
    public SuccessResponse<List<MeetThemeResponse>> readTheme() {
        List<MeetTheme> meetThemes = meetThemeService.getMeetThemes();
        return new SuccessResponse<>(meetThemes.stream()
                .map(MeetThemeResponse::of)
                .toList());
    }
}
