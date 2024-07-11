package com.prography.yakgwa.domain.magazine.controller.req;

import com.prography.yakgwa.domain.magazine.service.req.CreateMagazineRequestDto;
import lombok.Getter;

@Getter
public class CreateMagazineRequest {
    private String content;
    private Long placeId;
    public CreateMagazineRequestDto toRequestDto(Long userId){
        return CreateMagazineRequestDto.builder()
                .contents(content).placeId(placeId).userId(userId)
                .build();
    }
}
