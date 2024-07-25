package com.prography.yakgwa.domain.place.service.dto;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceInfoWithUserLike {
    @Schema(description = "검색한 장소정보")
    private PlaceInfoDto placeInfoDto;
    @Schema(description = "즐겨찾기 여부")
    private Boolean isUserLike;
}
