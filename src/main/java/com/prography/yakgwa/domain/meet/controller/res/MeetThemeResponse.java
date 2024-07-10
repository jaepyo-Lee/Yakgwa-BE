package com.prography.yakgwa.domain.meet.controller.res;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeetThemeResponse {
    @Schema(description = "모임 테마id", example = "1")
    private Long id;
    @Schema(description = "모임테마명", example = "데이트")
    private String name;

    public static MeetThemeResponse of(MeetTheme meetTheme) {
        return MeetThemeResponse.builder()
                .id(meetTheme.getId())
                .name(meetTheme.getName())
                .build();
    }
}
