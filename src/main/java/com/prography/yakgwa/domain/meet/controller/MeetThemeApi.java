package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.res.MeetThemeResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Theme", description = "모임 테마관련 API입니다.")
public interface MeetThemeApi {
    SuccessResponse<List<MeetThemeResponse>> readTheme();
}
