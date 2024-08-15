package com.prography.yakgwa.domain.vote.controller.req;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceSlotAppendRequest {
    @Schema(description = "후보지로 추가할 장소에 대한 정보")
    @NotNull(message = "장소정보는 필수정보입니다.")
    private PlaceInfoDto placeInfo;
}
