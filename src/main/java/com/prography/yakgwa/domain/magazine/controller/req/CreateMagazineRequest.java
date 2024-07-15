package com.prography.yakgwa.domain.magazine.controller.req;

import com.prography.yakgwa.domain.magazine.service.req.CreateMagazineRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(example = "{\"placeId\": 1, \"title\": \"합정역데이트맛집\"}")
@Getter
public class CreateMagazineRequest {
    @Schema(description = "태그를 거는 장소의 아이디값")
    @NotNull(message = "매거진의 장소를 태그해주세요")
    private Long placeId;

    @Schema(description = "매거진 제목")
    @NotBlank(message = "매거진 제목은 필수요소입니다.")
    private String title;

    public CreateMagazineRequestDto toRequestDto(Long userId) {
        return CreateMagazineRequestDto.builder()
                .title(title).placeId(placeId).userId(userId)
                .build();
    }
}
