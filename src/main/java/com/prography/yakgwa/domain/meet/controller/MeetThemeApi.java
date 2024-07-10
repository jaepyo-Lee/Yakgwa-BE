package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.meet.controller.res.MeetThemeResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Meet", description = "모임관련 API입니다.")
public interface MeetThemeApi {
    @Operation(summary = "모임생성시 선택가능한 모임테마 조회 API")
    SuccessResponse<List<MeetThemeResponse>> readTheme();
}
