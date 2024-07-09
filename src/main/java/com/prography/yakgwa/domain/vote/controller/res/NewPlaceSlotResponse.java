package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewPlaceSlotResponse {
    @Schema(description = "추가한 장소에 대한 정보")
    private PlaceSlotInMeet placeInfoDto;

    @Getter
    @Builder
    private static class PlaceSlotInMeet {
        @Schema(description = "장소명")
        private String title;
        @Schema(description = "장소 주소")
        private String address;
        @Schema(description = "장소 x좌표, 검색 구현시 없어질수 있음")
        private String mapx;
        @Schema(description = "장소 y좌표, 검색구현시 없어질수 있음")
        private String mapy;
    }

    public static NewPlaceSlotResponse of(Place place) {
        return NewPlaceSlotResponse.builder()
                .placeInfoDto(PlaceSlotInMeet.builder()
                        .address(place.getAddress())
                        .title(place.getTitle())
                        .mapx(place.getMapx())
                        .mapy(place.getMapy())
                        .build())
                .build();
    }
}
