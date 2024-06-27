package com.prography.yakgwa.domain.meet.controller.res;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeetThemeResponse {
    private Long id;
    private String name;
    public static MeetThemeResponse of(MeetTheme meetTheme) {
        return MeetThemeResponse.builder()
                .id(meetTheme.getId())
                .name(meetTheme.getName())
                .build();
    }
}
