package com.prography.yakgwa.domain.magazine.service.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMagazineRequestDto {
    private Long userId;
    private Long placeId;
    private String title;
}
