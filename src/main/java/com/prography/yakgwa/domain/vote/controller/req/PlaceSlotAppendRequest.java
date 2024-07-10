package com.prography.yakgwa.domain.vote.controller.req;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlaceSlotAppendRequest {
    @Schema(description = "후보지로 추가할 장소에 대한 정보")
    private PlaceInfoDto placeInfo;
}
